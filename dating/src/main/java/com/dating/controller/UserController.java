package com.dating.controller;

import com.dating.domain.User;
import com.dating.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable UUID userId){
        User user = userService.getPersonById(userId).orElse(User.builder().build());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/submitNewUser")
    public ResponseEntity saveNewUser(@RequestBody User user){
        User newUser = userService.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.add("contentLocation", "/api/v1/user/"+newUser.getRegId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity updatePerson(@PathVariable UUID userId, User user) {
        userService.update(userId, user);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
    }
}
