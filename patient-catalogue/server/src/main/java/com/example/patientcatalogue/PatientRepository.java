package com.example.patientcatalogue;

import java.util.Optional;
import java.util.Set;

public interface PatientRepository {

    /**
     * @param id
     * @return the patient with the id
     */
    Optional<Patient> get(String id);

    /**
     * @return all the patients
     */
    Set<Patient> all();

    /**
     * @param name
     * @param email
     * @return the patient ID
     */
    String create(String name, String email);

    /**
     * @param id
     * @param name
     * @param email
     * @return true whether the patient updated, otherwise false
     */
    boolean update(String id, String name, String email);

    /**
     * @param id
     * @@return true whether the patient deleted, otherwise false
     */
    boolean delete(String id);

    /**
     *
     */
    void clear();
}
