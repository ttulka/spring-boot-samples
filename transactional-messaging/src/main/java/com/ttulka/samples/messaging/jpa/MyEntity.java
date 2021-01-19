package com.ttulka.samples.messaging.jpa;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;


    private String value;
}
