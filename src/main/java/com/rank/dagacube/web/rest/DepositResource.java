package com.rank.dagacube.web.rest;

import com.rank.dagacube.config.Constants;
import com.rank.dagacube.domain.PlayersAudit;
import com.rank.dagacube.service.PlayersAuditService;
import com.rank.dagacube.service.PlayersService;
import com.rank.dagacube.web.api.DepositApiDelegate;
import com.rank.dagacube.web.api.model.DepositRequest;
import com.rank.dagacube.web.api.model.DepositResponse;
import com.rank.dagacube.web.rest.errors.PlayersException;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

public class DepositResource implements DepositApiDelegate {

    @Autowired
    private final PlayersService playersService;

    @Autowired
    private final PlayersAuditService playersAuditService;

    DepositResource(PlayersService playersService, PlayersAuditService playersAuditService) {
        this.playersService = playersService;
        this.playersAuditService = playersAuditService;
    }

    @Override
    public ResponseEntity<DepositResponse> depositMoney(DepositRequest depositRequest) {
        DepositResponse depositResponse = new DepositResponse();
        //Check if transaction is unique
        Optional<PlayersAudit> playersAuditOptional = playersAuditService.findOne(depositRequest.getTransactionId());
        if (!playersAuditOptional.isPresent()) {
            playersService
                .findOne(depositRequest.getPlayerId())
                .ifPresent(
                    players -> {
                        players.setCurrentBalance(players.getCurrentBalance() + depositRequest.getWin().longValue());
                        players.setWinningMoney(depositRequest.getWin().longValue());
                        playersService.save(players);

                        //Audit the event
                        PlayersAudit playersAudit = new PlayersAudit()
                            .eventDate(ZonedDateTime.now())
                            .operation(Constants.DEPOSIT)
                            .playerId(depositRequest.getPlayerId())
                            .promotion(null)
                            .transactionId(depositRequest.getTransactionId())
                            .winningMoney(depositRequest.getWin().longValue());
                        playersAuditService.save(playersAudit);

                        depositResponse.setPlayerId(depositRequest.getPlayerId());
                        depositResponse.setTransactionId(depositRequest.getTransactionId());
                    }
                );
        } else throw new PlayersException("Idempodent", Status.ALREADY_REPORTED);

        if (depositResponse != null) throw new PlayersException("Player Nor Found", Status.NOT_FOUND);

        return new ResponseEntity<>(depositResponse, HttpStatus.OK);
    }
}
