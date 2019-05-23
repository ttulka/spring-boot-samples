package com.ttulka.samples.jpa.spring.hibernate.model2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "sample")
@Data
@EqualsAndHashCode(exclude = "id")
public class SampleEntity2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
