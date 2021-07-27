package com.rank.dagacube.web.rest;

import com.rank.dagacube.config.Constants;
import com.rank.dagacube.domain.PlayersAudit;
import com.rank.dagacube.service.PlayersAuditService;
import com.rank.dagacube.service.PlayersService;
import com.rank.dagacube.web.api.CurrentBalanceApiDelegate;
import com.rank.dagacube.web.api.model.BalanceGetResponse;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentBalanceResource implements CurrentBalanceApiDelegate {

    @Autowired
    private final PlayersService playersService;

    @Autowired
    private final PlayersAuditService playersAuditService;

    CurrentBalanceResource(PlayersService playersService, PlayersAuditService playersAuditService) {
        this.playersService = playersService;
        this.playersAuditService = playersAuditService;
    }

    @Transactional
    @Override
    public ResponseEntity<BalanceGetResponse> getCurrentBalance(Long id) {
        BalanceGetResponse balanceGetResponse = new BalanceGetResponse();

        playersService
            .findOne(id)
            .ifPresent(
                players -> {
                    balanceGetResponse.setCurrentBalance(BigDecimal.valueOf(players.getCurrentBalance()));
                    balanceGetResponse.setTransactionId(UUID.randomUUID().toString());

                    //Audit the event
                    PlayersAudit playersAudit = new PlayersAudit()
                        .eventDate(ZonedDateTime.now())
                        .operation(Constants.VIEW_BALANCE)
                        .playerId(id)
                        .promotion(null)
                        .transactionId(balanceGetResponse.getTransactionId());
                    //.winningMoney(depositRequest.getWin().longValue());
                    playersAuditService.save(playersAudit);
                }
            );

        return new ResponseEntity<>(balanceGetResponse, HttpStatus.OK);
    }
}
