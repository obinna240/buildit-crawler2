package com.dating.service;

import com.dating.domain.User;
import java.util.Optional;
import java.util.UUID;

public interface UserServiceInterface {

    Optional<User> getPersonById(UUID personId);

    User save(User user);

    void update(UUID userId, User user);

    void delete(UUID userId);
}
