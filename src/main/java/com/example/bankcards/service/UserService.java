package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EmailAlreadyExistsException;
import com.example.bankcards.impl.UserDetailsImpl;
import com.example.bankcards.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final BankCardService bankCardService;


    public UserService(UserRepository userRepository,
                       RoleService roleService,
                       PasswordEncoder passwordEncoder,
                       @Lazy BankCardService bankCardService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.bankCardService = bankCardService;
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByCardHolderName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }

    public Optional<User> findByCardHolderName(String cardHolderName) {
        return userRepository.findByCardHolderName(cardHolderName);
    }

    public ResponseEntity<UserDto> createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();

        user.setCardHolderName(registrationUserDto.cardHolderName());
        user.setPassword(passwordEncoder.encode(registrationUserDto.password()));
        user.setRoles(List.of(roleService.getUserRole()));
        if (userRepository.existsByEmail(registrationUserDto.email())) {
            throw new EmailAlreadyExistsException("Email уже используется" + registrationUserDto.email());
        }
        user.setEmail(registrationUserDto.email());

        UserDto userDto = new UserDto(registrationUserDto.cardHolderName(), registrationUserDto.email());
        userRepository.save(user);
        return ResponseEntity.ok(userDto);

    }

    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> listUserDto = userRepository.findAll().stream()
                .map(user -> new UserDto(user.getCardHolderName(),user.getEmail())).toList();
        return ResponseEntity.ok(listUserDto);
    }

    public ResponseEntity<UserDto> deleteUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);

        User user = userOptional.get();
        UserDto userDto = new UserDto(user.getCardHolderName(), user.getEmail());

        return ResponseEntity.ok(userDto);
    }

    @Transactional
    public ResponseEntity<UserDto> updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (updateUserDto.cardHolderName() != null) {
            user.setCardHolderName(updateUserDto.cardHolderName());
        }

        if (updateUserDto.roleDtoCollection() != null) {
            Set<Role> setRole = updateUserDto.roleDtoCollection().stream()
                    .map(roleDto -> roleService.findRole(roleDto.roleName()).orElseThrow(() ->
                            new RuntimeException("Role not found: " + roleDto.roleName())))
                    .collect(Collectors.toSet());
            user.setRoles(setRole);
        }

        if (updateUserDto.bankCardDtoList() != null) {
            Set<BankCard> updatedBankCards = bankCardService.updateBankCardsFromDtoList(updateUserDto.bankCardDtoList(), user);
            user.setBankCards(updatedBankCards);
        }

        userRepository.save(user);

        UserDto userDto = new UserDto(user.getCardHolderName(),user.getEmail());

        return ResponseEntity.ok(userDto);
    }

    public Optional<User> loadUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
