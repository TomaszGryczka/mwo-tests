package com.github.tomaszgryczka.mwotests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PlayerRequest {
    private Long coachId;
    private String firstname;
    private String lastname;
    private String country;
    private LocalDate dateOfBirth;
    private Double height;
    private Double weight;
}
