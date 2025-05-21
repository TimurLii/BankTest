package com.example.bankcards.service;

import com.example.bankcards.dto.JwtRequest;
import com.example.bankcards.dto.JwtResponse;
import com.example.bankcards.dto.RegistrationUserDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.exception.AuthError;
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class AuthService {
    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * authentication user
     * @param jwtRequest dto for create jwt token
     * @return  JwtResponse (new token )
     */
    public ResponseEntity<?> createAuthToken(JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.cardHolderName(), jwtRequest.password()));
        } catch (
                BadCredentialsException ex) {
            return new ResponseEntity<>(new AuthError(HttpStatus.UNAUTHORIZED.value(), "Неправильный логин или пароль "), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.cardHolderName());

        String token = jwtTokenUtil.generateToken((UserDetailsImpl) userDetails);
        return ResponseEntity.ok(new JwtResponse(token));

    }

    /**
     *
     * @param registrationUserDto dto for create new user
     * @return ResponseEntity<UserDto>
     */
    public ResponseEntity<?> createNewUser(RegistrationUserDto registrationUserDto) {
        if(!registrationUserDto.password().equals(registrationUserDto.passwordConfirm())){
            return new ResponseEntity<>(new AuthError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"), HttpStatus.BAD_REQUEST);
        }
        if(userService.findByCardHolderName(registrationUserDto.cardHolderName()).isPresent()){
            return new ResponseEntity<>(new AuthError(HttpStatus.BAD_REQUEST.value(), "Такой пользователь существует"), HttpStatus.BAD_REQUEST);
        }

        ResponseEntity<UserDto> userDto = userService.createNewUser(registrationUserDto);

        return ResponseEntity.ok(userDto);

    }
}
