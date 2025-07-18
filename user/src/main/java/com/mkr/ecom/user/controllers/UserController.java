package com.mkr.ecom.user.controllers;

import com.mkr.ecom.user.dto.UserRequest;
import com.mkr.ecom.user.dto.UserResponse;
import com.mkr.ecom.user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    public static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getUser() {
        log.info("REQUEST RECEIVED");
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUsers(@PathVariable String id) {
        log.info("Request received for user: {}", id);
        log.trace("Request details");
        return userService.fetchUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody UserRequest user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id,
                                           @RequestBody UserRequest user) {
        boolean updated = userService.editUser(id, user);
        return updated ? ResponseEntity.ok("User updated successfully")
                : ResponseEntity.notFound().build();
    }
}
