package com.rank.dagacube.web.rest;

import static com.rank.dagacube.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rank.dagacube.IntegrationTest;
import com.rank.dagacube.domain.PlayersAudit;
import com.rank.dagacube.repository.PlayersAuditRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PlayersAuditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayersAuditResourceIT {

    private static final ZonedDateTime DEFAULT_EVENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EVENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_OPERATION = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION = "BBBBBBBBBB";

    private static final Long DEFAULT_WINNING_MONEY = 1L;
    private static final Long UPDATED_WINNING_MONEY = 2L;

    private static final Long DEFAULT_WAGERING_MONEY = 1L;
    private static final Long UPDATED_WAGERING_MONEY = 2L;

    private static final Long DEFAULT_PLAYER_ID = 1L;
    private static final Long UPDATED_PLAYER_ID = 2L;

    private static final String DEFAULT_PROMOTION = "AAAAAAAAAA";
    private static final String UPDATED_PROMOTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/players-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{transactionId}";

    @Autowired
    private PlayersAuditRepository playersAuditRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayersAuditMockMvc;

    private PlayersAudit playersAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayersAudit createEntity(EntityManager em) {
        PlayersAudit playersAudit = new PlayersAudit()
            .eventDate(DEFAULT_EVENT_DATE)
            .operation(DEFAULT_OPERATION)
            .winningMoney(DEFAULT_WINNING_MONEY)
            .wageringMoney(DEFAULT_WAGERING_MONEY)
            .playerId(DEFAULT_PLAYER_ID)
            .promotion(DEFAULT_PROMOTION);
        return playersAudit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayersAudit createUpdatedEntity(EntityManager em) {
        PlayersAudit playersAudit = new PlayersAudit()
            .eventDate(UPDATED_EVENT_DATE)
            .operation(UPDATED_OPERATION)
            .winningMoney(UPDATED_WINNING_MONEY)
            .wageringMoney(UPDATED_WAGERING_MONEY)
            .playerId(UPDATED_PLAYER_ID)
            .promotion(UPDATED_PROMOTION);
        return playersAudit;
    }

    @BeforeEach
    public void initTest() {
        playersAudit = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayersAudit() throws Exception {
        int databaseSizeBeforeCreate = playersAuditRepository.findAll().size();
        // Create the PlayersAudit
        restPlayersAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playersAudit)))
            .andExpect(status().isCreated());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeCreate + 1);
        PlayersAudit testPlayersAudit = playersAuditList.get(playersAuditList.size() - 1);
        assertThat(testPlayersAudit.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testPlayersAudit.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testPlayersAudit.getWinningMoney()).isEqualTo(DEFAULT_WINNING_MONEY);
        assertThat(testPlayersAudit.getWageringMoney()).isEqualTo(DEFAULT_WAGERING_MONEY);
        assertThat(testPlayersAudit.getPlayerId()).isEqualTo(DEFAULT_PLAYER_ID);
        assertThat(testPlayersAudit.getPromotion()).isEqualTo(DEFAULT_PROMOTION);
    }

    @Test
    @Transactional
    void createPlayersAuditWithExistingId() throws Exception {
        // Create the PlayersAudit with an existing ID
        playersAudit.setTransactionId("existing_id");

        int databaseSizeBeforeCreate = playersAuditRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayersAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playersAudit)))
            .andExpect(status().isBadRequest());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlayersAudits() throws Exception {
        // Initialize the database
        playersAuditRepository.saveAndFlush(playersAudit);

        // Get all the playersAuditList
        restPlayersAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=transactionId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].transactionId").value(hasItem(playersAudit.getTransactionId())))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(sameInstant(DEFAULT_EVENT_DATE))))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION)))
            .andExpect(jsonPath("$.[*].winningMoney").value(hasItem(DEFAULT_WINNING_MONEY.intValue())))
            .andExpect(jsonPath("$.[*].wageringMoney").value(hasItem(DEFAULT_WAGERING_MONEY.intValue())))
            .andExpect(jsonPath("$.[*].playerId").value(hasItem(DEFAULT_PLAYER_ID.intValue())))
            .andExpect(jsonPath("$.[*].promotion").value(hasItem(DEFAULT_PROMOTION)));
    }

    @Test
    @Transactional
    void getPlayersAudit() throws Exception {
        // Initialize the database
        playersAuditRepository.saveAndFlush(playersAudit);

        // Get the playersAudit
        restPlayersAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, playersAudit.getTransactionId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.transactionId").value(playersAudit.getTransactionId()))
            .andExpect(jsonPath("$.eventDate").value(sameInstant(DEFAULT_EVENT_DATE)))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION))
            .andExpect(jsonPath("$.winningMoney").value(DEFAULT_WINNING_MONEY.intValue()))
            .andExpect(jsonPath("$.wageringMoney").value(DEFAULT_WAGERING_MONEY.intValue()))
            .andExpect(jsonPath("$.playerId").value(DEFAULT_PLAYER_ID.intValue()))
            .andExpect(jsonPath("$.promotion").value(DEFAULT_PROMOTION));
    }

    @Test
    @Transactional
    void getNonExistingPlayersAudit() throws Exception {
        // Get the playersAudit
        restPlayersAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlayersAudit() throws Exception {
        // Initialize the database
        playersAuditRepository.saveAndFlush(playersAudit);

        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();

        // Update the playersAudit
        PlayersAudit updatedPlayersAudit = playersAuditRepository.findById(playersAudit.getTransactionId()).get();
        // Disconnect from session so that the updates on updatedPlayersAudit are not directly saved in db
        em.detach(updatedPlayersAudit);
        updatedPlayersAudit
            .eventDate(UPDATED_EVENT_DATE)
            .operation(UPDATED_OPERATION)
            .winningMoney(UPDATED_WINNING_MONEY)
            .wageringMoney(UPDATED_WAGERING_MONEY)
            .playerId(UPDATED_PLAYER_ID)
            .promotion(UPDATED_PROMOTION);

        restPlayersAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlayersAudit.getTransactionId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlayersAudit))
            )
            .andExpect(status().isOk());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
        PlayersAudit testPlayersAudit = playersAuditList.get(playersAuditList.size() - 1);
        assertThat(testPlayersAudit.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testPlayersAudit.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testPlayersAudit.getWinningMoney()).isEqualTo(UPDATED_WINNING_MONEY);
        assertThat(testPlayersAudit.getWageringMoney()).isEqualTo(UPDATED_WAGERING_MONEY);
        assertThat(testPlayersAudit.getPlayerId()).isEqualTo(UPDATED_PLAYER_ID);
        assertThat(testPlayersAudit.getPromotion()).isEqualTo(UPDATED_PROMOTION);
    }

    @Test
    @Transactional
    void putNonExistingPlayersAudit() throws Exception {
        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();
        playersAudit.setTransactionId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayersAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playersAudit.getTransactionId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playersAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayersAudit() throws Exception {
        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();
        playersAudit.setTransactionId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayersAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(playersAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayersAudit() throws Exception {
        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();
        playersAudit.setTransactionId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayersAuditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(playersAudit)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayersAuditWithPatch() throws Exception {
        // Initialize the database
        playersAuditRepository.saveAndFlush(playersAudit);

        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();

        // Update the playersAudit using partial update
        PlayersAudit partialUpdatedPlayersAudit = new PlayersAudit();
        partialUpdatedPlayersAudit.setTransactionId(playersAudit.getTransactionId());

        partialUpdatedPlayersAudit.eventDate(UPDATED_EVENT_DATE).operation(UPDATED_OPERATION).winningMoney(UPDATED_WINNING_MONEY);

        restPlayersAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayersAudit.getTransactionId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayersAudit))
            )
            .andExpect(status().isOk());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
        PlayersAudit testPlayersAudit = playersAuditList.get(playersAuditList.size() - 1);
        assertThat(testPlayersAudit.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testPlayersAudit.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testPlayersAudit.getWinningMoney()).isEqualTo(UPDATED_WINNING_MONEY);
        assertThat(testPlayersAudit.getWageringMoney()).isEqualTo(DEFAULT_WAGERING_MONEY);
        assertThat(testPlayersAudit.getPlayerId()).isEqualTo(DEFAULT_PLAYER_ID);
        assertThat(testPlayersAudit.getPromotion()).isEqualTo(DEFAULT_PROMOTION);
    }

    @Test
    @Transactional
    void fullUpdatePlayersAuditWithPatch() throws Exception {
        // Initialize the database
        playersAuditRepository.saveAndFlush(playersAudit);

        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();

        // Update the playersAudit using partial update
        PlayersAudit partialUpdatedPlayersAudit = new PlayersAudit();
        partialUpdatedPlayersAudit.setTransactionId(playersAudit.getTransactionId());

        partialUpdatedPlayersAudit
            .eventDate(UPDATED_EVENT_DATE)
            .operation(UPDATED_OPERATION)
            .winningMoney(UPDATED_WINNING_MONEY)
            .wageringMoney(UPDATED_WAGERING_MONEY)
            .playerId(UPDATED_PLAYER_ID)
            .promotion(UPDATED_PROMOTION);

        restPlayersAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayersAudit.getTransactionId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayersAudit))
            )
            .andExpect(status().isOk());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
        PlayersAudit testPlayersAudit = playersAuditList.get(playersAuditList.size() - 1);
        assertThat(testPlayersAudit.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testPlayersAudit.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testPlayersAudit.getWinningMoney()).isEqualTo(UPDATED_WINNING_MONEY);
        assertThat(testPlayersAudit.getWageringMoney()).isEqualTo(UPDATED_WAGERING_MONEY);
        assertThat(testPlayersAudit.getPlayerId()).isEqualTo(UPDATED_PLAYER_ID);
        assertThat(testPlayersAudit.getPromotion()).isEqualTo(UPDATED_PROMOTION);
    }

    @Test
    @Transactional
    void patchNonExistingPlayersAudit() throws Exception {
        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();
        playersAudit.setTransactionId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayersAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playersAudit.getTransactionId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playersAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayersAudit() throws Exception {
        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();
        playersAudit.setTransactionId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayersAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(playersAudit))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayersAudit() throws Exception {
        int databaseSizeBeforeUpdate = playersAuditRepository.findAll().size();
        playersAudit.setTransactionId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayersAuditMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(playersAudit))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayersAudit in the database
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayersAudit() throws Exception {
        // Initialize the database
        playersAuditRepository.saveAndFlush(playersAudit);

        int databaseSizeBeforeDelete = playersAuditRepository.findAll().size();

        // Delete the playersAudit
        restPlayersAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, playersAudit.getTransactionId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlayersAudit> playersAuditList = playersAuditRepository.findAll();
        assertThat(playersAuditList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
