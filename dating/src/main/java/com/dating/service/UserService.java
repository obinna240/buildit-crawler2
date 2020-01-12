package com.dating.service;

import com.dating.domain.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public final class UserService implements UserServiceInterface {

    @Override
    public Optional<User> getPersonById(UUID personId) {
        return Optional.empty();
    }

    //temp user
    @Override
    public User save(User user) {
        return User.builder().build();
    }

    public void delete(UUID userId) {
    }

    public void update(UUID userId, User user) {
    }
}
