package com.github.tomaszgryczka.mwotests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Player {
    private final long id;
    private long coachId;
    private String firstname;
    private String lastname;
    private String country;
    private final LocalDate dateOfBirth;
    private Double height;
    private Double weight;
}
