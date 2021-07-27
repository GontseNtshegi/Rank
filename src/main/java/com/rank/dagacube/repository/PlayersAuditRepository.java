package com.rank.dagacube.repository;

import com.rank.dagacube.domain.PlayersAudit;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PlayersAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlayersAuditRepository extends JpaRepository<PlayersAudit, String> {
    List<PlayersAudit> findTop10ByPlayerIdAndWinningMoneyGreaterThanOrderByEventDateDesc(Long playerId, Long winningMoney);

    List<PlayersAudit> findTop10ByPlayerIdAndWageringMoneyGreaterThanOrderByEventDateDesc(Long playerId, Long wageringMoney);
}
