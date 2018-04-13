package com.example.patientcatalogue;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class PatientRepositoryInMemoryImpl implements PatientRepository {

    final Set<Patient> storage = new CopyOnWriteArraySet<>();

    @Override
    public Optional<Patient> get(String id) {
        return storage.stream()
                .filter(patient -> patient.getId().equals(id))
                .findAny();
    }

    @Override
    public Set<Patient> all() {
        return storage.stream()
                .collect(Collectors.toSet());
    }

    @Override
    public String create(String name, String email) {
        String id = UUID.randomUUID().toString();
        storage.add(new Patient(id, name, email));
        return id;
    }

    @Override
    public boolean update(String id, String name, String email) {
        return 0 != storage.stream()
                .filter(patient -> patient.getId().equals(id))
                .map(patient -> {
                    patient.setName(name);
                    patient.setEmail(email);
                    return patient;
                })
                .count();
    }

    @Override
    public boolean delete(String id) {
        Set<Patient> toDelete = storage.stream()
                .filter(patient -> patient.getId().equals(id))
                .collect(Collectors.toSet());

        if (!toDelete.isEmpty()) {
            storage.removeAll(toDelete);
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        storage.clear();
    }
}
