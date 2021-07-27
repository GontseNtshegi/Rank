package com.rank.dagacube.service;

import com.rank.dagacube.domain.PlayersAudit;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link PlayersAudit}.
 */
public interface PlayersAuditService {
    /**
     * Save a playersAudit.
     *
     * @param playersAudit the entity to save.
     * @return the persisted entity.
     */
    PlayersAudit save(PlayersAudit playersAudit);

    /**
     * Partially updates a playersAudit.
     *
     * @param playersAudit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlayersAudit> partialUpdate(PlayersAudit playersAudit);

    /**
     * Get all the playersAudits.
     *
     * @return the list of entities.
     */
    List<PlayersAudit> findAll();

    /**
     * Get the "id" playersAudit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayersAudit> findOne(String id);

    /**
     * Delete the "id" playersAudit.
     *
     * @param id the id of the entity.
     */
    void delete(String id);

    /**
     *
     * @param playerId
     * @param winningMoney
     * @return
     */
    List<PlayersAudit> findTop10ByPlayerIdAndWinningMoneyGreaterThanOrderByEventDateDesc(Long playerId, Long winningMoney);

    /**
     *
     * @param playerId
     * @param wageringMoney
     * @return
     */
    List<PlayersAudit> findTop10ByPlayerIdAndWageringMoneyGreaterThanOrderByEventDateDesc(Long playerId, Long wageringMoney);
}
