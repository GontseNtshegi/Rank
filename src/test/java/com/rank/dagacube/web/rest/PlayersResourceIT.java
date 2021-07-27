package com.rank.dagacube.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.rank.dagacube.IntegrationTest;
import com.rank.dagacube.domain.Players;
import com.rank.dagacube.repository.PlayersRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link PlayersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayersResourceIT {

    private static final Long DEFAULT_CURRENT_BALANCE = 1L;
    private static final Long UPDATED_CURRENT_BALANCE = 2L;

    private static final Long DEFAULT_WAGERING_MONEY = 1L;
    private static final Long UPDATED_WAGERING_MONEY = 2L;

    private static final Long DEFAULT_WINNING_MONEY = 1L;
    private static final Long UPDATED_WINNING_MONEY = 2L;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/players";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlayersRepository playersRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayersMockMvc;

    private Players players;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Players createEntity(EntityManager em) {
        Players players = new Players()
            .currentBalance(DEFAULT_CURRENT_BALANCE)
            .wageringMoney(DEFAULT_WAGERING_MONEY)
            .winningMoney(DEFAULT_WINNING_MONEY)
            .username(DEFAULT_USERNAME)
            .email(DEFAULT_EMAIL)
            .contact(DEFAULT_CONTACT);
        return players;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Players createUpdatedEntity(EntityManager em) {
        Players players = new Players()
            .currentBalance(UPDATED_CURRENT_BALANCE)
            .wageringMoney(UPDATED_WAGERING_MONEY)
            .winningMoney(UPDATED_WINNING_MONEY)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .contact(UPDATED_CONTACT);
        return players;
    }

    @BeforeEach
    public void initTest() {
        players = createEntity(em);
    }

    @Test
    @Transactional
    void createPlayers() throws Exception {
        int databaseSizeBeforeCreate = playersRepository.findAll().size();
        // Create the Players
        restPlayersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(players)))
            .andExpect(status().isCreated());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeCreate + 1);
        Players testPlayers = playersList.get(playersList.size() - 1);
        assertThat(testPlayers.getCurrentBalance()).isEqualTo(DEFAULT_CURRENT_BALANCE);
        assertThat(testPlayers.getWageringMoney()).isEqualTo(DEFAULT_WAGERING_MONEY);
        assertThat(testPlayers.getWinningMoney()).isEqualTo(DEFAULT_WINNING_MONEY);
        assertThat(testPlayers.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testPlayers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPlayers.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void createPlayersWithExistingId() throws Exception {
        // Create the Players with an existing ID
        players.setId(1L);

        int databaseSizeBeforeCreate = playersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayersMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(players)))
            .andExpect(status().isBadRequest());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPlayers() throws Exception {
        // Initialize the database
        playersRepository.saveAndFlush(players);

        // Get all the playersList
        restPlayersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(players.getId().intValue())))
            .andExpect(jsonPath("$.[*].currentBalance").value(hasItem(DEFAULT_CURRENT_BALANCE.intValue())))
            .andExpect(jsonPath("$.[*].wageringMoney").value(hasItem(DEFAULT_WAGERING_MONEY.intValue())))
            .andExpect(jsonPath("$.[*].winningMoney").value(hasItem(DEFAULT_WINNING_MONEY.intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)));
    }

    @Test
    @Transactional
    void getPlayers() throws Exception {
        // Initialize the database
        playersRepository.saveAndFlush(players);

        // Get the players
        restPlayersMockMvc
            .perform(get(ENTITY_API_URL_ID, players.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(players.getId().intValue()))
            .andExpect(jsonPath("$.currentBalance").value(DEFAULT_CURRENT_BALANCE.intValue()))
            .andExpect(jsonPath("$.wageringMoney").value(DEFAULT_WAGERING_MONEY.intValue()))
            .andExpect(jsonPath("$.winningMoney").value(DEFAULT_WINNING_MONEY.intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT));
    }

    @Test
    @Transactional
    void getNonExistingPlayers() throws Exception {
        // Get the players
        restPlayersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlayers() throws Exception {
        // Initialize the database
        playersRepository.saveAndFlush(players);

        int databaseSizeBeforeUpdate = playersRepository.findAll().size();

        // Update the players
        Players updatedPlayers = playersRepository.findById(players.getId()).get();
        // Disconnect from session so that the updates on updatedPlayers are not directly saved in db
        em.detach(updatedPlayers);
        updatedPlayers
            .currentBalance(UPDATED_CURRENT_BALANCE)
            .wageringMoney(UPDATED_WAGERING_MONEY)
            .winningMoney(UPDATED_WINNING_MONEY)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .contact(UPDATED_CONTACT);

        restPlayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlayers.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlayers))
            )
            .andExpect(status().isOk());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
        Players testPlayers = playersList.get(playersList.size() - 1);
        assertThat(testPlayers.getCurrentBalance()).isEqualTo(UPDATED_CURRENT_BALANCE);
        assertThat(testPlayers.getWageringMoney()).isEqualTo(UPDATED_WAGERING_MONEY);
        assertThat(testPlayers.getWinningMoney()).isEqualTo(UPDATED_WINNING_MONEY);
        assertThat(testPlayers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testPlayers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPlayers.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void putNonExistingPlayers() throws Exception {
        int databaseSizeBeforeUpdate = playersRepository.findAll().size();
        players.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, players.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(players))
            )
            .andExpect(status().isBadRequest());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlayers() throws Exception {
        int databaseSizeBeforeUpdate = playersRepository.findAll().size();
        players.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(players))
            )
            .andExpect(status().isBadRequest());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlayers() throws Exception {
        int databaseSizeBeforeUpdate = playersRepository.findAll().size();
        players.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayersMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(players)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlayersWithPatch() throws Exception {
        // Initialize the database
        playersRepository.saveAndFlush(players);

        int databaseSizeBeforeUpdate = playersRepository.findAll().size();

        // Update the players using partial update
        Players partialUpdatedPlayers = new Players();
        partialUpdatedPlayers.setId(players.getId());

        partialUpdatedPlayers.wageringMoney(UPDATED_WAGERING_MONEY).email(UPDATED_EMAIL);

        restPlayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayers))
            )
            .andExpect(status().isOk());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
        Players testPlayers = playersList.get(playersList.size() - 1);
        assertThat(testPlayers.getCurrentBalance()).isEqualTo(DEFAULT_CURRENT_BALANCE);
        assertThat(testPlayers.getWageringMoney()).isEqualTo(UPDATED_WAGERING_MONEY);
        assertThat(testPlayers.getWinningMoney()).isEqualTo(DEFAULT_WINNING_MONEY);
        assertThat(testPlayers.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testPlayers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPlayers.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    void fullUpdatePlayersWithPatch() throws Exception {
        // Initialize the database
        playersRepository.saveAndFlush(players);

        int databaseSizeBeforeUpdate = playersRepository.findAll().size();

        // Update the players using partial update
        Players partialUpdatedPlayers = new Players();
        partialUpdatedPlayers.setId(players.getId());

        partialUpdatedPlayers
            .currentBalance(UPDATED_CURRENT_BALANCE)
            .wageringMoney(UPDATED_WAGERING_MONEY)
            .winningMoney(UPDATED_WINNING_MONEY)
            .username(UPDATED_USERNAME)
            .email(UPDATED_EMAIL)
            .contact(UPDATED_CONTACT);

        restPlayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayers.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlayers))
            )
            .andExpect(status().isOk());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
        Players testPlayers = playersList.get(playersList.size() - 1);
        assertThat(testPlayers.getCurrentBalance()).isEqualTo(UPDATED_CURRENT_BALANCE);
        assertThat(testPlayers.getWageringMoney()).isEqualTo(UPDATED_WAGERING_MONEY);
        assertThat(testPlayers.getWinningMoney()).isEqualTo(UPDATED_WINNING_MONEY);
        assertThat(testPlayers.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testPlayers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPlayers.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    void patchNonExistingPlayers() throws Exception {
        int databaseSizeBeforeUpdate = playersRepository.findAll().size();
        players.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, players.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(players))
            )
            .andExpect(status().isBadRequest());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlayers() throws Exception {
        int databaseSizeBeforeUpdate = playersRepository.findAll().size();
        players.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(players))
            )
            .andExpect(status().isBadRequest());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlayers() throws Exception {
        int databaseSizeBeforeUpdate = playersRepository.findAll().size();
        players.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayersMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(players)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Players in the database
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlayers() throws Exception {
        // Initialize the database
        playersRepository.saveAndFlush(players);

        int databaseSizeBeforeDelete = playersRepository.findAll().size();

        // Delete the players
        restPlayersMockMvc
            .perform(delete(ENTITY_API_URL_ID, players.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Players> playersList = playersRepository.findAll();
        assertThat(playersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
