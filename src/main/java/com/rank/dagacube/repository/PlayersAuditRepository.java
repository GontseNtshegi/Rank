package com.rank.dagacube.repository;

import com.rank.dagacube.domain.PlayersAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlayersAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayersAuditRepository extends JpaRepository<PlayersAudit, String> {}
