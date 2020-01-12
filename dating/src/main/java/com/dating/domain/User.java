package com.dating.domain;

import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class User {

    private final UUID regId;
    private final String alias;
    private final String phoneNumber;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String middleName;
    private final OffsetDateTime dateOfRegistration;
    private final Profile profile;
    private final String address;

}
