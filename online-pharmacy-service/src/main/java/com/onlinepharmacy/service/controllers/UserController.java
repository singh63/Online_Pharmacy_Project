package com.onlinepharmacy.service.controllers;

import com.onlinepharmacy.service.config.AppConstants;
import com.onlinepharmacy.service.payloads.UserDTO;
import com.onlinepharmacy.service.payloads.UserResponse;
import com.onlinepharmacy.service.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserController {

    private final UserService userService;

    @GetMapping("/admin/users")
    public ResponseEntity<UserResponse> getUsers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        UserResponse userResponse = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO user = userService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/admin/users/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        String status = userService.deleteUser(userId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }
}
