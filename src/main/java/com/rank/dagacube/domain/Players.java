package com.rank.dagacube.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Players.
 */
@Entity
@Table(name = "players")
public class Players implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The firstname attribute.
     */
    @ApiModelProperty(value = "The firstname attribute.")
    @Column(name = "current_balance")
    private Long currentBalance;

    @Column(name = "wagering_money")
    private Long wageringMoney;

    @Column(name = "winning_money")
    private Long winningMoney;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "promo_left")
    private Long promoLeft;

    /*@OneToMany(mappedBy = "players")
    @JsonIgnoreProperties(value = { "players" }, allowSetters = true)
    private Set<PlayersAudit> playerIds = new HashSet<>();*/

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Players id(Long id) {
        this.id = id;
        return this;
    }

    public Long getCurrentBalance() {
        return this.currentBalance;
    }

    public Players currentBalance(Long currentBalance) {
        this.currentBalance = currentBalance;
        return this;
    }

    public void setCurrentBalance(Long currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Long getWageringMoney() {
        return this.wageringMoney;
    }

    public Players wageringMoney(Long wageringMoney) {
        this.wageringMoney = wageringMoney;
        return this;
    }

    public void setWageringMoney(Long wageringMoney) {
        this.wageringMoney = wageringMoney;
    }

    public Long getWinningMoney() {
        return this.winningMoney;
    }

    public Players winningMoney(Long winningMoney) {
        this.winningMoney = winningMoney;
        return this;
    }

    public void setWinningMoney(Long winningMoney) {
        this.winningMoney = winningMoney;
    }

    public String getUsername() {
        return this.username;
    }

    public Players username(String username) {
        this.username = username;
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public Players email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return this.contact;
    }

    public Players contact(String contact) {
        this.contact = contact;
        return this;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    /*public Set<PlayersAudit> getPlayerIds() {
        return this.playerIds;
    }

     public Players playerIds(Set<PlayersAudit> playersAudits) {
        this.setPlayerIds(playersAudits);
        return this;
    }

   public Players addPlayerId(PlayersAudit playersAudit) {
        this.playerIds.add(playersAudit);
        playersAudit.setPlayers(this);
        return this;
    }

    public Players removePlayerId(PlayersAudit playersAudit) {
        this.playerIds.remove(playersAudit);
        playersAudit.setPlayers(null);
        return this;
    }

    public void setPlayerIds(Set<PlayersAudit> playersAudits) {
        if (this.playerIds != null) {
            this.playerIds.forEach(i -> i.setPlayers(null));
        }
        if (playersAudits != null) {
            playersAudits.forEach(i -> i.setPlayers(this));
        }
        this.playerIds = playersAudits;
    }*/

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    public Long getPromoLeft() {
        return promoLeft;
    }

    public void setPromoLeft(Long promoLeft) {
        this.promoLeft = promoLeft;
    }

    public Players promoLeft(Long promoLeft) {
        this.promoLeft = promoLeft;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Players)) {
            return false;
        }
        return id != null && id.equals(((Players) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Players{" +
            "id=" + getId() +
            ", currentBalance=" + getCurrentBalance() +
            ", wageringMoney=" + getWageringMoney() +
            ", winningMoney=" + getWinningMoney() +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", contact='" + getContact() + "'" +
            ", promoLeft='" + getPromoLeft() + "'" +
            "}";
    }
}
