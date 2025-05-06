package com.practice.spring.controller;

import com.practice.spring.entity.User;
import com.practice.spring.entity.UserResponse;
import com.practice.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getUsers(@RequestParam int minId,
                                               @RequestParam int maxId) {
        return ResponseEntity.ok(userRepository.findAll()
                .stream()
                .filter(user -> user.getId() >= minId)
                .filter(user -> user.getId() <= maxId)
                .collect(Collectors.toList()));
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid User user) {
        logger.info("Creating User");

        if(userRepository.findById(user.getId()).isPresent())
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserResponse("User with id " + user.getId() + " already exists!"));
        }

        // Encode password before saving to DB
        user.setPassword(encoder.encode(user.getPassword()));
        return Optional.of(userRepository.save(user))
                .map(saved ->  {
                    logger.info("Created User");
                    return ResponseEntity
                            .status(HttpStatus.CREATED)
                            .body(new UserResponse("User with id " + saved.getId() + " created successfully at " + LocalDateTime.now()));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new UserResponse("Unable to create user!")));
    }
}

