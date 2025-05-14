package com.example.bankcards.controller;

import com.example.bankcards.dto.*;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured("ROLE_ADMIN")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
        return userService.createNewUser(registrationUserDto);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllBankCard() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDto> deleteBankCard(@PathVariable Long id) {
        return userService.deleteUserById(id);

    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto) {
        UserDto updatedUser = userService.updateUser(id, updateUserDto).getBody();
        return ResponseEntity.ok(updatedUser);
    }
}
