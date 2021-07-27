package com.rank.dagacube.web.rest;

import com.rank.dagacube.domain.PlayersAudit;
import com.rank.dagacube.repository.PlayersAuditRepository;
import com.rank.dagacube.service.PlayersAuditService;
import com.rank.dagacube.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.rank.dagacube.domain.PlayersAudit}.
 */
@RestController
@RequestMapping("/api")
public class PlayersAuditResource {

    private final Logger log = LoggerFactory.getLogger(PlayersAuditResource.class);

    private static final String ENTITY_NAME = "playersAudit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlayersAuditService playersAuditService;

    private final PlayersAuditRepository playersAuditRepository;

    public PlayersAuditResource(PlayersAuditService playersAuditService, PlayersAuditRepository playersAuditRepository) {
        this.playersAuditService = playersAuditService;
        this.playersAuditRepository = playersAuditRepository;
    }

    /**
     * {@code POST  /players-audits} : Create a new playersAudit.
     *
     * @param playersAudit the playersAudit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new playersAudit, or with status {@code 400 (Bad Request)} if the playersAudit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/players-audits")
    public ResponseEntity<PlayersAudit> createPlayersAudit(@Valid @RequestBody PlayersAudit playersAudit) throws URISyntaxException {
        log.debug("REST request to save PlayersAudit : {}", playersAudit);
        if (playersAudit.getTransactionId() != null) {
            throw new BadRequestAlertException("A new playersAudit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlayersAudit result = playersAuditService.save(playersAudit);
        return ResponseEntity
            .created(new URI("/api/players-audits/" + result.getTransactionId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getTransactionId()))
            .body(result);
    }

    /**
     * {@code PUT  /players-audits/:transactionId} : Updates an existing playersAudit.
     *
     * @param transactionId the id of the playersAudit to save.
     * @param playersAudit the playersAudit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playersAudit,
     * or with status {@code 400 (Bad Request)} if the playersAudit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the playersAudit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/players-audits/{transactionId}")
    public ResponseEntity<PlayersAudit> updatePlayersAudit(
        @PathVariable(value = "transactionId", required = false) final String transactionId,
        @Valid @RequestBody PlayersAudit playersAudit
    ) throws URISyntaxException {
        log.debug("REST request to update PlayersAudit : {}, {}", transactionId, playersAudit);
        if (playersAudit.getTransactionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(transactionId, playersAudit.getTransactionId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playersAuditRepository.existsById(transactionId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlayersAudit result = playersAuditService.save(playersAudit);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playersAudit.getTransactionId()))
            .body(result);
    }

    /**
     * {@code PATCH  /players-audits/:transactionId} : Partial updates given fields of an existing playersAudit, field will ignore if it is null
     *
     * @param transactionId the id of the playersAudit to save.
     * @param playersAudit the playersAudit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated playersAudit,
     * or with status {@code 400 (Bad Request)} if the playersAudit is not valid,
     * or with status {@code 404 (Not Found)} if the playersAudit is not found,
     * or with status {@code 500 (Internal Server Error)} if the playersAudit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/players-audits/{transactionId}", consumes = "application/merge-patch+json")
    public ResponseEntity<PlayersAudit> partialUpdatePlayersAudit(
        @PathVariable(value = "transactionId", required = false) final String transactionId,
        @NotNull @RequestBody PlayersAudit playersAudit
    ) throws URISyntaxException {
        log.debug("REST request to partial update PlayersAudit partially : {}, {}", transactionId, playersAudit);
        if (playersAudit.getTransactionId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(transactionId, playersAudit.getTransactionId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!playersAuditRepository.existsById(transactionId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlayersAudit> result = playersAuditService.partialUpdate(playersAudit);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, playersAudit.getTransactionId())
        );
    }

    /**
     * {@code GET  /players-audits} : get all the playersAudits.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of playersAudits in body.
     */
    @GetMapping("/players-audits")
    public List<PlayersAudit> getAllPlayersAudits() {
        log.debug("REST request to get all PlayersAudits");
        return playersAuditService.findAll();
    }

    /**
     * {@code GET  /players-audits/:id} : get the "id" playersAudit.
     *
     * @param id the id of the playersAudit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the playersAudit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/players-audits/{id}")
    public ResponseEntity<PlayersAudit> getPlayersAudit(@PathVariable String id) {
        log.debug("REST request to get PlayersAudit : {}", id);
        Optional<PlayersAudit> playersAudit = playersAuditService.findOne(id);
        return ResponseUtil.wrapOrNotFound(playersAudit);
    }

    /**
     * {@code DELETE  /players-audits/:id} : delete the "id" playersAudit.
     *
     * @param id the id of the playersAudit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/players-audits/{id}")
    public ResponseEntity<Void> deletePlayersAudit(@PathVariable String id) {
        log.debug("REST request to delete PlayersAudit : {}", id);
        playersAuditService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build();
    }
}
