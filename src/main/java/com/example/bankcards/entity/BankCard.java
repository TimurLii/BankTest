package com.example.bankcards.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.YearMonth;
import java.util.Date;

@Entity
@Data
@Table(name = "bank_card")
public class BankCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "validity_period")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/yyyy")
    private Date validityPeriod;

    @Enumerated(EnumType.STRING)
    private StatusCard statusCard;

    private Long balance;

    public enum StatusCard {
        ACTIVE, INACTIVE, DEADLINE_EXPIRED;

    }
}
