package com.rank.dagacube.service;

import com.rank.dagacube.domain.Players;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Players}.
 */
public interface PlayersService {
    /**
     * Save a players.
     *
     * @param players the entity to save.
     * @return the persisted entity.
     */
    Players save(Players players);

    /**
     * Partially updates a players.
     *
     * @param players the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Players> partialUpdate(Players players);

    /**
     * Get all the players.
     *
     * @return the list of entities.
     */
    List<Players> findAll();

    /**
     * Get the "id" players.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Players> findOne(Long id);

    /**
     * Delete the "id" players.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
