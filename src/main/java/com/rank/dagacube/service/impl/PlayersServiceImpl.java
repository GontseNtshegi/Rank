package com.rank.dagacube.service.impl;

import com.rank.dagacube.domain.Players;
import com.rank.dagacube.repository.PlayersRepository;
import com.rank.dagacube.service.PlayersService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Players}.
 */
@Service
@Transactional
public class PlayersServiceImpl implements PlayersService {

    private final Logger log = LoggerFactory.getLogger(PlayersServiceImpl.class);

    private final PlayersRepository playersRepository;

    public PlayersServiceImpl(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    @Override
    public Players save(Players players) {
        log.debug("Request to save Players : {}", players);
        return playersRepository.save(players);
    }

    @Override
    public Optional<Players> partialUpdate(Players players) {
        log.debug("Request to partially update Players : {}", players);

        return playersRepository
            .findById(players.getId())
            .map(
                existingPlayers -> {
                    if (players.getCurrentBalance() != null) {
                        existingPlayers.setCurrentBalance(players.getCurrentBalance());
                    }
                    if (players.getWageringMoney() != null) {
                        existingPlayers.setWageringMoney(players.getWageringMoney());
                    }
                    if (players.getWinningMoney() != null) {
                        existingPlayers.setWinningMoney(players.getWinningMoney());
                    }
                    if (players.getUsername() != null) {
                        existingPlayers.setUsername(players.getUsername());
                    }
                    if (players.getEmail() != null) {
                        existingPlayers.setEmail(players.getEmail());
                    }
                    if (players.getContact() != null) {
                        existingPlayers.setContact(players.getContact());
                    }

                    return existingPlayers;
                }
            )
            .map(playersRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Players> findAll() {
        log.debug("Request to get all Players");
        return playersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Players> findOne(Long id) {
        log.debug("Request to get Players : {}", id);
        return playersRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Players : {}", id);
        playersRepository.deleteById(id);
    }
}
