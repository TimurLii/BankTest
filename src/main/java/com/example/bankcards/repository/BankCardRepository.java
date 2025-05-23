package com.example.bankcards.repository;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankCardRepository  extends JpaRepository<BankCard, Long> {
    BankCard getBankCardsByBankCardNumber(String bankCardNumber);

    Optional<BankCard> findBankCardByBankCardNumber(String bankCardNumber);

    Page<BankCard> findByOwner(User owner, Pageable pageable);

    List<BankCard> findByOwner(User user);


}
