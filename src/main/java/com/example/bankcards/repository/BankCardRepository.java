package com.example.bankcards.repository;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankCardRepository  extends JpaRepository<BankCard, Long> {
    BankCard getBankCardsByBankCardNumber(String bankCardNumber);

    Optional<BankCard> findBankCardById(Long id);

    Optional<BankCard> findBankCardByBankCardNumber(String bankCardNumber);

    List<BankCard> findByOwner(User owner);

    boolean existsByBankCardNumberAndOwner_CardHolderName(String bankCardNumber, String userName);
}
