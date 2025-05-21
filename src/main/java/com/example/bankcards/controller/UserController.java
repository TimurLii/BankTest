package com.example.bankcards.controller;

import com.example.bankcards.dto.*;
import com.example.bankcards.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController - create, delete,get, update user 
 * only for users with the ADMIN role
 */
@RestController
@Secured("ROLE_ADMIN")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 
     * @param registrationUserDto - dto for create new user 
     * @return ResponseEntity<UserDto>
     */
    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid RegistrationUserDto registrationUserDto) {
        return userService.createNewUser(registrationUserDto);
    }

    /**
     * @return ResponseEntity<List<UserDto>>
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUser() {
        return userService.getAllUsers();
    }

    /**
     *
     * @param id id users for delete
     * @return ResponseEntity<UserDto>
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long id) {
        return userService.deleteUserById(id);

    }

    /**
     *
     * @param id - id user for update
     * @param updateUserDto - dto with modified parameters
     * @return - ResponseEntity<UserDto>
     */
    @PatchMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserDto updateUserDto) {
        UserDto updatedUser = userService.updateUser(id, updateUserDto).getBody();
        return ResponseEntity.ok(updatedUser);
    }
}
