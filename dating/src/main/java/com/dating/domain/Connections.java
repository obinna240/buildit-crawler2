package com.dating.domain;

import lombok.AllArgsConstructor;
import lombok.Value;
import java.time.LocalDate;
import java.util.List;

@Value
@AllArgsConstructor
public final class Connections {

    private LocalDate dateOfConnection;
    private final Integer id;
    private final List<User> users;
}
