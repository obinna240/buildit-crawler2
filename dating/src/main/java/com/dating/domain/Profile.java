package com.dating.domain;

import com.dating.domain.enumerations.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value
@Builder
@AllArgsConstructor
public final class Profile {

    private final UUID profileId;
    private final State state;
    private final LocalDate dateOfBirth;
    private final Occupation occupation;
    private final Education education;
    private final Religion religion;
    private final List<Interests> interests;
    private final Location location;
}

