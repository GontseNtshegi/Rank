package com.rank.dagacube.repository;

import com.rank.dagacube.domain.Players;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Players entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayersRepository extends JpaRepository<Players, Long> {}
