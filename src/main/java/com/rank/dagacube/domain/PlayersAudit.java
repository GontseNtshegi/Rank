package com.rank.dagacube.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A PlayersAudit.
 */
@Entity
@Table(name = "players_audit")
public class PlayersAudit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String transactionId;

    /**
     * The firstname attribute.
     */
    @ApiModelProperty(value = "The firstname attribute.")
    @Column(name = "event_date")
    private ZonedDateTime eventDate;

    @Column(name = "operation")
    private String operation;

    @Column(name = "winning_money")
    private Long winningMoney;

    @Column(name = "wagering_money")
    private Long wageringMoney;

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "promotion")
    private String promotion;

    /*@ManyToOne
    @JsonIgnoreProperties(value = { "playerIds" }, allowSetters = true)
    private Players players;*/

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PlayersAudit transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public ZonedDateTime getEventDate() {
        return this.eventDate;
    }

    public PlayersAudit eventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public void setEventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getOperation() {
        return this.operation;
    }

    public PlayersAudit operation(String operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getWinningMoney() {
        return this.winningMoney;
    }

    public PlayersAudit winningMoney(Long winningMoney) {
        this.winningMoney = winningMoney;
        return this;
    }

    public void setWinningMoney(Long winningMoney) {
        this.winningMoney = winningMoney;
    }

    public Long getWageringMoney() {
        return this.wageringMoney;
    }

    public PlayersAudit wageringMoney(Long wageringMoney) {
        this.wageringMoney = wageringMoney;
        return this;
    }

    public void setWageringMoney(Long wageringMoney) {
        this.wageringMoney = wageringMoney;
    }

    public Long getPlayerId() {
        return this.playerId;
    }

    public PlayersAudit playerId(Long playerId) {
        this.playerId = playerId;
        return this;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPromotion() {
        return this.promotion;
    }

    public PlayersAudit promotion(String promotion) {
        this.promotion = promotion;
        return this;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    /* public Players getPlayers() {
        return this.players;
    }

    public PlayersAudit players(Players players) {
        this.setPlayers(players);
        return this;
    }

    public void setPlayers(Players players) {
        this.players = players;
    }*/

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayersAudit)) {
            return false;
        }
        return transactionId != null && transactionId.equals(((PlayersAudit) o).transactionId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlayersAudit{" +
            "transactionId=" + getTransactionId() +
            ", eventDate='" + getEventDate() + "'" +
            ", operation='" + getOperation() + "'" +
            ", winningMoney=" + getWinningMoney() +
            ", wageringMoney=" + getWageringMoney() +
            ", playerId=" + getPlayerId() +
            ", promotion='" + getPromotion() + "'" +
            "}";
    }
}
