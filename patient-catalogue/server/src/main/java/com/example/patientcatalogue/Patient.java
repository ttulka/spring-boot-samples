package com.example.patientcatalogue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Patient {

    private final String id;
    private String name;
    private String email;
}
