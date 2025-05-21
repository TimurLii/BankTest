package com.example.bankcards.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "bank_card")
@ToString(exclude = "owner")

public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "validity_period")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yyyy")
    private Date validityPeriod;

    @Column(name = "bank_Card_Number")
    private String bankCardNumber;

    @Enumerated(EnumType.STRING)
    private StatusCard statusCard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Date validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public StatusCard getStatusCard() {
        return statusCard;
    }

    public void setStatusCard(StatusCard statusCard) {
        this.statusCard = statusCard;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    private Long balance;

    public enum StatusCard {
        ACTIVE, INACTIVE, DEADLINE_EXPIRED;

    }

}
