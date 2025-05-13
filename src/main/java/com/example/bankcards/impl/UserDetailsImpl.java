package com.example.bankcards.impl;

import com.example.bankcards.entity.BankCard;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String cardHolderName;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private List<BankCard> bankCards;


    private UserDetailsImpl(Long id, String cardHolderName, String password, Collection<? extends GrantedAuthority> authorities, List<BankCard> bankCards) {
        this.id = id;
        this.cardHolderName = cardHolderName;
        this.password = password;
        this.authorities = authorities;
        this.bankCards = bankCards;
    }

    public static UserDetailsImpl build(User user) {
        var authorities = user.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        var bankCardsList = new ArrayList<>(user.getBankCards());


        return new UserDetailsImpl(
                user.getId(),
                user.getCardHolderName(),
                user.getPassword(),
                authorities,
                bankCardsList
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return cardHolderName;
    }

    // Для простоты считаем, что аккаунт всегда активен и не заблокирован
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
