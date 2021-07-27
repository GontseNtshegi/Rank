package com.rank.dagacube.service.impl;

import com.rank.dagacube.domain.PlayersAudit;
import com.rank.dagacube.repository.PlayersAuditRepository;
import com.rank.dagacube.service.PlayersAuditService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PlayersAudit}.
 */
@Service
@Transactional
public class PlayersAuditServiceImpl implements PlayersAuditService {

    private final Logger log = LoggerFactory.getLogger(PlayersAuditServiceImpl.class);

    private final PlayersAuditRepository playersAuditRepository;

    public PlayersAuditServiceImpl(PlayersAuditRepository playersAuditRepository) {
        this.playersAuditRepository = playersAuditRepository;
    }

    @Override
    public PlayersAudit save(PlayersAudit playersAudit) {
        log.debug("Request to save PlayersAudit : {}", playersAudit);
        return playersAuditRepository.save(playersAudit);
    }

    @Override
    public Optional<PlayersAudit> partialUpdate(PlayersAudit playersAudit) {
        log.debug("Request to partially update PlayersAudit : {}", playersAudit);

        return playersAuditRepository
            .findById(playersAudit.getTransactionId())
            .map(
                existingPlayersAudit -> {
                    if (playersAudit.getEventDate() != null) {
                        existingPlayersAudit.setEventDate(playersAudit.getEventDate());
                    }
                    if (playersAudit.getOperation() != null) {
                        existingPlayersAudit.setOperation(playersAudit.getOperation());
                    }
                    if (playersAudit.getWinningMoney() != null) {
                        existingPlayersAudit.setWinningMoney(playersAudit.getWinningMoney());
                    }
                    if (playersAudit.getPlayerId() != null) {
                        existingPlayersAudit.setPlayerId(playersAudit.getPlayerId());
                    }
                    if (playersAudit.getPromotion() != null) {
                        existingPlayersAudit.setPromotion(playersAudit.getPromotion());
                    }

                    return existingPlayersAudit;
                }
            )
            .map(playersAuditRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayersAudit> findAll() {
        log.debug("Request to get all PlayersAudits");
        return playersAuditRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlayersAudit> findOne(String id) {
        log.debug("Request to get PlayersAudit : {}", id);
        return playersAuditRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete PlayersAudit : {}", id);
        playersAuditRepository.deleteById(id);
    }
}
