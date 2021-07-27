package com.rank.dagacube.web.rest;

import com.rank.dagacube.domain.PlayersAudit;
import com.rank.dagacube.service.PlayersAuditService;
import com.rank.dagacube.web.api.Last10WinApiDelegate;
import com.rank.dagacube.web.rest.errors.PlayersException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zalando.problem.Status;

@Service
public class Last10WinResource implements Last10WinApiDelegate {

    @Autowired
    private final PlayersAuditService playersAuditService;

    Last10WinResource(PlayersAuditService playersAuditService) {
        this.playersAuditService = playersAuditService;
    }

    @Override
    public ResponseEntity<List<BigDecimal>> getLast10win(Long id) {
        List<BigDecimal> response = new ArrayList<>();

        List<PlayersAudit> playersAuditList = playersAuditService.findTop10ByPlayerIdAndWinningMoneyGreaterThanOrderByEventDateDesc(id, 0L);
        if (!playersAuditList.isEmpty()) {
            playersAuditList
                .stream()
                .forEach(
                    playersAudit -> {
                        response.add(BigDecimal.valueOf(playersAudit.getWageringMoney()));
                    }
                );
        } else throw new PlayersException("Not Found", Status.NOT_FOUND);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
