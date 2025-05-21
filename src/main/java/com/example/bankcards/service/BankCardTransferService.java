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
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.repository.BankCardRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for transfer
 */
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

    /**
     *
     * @param bankTransferDto dto for transfer
     * @param userDetails  iwner bankCard
     * @return List<BankCardUpdateDto>
     */
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

    /**
     * Checking the status BankCard
     * @param bankTransferDto dto for transfer
     * @return true if status ACTIVITY or FALSE if not ACTIVITY
     */
    private boolean statusBankCardActivity(BankTransferDto bankTransferDto) {
        BankCard sendersBankCard = bankCardRepository.findBankCardByBankCardNumber(bankTransferDto.sendersCard()).get();
        BankCard recipientsBankCard = bankCardRepository.findBankCardByBankCardNumber(bankTransferDto.recipientsCard()).get();
        return sendersBankCard.getStatusCard().equals(BankCard.StatusCard.ACTIVE)
                &&
                recipientsBankCard.getStatusCard().equals(BankCard.StatusCard.ACTIVE);
    }

    /**
     *
     * @param bankTransferDto dto for transfer
     * @param listBankCardDto list<BankCardDto> which the user has
     * @return TRUE if user have bankCard  of FALSE if note have bankCard
     */
    private boolean haveBankCard(BankTransferDto bankTransferDto, List<BankCardDto> listBankCardDto) {
        List<String> listBankCardNumber = listBankCardDto.stream().map(el -> el.bankCardNumber()).toList();
        if (listBankCardNumber.contains(bankTransferDto.sendersCard()) && listBankCardNumber.contains(bankTransferDto.recipientsCard())) {
            return true;
        }

        throw new UserHasNoCardException("Данная карта не является картой пользователя");
    }

    /**
     *
     * @param numberSendersBankCard
     * @param transferAmount
     * @return TRUE if balance BankCard > transferAmount or FALSE if balance BankCard < transferAmount
     */
    private boolean neededSumInSendersBankCard(String numberSendersBankCard, long transferAmount) {
        Long balance = bankCardRepository
                .findBankCardByBankCardNumber(numberSendersBankCard).get().getBalance();
        if (balance >= transferAmount) {
            return true;
        }
        throw new UserNoHasMoney(" Нехватает денежных средств");
    }
    /**
     *
     * @param bankTransferDto dto fro transfer
     * @param sum sum transfer
     * @return List<BankCardDto> after update
     */
    private List<BankCardUpdateDto> updateBankCardAfterTransfer(BankTransferDto bankTransferDto, Long sum) {

        BankCard sendersBankCard = bankCardRepository.findBankCardByBankCardNumber(bankTransferDto.sendersCard()).get();
        BankCard recipientsBankCard = bankCardRepository.findBankCardByBankCardNumber(bankTransferDto.recipientsCard()).get();

        BankCardUpdateDto sendersBankCardUpdate = new BankCardUpdateDto
                (new UserDto(sendersBankCard.getOwner().getCardHolderName(), sendersBankCard.getOwner().getEmail()),
                        sendersBankCard.getValidityPeriod(), sendersBankCard.getBalance() - sum, sendersBankCard.getStatusCard());

        BankCardUpdateDto recipientsBankCardUpdate = new BankCardUpdateDto
                (new UserDto(recipientsBankCard.getOwner().getCardHolderName(), recipientsBankCard.getOwner().getEmail()),
                        recipientsBankCard.getValidityPeriod(), recipientsBankCard.getBalance() + sum, recipientsBankCard.getStatusCard());

        bankCardService.updateBankCard(sendersBankCard.getId(), sendersBankCardUpdate);
        bankCardService.updateBankCard(recipientsBankCard.getId(), recipientsBankCardUpdate);
        return List.of(sendersBankCardUpdate, recipientsBankCardUpdate);
    }


}
