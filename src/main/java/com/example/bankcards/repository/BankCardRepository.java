package com.example.bankcards.repository;

import com.example.bankcards.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardRepository  extends JpaRepository<BankCard, Long> {
    BankCard getBankCardsByBankCardNumber(String bankCardNumber);
}
