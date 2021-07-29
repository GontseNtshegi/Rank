package com.rank.dagacube.web.rest;

import com.rank.dagacube.config.Constants;
import com.rank.dagacube.domain.PlayersAudit;
import com.rank.dagacube.service.PlayersAuditService;
import com.rank.dagacube.service.PlayersService;
import com.rank.dagacube.web.api.WageApiDelegate;
import com.rank.dagacube.web.api.model.WageRequest;
import com.rank.dagacube.web.api.model.WageResponse;
import com.rank.dagacube.web.rest.errors.BadRequestAlertException;
import com.rank.dagacube.web.rest.errors.PlayersException;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Status;

@Service
public class WageResource implements WageApiDelegate {

    @Autowired
    private final PlayersService playersService;

    @Autowired
    private final PlayersAuditService playersAuditService;

    WageResource(PlayersService playersService, PlayersAuditService playersAuditService) {
        this.playersService = playersService;
        this.playersAuditService = playersAuditService;
    }

    @Transactional
    @Override
    public ResponseEntity<WageResponse> deductMoney(WageRequest wageRequest) {
        WageResponse wageResponse = new WageResponse();

        //Check if transaction is unique
        Optional<PlayersAudit> playersAuditOptional = playersAuditService.findOne(wageRequest.getTransactionId());
        if (!playersAuditOptional.isPresent()) {
            playersService
                .findOne(wageRequest.getPlayerId())
                .ifPresent(
                    players -> {
                        Long balance = players.getPromoLeft() == null
                            ? players.getCurrentBalance() - wageRequest.getWage().longValue()
                            : players.getCurrentBalance();
                        if (balance < 0) throw new PlayersException("It is a teapot", Status.I_AM_A_TEAPOT); else {
                            String promo = wageRequest.getPromotion() != null &&
                                wageRequest.getPromotion().equalsIgnoreCase(Constants.PROMOTION)
                                ? "Y"
                                : null;

                            players.setCurrentBalance(balance);
                            players.setWageringMoney(wageRequest.getWage().longValue());
                            if (players.getPromoLeft() != null) {
                                players.setPromoLeft(
                                    promo != null
                                        ? (players.getPromoLeft() != null ? (players.getPromoLeft() - 1) : Constants.NEXT_PROMOTION)
                                        : (players.getPromoLeft() - 1)
                                );
                            }

                            if (players.getPromoLeft() != null && players.getPromoLeft() == 0) players.setPromoLeft(null);
                            playersService.save(players);

                            //Audit the event
                            PlayersAudit playersAudit = new PlayersAudit()
                                .eventDate(ZonedDateTime.now())
                                .operation(Constants.WAGE)
                                .playerId(wageRequest.getPlayerId())
                                .promotion(promo)
                                .transactionId(wageRequest.getTransactionId())
                                .wageringMoney(wageRequest.getWage().longValue());

                            playersAuditService.save(playersAudit);

                            wageResponse.setPlayerId(wageRequest.getPlayerId());
                            wageResponse.setTransactionId(wageRequest.getTransactionId());
                        }
                    }
                );
        } else throw new PlayersException("Idempodent", Status.ALREADY_REPORTED);

        if (wageResponse.getPlayerId() == null) throw new PlayersException("Player Nor Found", Status.NOT_FOUND);

        return new ResponseEntity<>(wageResponse, HttpStatus.OK);
    }
}
