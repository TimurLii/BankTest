package com.example.bankcards.service;

import com.example.bankcards.dto.RegistrationUserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.impl.UserDetailsImpl;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByCardHolderName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return UserDetailsImpl.build(user);
    }

    public Optional<User> findByCardHolderName(String cardHolderName) {
        return userRepository.findByCardHolderName(cardHolderName);
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User user = new User();

        user.setCardHolderName(registrationUserDto.cardHolderName());
        user.setPassword(passwordEncoder.encode(registrationUserDto.password()));
        user.setRoles(List.of(roleService.getUserRole()));
        return userRepository.save(user);

    }
}
