package com.example.bankcards.service;

import com.example.bankcards.dto.BankCardDto;
import com.example.bankcards.dto.BankCardUpdateDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.BankCardNotFoundException;
import com.example.bankcards.exception.EmailNotFoundException;
import com.example.bankcards.repository.BankCardRepository;
import com.example.bankcards.util.CreateBankCardNumber;
import com.example.bankcards.util.MaskCardNumber;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BankCardService - service for delete ,create, update ,get bankCard
 */
@Service
public class BankCardService {

    private final BankCardRepository bankCardRepository;
    private final UserService userService;
    private final CreateBankCardNumber createBankCardNumber;

    public BankCardService(BankCardRepository bankCardRepository, UserService userService, CreateBankCardNumber createBankCardNumber) {
        this.bankCardRepository = bankCardRepository;
        this.userService = userService;
        this.createBankCardNumber = createBankCardNumber;
    }

    /**
     * Initial new BankCard and save bankCard
     * @param bankCardDto dto for new BankCard
     * @return ResponseEntity<BankCardDto>
     */
    public ResponseEntity<BankCardDto> save(BankCardDto bankCardDto) {

        User user = userService.loadUserByEmail(bankCardDto.userDto().email())
                .orElseThrow(() -> new EmailNotFoundException("Email not found"));

        BankCard bankCard = new BankCard();
        bankCard.setOwner(user);
        bankCard.setStatusCard(bankCardDto.statusCard());
        bankCard.setBalance(bankCardDto.balance());
        bankCard.setValidityPeriod(bankCardDto.validPeriod());
        bankCard.setBankCardNumber(getBankCardNumber());

        if (user.getBankCards() == null) {
            user.setBankCards(new HashSet<>());
        }

        user.getBankCards().add(bankCard);

        BankCardDto newBankCardDto = new BankCardDto(
                new UserDto(bankCard.getOwner().getCardHolderName(), bankCard.getOwner().getEmail()),
                bankCard.getValidityPeriod(),
                bankCard.getBalance(),
                bankCard.getBankCardNumber(),
                bankCard.getStatusCard()
        );

        userService.save(user);

        return ResponseEntity.ok(newBankCardDto);
    }

    /**
     * Creating a unique BankCardNumber
     * @return String new unique BankCardNumber
     */

    private String getBankCardNumber() {
        String bankCardNumber;
        do {
            bankCardNumber = createBankCardNumber.getBankCardNumber();
        } while (bankCardRepository.getBankCardsByBankCardNumber(bankCardNumber) != null);
        return bankCardNumber;
    }

    /**
     *  Method for user only for users with the ADMIN role
     * @param pageable interface for pagination output
     * @return Page<BankCardDto>
     */
    public Page<BankCardDto> getAllBankCard(Pageable pageable) {
        return bankCardRepository.findAll(pageable)
                .map(bankCard -> {
                    UserDto userDto = new UserDto(
                            bankCard.getOwner().getCardHolderName(),
                            bankCard.getOwner().getEmail()
                    );
                    return new BankCardDto(
                            userDto,
                            bankCard.getValidityPeriod(),
                            bankCard.getBalance(),
                            MaskCardNumber.getMaskCardNumber(bankCard.getBankCardNumber()),
                            bankCard.getStatusCard()
                    );
                });
    }

    /**
     * Method for user only for users with the USER role
     * @param  username username authorized  user
     * @param pageable interface for pagination output
     * @return Page<BankCardDto>
     */
    public Page<BankCardDto> getCardsForUser(String username, Pageable pageable) {
        User user = userService.findByCardHolderName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Page<BankCard> cardsPage = bankCardRepository.findByOwner(user, pageable);

        return cardsPage.map(el -> new BankCardDto(
                new UserDto(el.getOwner().getCardHolderName(), el.getOwner().getEmail()),
                el.getValidityPeriod(),
                el.getBalance(),
                el.getBankCardNumber(),
                el.getStatusCard()
        ));
    }

    public ResponseEntity<List<BankCardDto>> getCardsForUser(String username) {
        User user = userService.findByCardHolderName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<BankCard> cards = bankCardRepository.findByOwner(user);

        List<BankCardDto> listBankCardDto = cards.stream().map(el -> new BankCardDto(
                new UserDto(el.getOwner().getCardHolderName(), el.getOwner().getEmail()),
                el.getValidityPeriod(),
                el.getBalance(),
                el.getBankCardNumber(),
                el.getStatusCard()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(listBankCardDto);
    }

    /**
     * Delete Bankcard
     * @param id  id bankCard for delete
     * @return ResponseEntity<BankCardDto>
     */
    public ResponseEntity<BankCardDto> deleteBankCardById(Long id) {

        Optional<BankCard> bankCardOptional = bankCardRepository.findById(id);

        if (bankCardOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        bankCardRepository.deleteById(id);

        BankCard bankCard = bankCardOptional.get();
        UserDto userDto = new UserDto(bankCard.getOwner().getCardHolderName(), bankCard.getOwner().getEmail());
        BankCardDto bankCardDto = new BankCardDto(
                userDto,
                bankCard.getValidityPeriod(),
                bankCard.getBalance(),
                bankCard.getBankCardNumber(),
                bankCard.getStatusCard());

        return ResponseEntity.ok(bankCardDto);
    }

    /**
     * Update bankCard
     * @param id id bankCard for update
     * @param bankCardUpdateDto  dto with modified parameters
     * @return ResponseEntity<BankCardDto>
     */
    public ResponseEntity<BankCardDto> updateBankCard(Long id, BankCardUpdateDto bankCardUpdateDto) {
        BankCard bankCard = bankCardRepository.findById(id)
                .orElseThrow(() -> new BankCardNotFoundException("BankCard not found"));

        if (bankCardUpdateDto.validPeriod() != null) {
            bankCard.setValidityPeriod(bankCardUpdateDto.validPeriod());
        }
        if (bankCardUpdateDto.balance() != null) {
            bankCard.setBalance(bankCardUpdateDto.balance());
        }
        if (bankCardUpdateDto.statusCard() != null) {
            bankCard.setStatusCard(bankCardUpdateDto.statusCard());
        }
        if (bankCardUpdateDto.userDto() != null && bankCardUpdateDto.userDto().cardHolderName() != null) {
            User user = userService.findByCardHolderName(bankCardUpdateDto.userDto().cardHolderName())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            bankCard.setOwner(user);
        }

        bankCardRepository.save(bankCard);

        UserDto userDto = new UserDto(bankCard.getOwner().getCardHolderName(), bankCard.getOwner().getEmail());
        BankCardDto updatedDto = new BankCardDto(
                userDto,
                bankCard.getValidityPeriod(),
                bankCard.getBalance(),
                bankCard.getBankCardNumber(),

                bankCard.getStatusCard());

        return ResponseEntity.ok(updatedDto);
    }

    /**
     * Save Set<bankCard>
     * @param updatedBankCards set new BankCard
     */
    public void saveAll(Set<BankCard> updatedBankCards) {
        bankCardRepository.saveAll(updatedBankCards);
    }

    /**
     * find bankCard by BankCardNumber
     * @param bankCarNumber
     * @return Optional<BankCard>
     */
    public Optional<BankCard> findByBankCardNumber(String bankCarNumber) {
        return bankCardRepository.findBankCardByBankCardNumber(bankCarNumber);
    }

    /**
     * assigning a new owner lists bankCard
     * @param bankCardDtoList list to which the owner needs to be added
     * @param user new owner
     * @return Set<BankCard>
     */
    @Transactional
    public Set<BankCard> updateBankCardsFromDtoList(List<BankCardDto> bankCardDtoList, User user) {
        Set<BankCard> updatedBankCards = new HashSet<>();
        for (BankCardDto bankCardDto : bankCardDtoList) {
            BankCard bankCard = findByBankCardNumber(bankCardDto.bankCardNumber())
                    .orElseThrow(() -> new BankCardNotFoundException("BankCard not found: " + bankCardDto.bankCardNumber()));


            if (bankCardDto.validPeriod() != null) {
                bankCard.setValidityPeriod(bankCardDto.validPeriod());
            }
            if (bankCardDto.balance() != null) {
                bankCard.setBalance(bankCardDto.balance());
            }
            if (bankCardDto.statusCard() != null) {
                bankCard.setStatusCard(bankCardDto.statusCard());
            }

            bankCard.setOwner(user);

            updatedBankCards.add(bankCard);
        }

        saveAll(updatedBankCards);

        return updatedBankCards;
    }
}

