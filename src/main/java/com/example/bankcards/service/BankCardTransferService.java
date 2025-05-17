package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankCardUpdateDto;
import com.example.bankcards.dto.BankTransferDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.AuthorizationException;
import com.example.bankcards.exception.UserHasNoCardException;
import com.example.bankcards.exception.UserNoHasMoney;
import com.example.bankcards.impl.UserDetailsImpl;
import com.example.bankcards.repository.BankCardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankCardTransferService {
    private final BankCardRepository bankCardRepository;
    private final BankCardService bankCardService;
    private final UserService userService;


    public BankCardTransferService(BankCardRepository bankCardRepository, BankCardService bankCardService, UserService userService) {
        this.bankCardRepository = bankCardRepository;
        this.bankCardService = bankCardService;

        this.userService = userService;
    }

    public ResponseEntity<List<BankCardUpdateDto>> bankCardTransfer(BankTransferDto bankTransferDto, UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new AuthorizationException("Пользователь не авторизован или токен невалиден");
        }
        User user = userService.findByCardHolderName(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        List<BankCardDto> cardsForUser = bankCardService.getCardsForUser(userDetails.getUsername()).getBody();

        if (cardsForUser == null || cardsForUser.isEmpty()) {
            throw new UserHasNoCardException("У пользователя нет банковских карт");
        }

        if (haveBankCard(bankTransferDto, cardsForUser)
                && neededSumInSendersBankCard(bankTransferDto.sendersCard(), bankTransferDto.summa())
                && statusBankCardActivity(bankTransferDto)) {
            List<BankCardUpdateDto> bankCardUpdateDtos = updateBankCardAfterTransfer(bankTransferDto, bankTransferDto.summa());

            return ResponseEntity.ok().body(bankCardUpdateDtos);
        }

        return ResponseEntity.badRequest().build();

    }

    private boolean statusBankCardActivity(BankTransferDto bankTransferDto) {
        BankCard sendersBankCard = bankCardRepository.findBankCardByBankCardNumber(bankTransferDto.sendersCard()).get();
        BankCard recipientsBankCard = bankCardRepository.findBankCardByBankCardNumber(bankTransferDto.recipientsCard()).get();
        return sendersBankCard.getStatusCard().equals(BankCard.StatusCard.ACTIVE)
                &&
                recipientsBankCard.getStatusCard().equals(BankCard.StatusCard.ACTIVE);
    }

    private List<BankCardUpdateDto> updateBankCardAfterTransfer(BankTransferDto bankTransferDto, Long sum) {

        BankCard sendersBankCard = bankCardRepository.findBankCardByBankCardNumber(bankTransferDto.sendersCard()).get();
        BankCard recipientsBankCard = bankCardRepository.findBankCardByBankCardNumber(bankTransferDto.recipientsCard()).get();

        BankCardUpdateDto sendersBankCardUpdate = new BankCardUpdateDto
                (new UserDto(sendersBankCard.getOwner().cardHolderName, sendersBankCard.getOwner().getEmail()),
                        sendersBankCard.getValidityPeriod(), sendersBankCard.getBalance() - sum, sendersBankCard.getStatusCard());

        BankCardUpdateDto recipientsBankCardUpdate = new BankCardUpdateDto
                (new UserDto(recipientsBankCard.getOwner().cardHolderName, recipientsBankCard.getOwner().getEmail()),
                        recipientsBankCard.getValidityPeriod(), recipientsBankCard.getBalance() + sum, recipientsBankCard.getStatusCard());

        bankCardService.updateBankCard(sendersBankCard.getId(), sendersBankCardUpdate);
        bankCardService.updateBankCard(recipientsBankCard.getId(), recipientsBankCardUpdate);
        return List.of(sendersBankCardUpdate, recipientsBankCardUpdate);
    }

    private boolean haveBankCard(BankTransferDto bankTransferDto, List<BankCardDto> listBankCardDto) {
        List<String> listBankCardNumber = listBankCardDto.stream().map(el -> el.bankCardNumber()).toList();
        if (listBankCardNumber.contains(bankTransferDto.sendersCard()) && listBankCardNumber.contains(bankTransferDto.recipientsCard())) {
            return true;
        }

        throw new UserHasNoCardException("Данная карта не является картой пользователя");
    }

    private boolean neededSumInSendersBankCard(String numberSendersBankCard, long transferAmount) {
        Long balance = bankCardRepository
                .findBankCardByBankCardNumber(numberSendersBankCard).get().getBalance();
        if (balance >= transferAmount) {
            return true;
        }
        throw new UserNoHasMoney(" Нехватает денежных средств");
    }

}
