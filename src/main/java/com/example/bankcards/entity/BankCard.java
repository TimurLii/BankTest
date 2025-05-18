package com.example.bankcards.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Entity
@Data
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

    private Long balance;

    public enum StatusCard {
        ACTIVE, INACTIVE, DEADLINE_EXPIRED;

    }
}
