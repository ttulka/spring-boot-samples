package com.ttulka.samples.jpa.spring.hibernate.model1;

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
public class SampleEntity1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
