package com.rank.dagacube.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.rank.dagacube.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayersAuditTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayersAudit.class);
        PlayersAudit playersAudit1 = new PlayersAudit();
        playersAudit1.setTransactionId("id1");
        PlayersAudit playersAudit2 = new PlayersAudit();
        playersAudit2.setTransactionId(playersAudit1.getTransactionId());
        assertThat(playersAudit1).isEqualTo(playersAudit2);
        playersAudit2.setTransactionId("id2");
        assertThat(playersAudit1).isNotEqualTo(playersAudit2);
        playersAudit1.setTransactionId(null);
        assertThat(playersAudit1).isNotEqualTo(playersAudit2);
    }
}
