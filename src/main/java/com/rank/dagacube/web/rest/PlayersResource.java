package com.rank.dagacube.web.rest;

import com.rank.dagacube.domain.Players;
import com.rank.dagacube.repository.PlayersRepository;
import com.rank.dagacube.service.PlayersService;
import com.rank.dagacube.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.rank.dagacube.domain.Players}.
 */
@RestController
@RequestMapping("/api")
public class PlayersResource {

    private final Logger log = LoggerFactory.getLogger(PlayersResource.class);

    private static final String ENTITY_NAME = "players";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayersService playersService;

    private final PlayersRepository playersRepository;

    public PlayersResource(PlayersService playersService, PlayersRepository playersRepository) {
        this.playersService = playersService;
        this.playersRepository = playersRepository;
    }

    /**
     * {@code POST  /players} : Create a new players.
     *
     * @param players the players to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new players, or with status {@code 400 (Bad Request)} if the players has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/players")
    public ResponseEntity<Players> createPlayers(@RequestBody Players players) throws URISyntaxException {
        log.debug("REST request to save Players : {}", players);
        if (players.getId() != null) {
            throw new BadRequestAlertException("A new players cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Players result = playersService.save(players);
        return ResponseEntity
            .created(new URI("/api/players/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /players/:id} : Updates an existing players.
     *
     * @param id the id of the players to save.
     * @param players the players to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated players,
     * or with status {@code 400 (Bad Request)} if the players is not valid,
     * or with status {@code 500 (Internal Server Error)} if the players couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/players/{id}")
    public ResponseEntity<Players> updatePlayers(@PathVariable(value = "id", required = false) final Long id, @RequestBody Players players)
        throws URISyntaxException {
        log.debug("REST request to update Players : {}, {}", id, players);
        if (players.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, players.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Players result = playersService.save(players);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, players.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /players/:id} : Partial updates given fields of an existing players, field will ignore if it is null
     *
     * @param id the id of the players to save.
     * @param players the players to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated players,
     * or with status {@code 400 (Bad Request)} if the players is not valid,
     * or with status {@code 404 (Not Found)} if the players is not found,
     * or with status {@code 500 (Internal Server Error)} if the players couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/players/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Players> partialUpdatePlayers(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Players players
    ) throws URISyntaxException {
        log.debug("REST request to partial update Players partially : {}, {}", id, players);
        if (players.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, players.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Players> result = playersService.partialUpdate(players);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, players.getId().toString())
        );
    }

    /**
     * {@code GET  /players} : get all the players.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of players in body.
     */
    @GetMapping("/players")
    public List<Players> getAllPlayers() {
        log.debug("REST request to get all Players");
        return playersService.findAll();
    }

    /**
     * {@code GET  /players/:id} : get the "id" players.
     *
     * @param id the id of the players to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the players, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/players/{id}")
    public ResponseEntity<Players> getPlayers(@PathVariable Long id) {
        log.debug("REST request to get Players : {}", id);
        Optional<Players> players = playersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(players);
    }

    /**
     * {@code DELETE  /players/:id} : delete the "id" players.
     *
     * @param id the id of the players to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/players/{id}")
    public ResponseEntity<Void> deletePlayers(@PathVariable Long id) {
        log.debug("REST request to delete Players : {}", id);
        playersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
