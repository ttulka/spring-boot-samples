package com.example.patientcatalogue;

import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class PatientRepositoryInMemoryImplTest {

    private final static Patient TEST_PATIENT = new Patient("test", "Test", "test@example.com");

    private PatientRepositoryInMemoryImpl repository;

    @Before
    public void setUp() {
        repository = new PatientRepositoryInMemoryImpl();
        repository.storage.add(TEST_PATIENT);
    }

    @Test
    public void getTest() {
        Optional<Patient> patient = repository.get(TEST_PATIENT.getId());

        assertThat(patient, is(notNullValue()));
        assertThat(patient.isPresent(), is(true));
        assertThat(patient.get(), is(TEST_PATIENT));

        assertThat(repository.storage.size(), is(1));
    }

    @Test
    public void allTest() {
        Collection<Patient> patients = repository.all();

        assertThat(patients, is(notNullValue()));
        assertThat(patients.size(), is(1));
        assertThat(patients, contains(TEST_PATIENT));

        assertThat(repository.storage.size(), is(1));
    }

    @Test
    public void createTest() {
        String id = repository.create("xxx", "xxx@example.com");

        assertThat(id, is(notNullValue()));
        assertThat(id.isEmpty(), is(false));

        assertThat(repository.storage.size(), is(2));
    }

    @Test
    public void updateTest() {
        boolean result = repository.update(TEST_PATIENT.getId(), "xxx", "xxx@example.com");

        assertThat(result, is(true));
        assertThat(repository.storage.size(), is(1));

        Patient patientUpdated = repository.storage.iterator().next();
        assertThat(patientUpdated.getId(), is(TEST_PATIENT.getId()));
        assertThat(patientUpdated.getName(), is("xxx"));
        assertThat(patientUpdated.getEmail(), is("xxx@example.com"));
    }

    @Test
    public void updateNonExistingPatientTest() {
        boolean result = repository.update("xxx", "xxx", "xxx@example.com");

        assertThat(result, is(false));
        assertThat(repository.storage.size(), is(1));

        Patient patientUpdated = repository.storage.iterator().next();
        assertThat(patientUpdated.getId(), is(TEST_PATIENT.getId()));
        assertThat(patientUpdated.getName(), is(TEST_PATIENT.getName()));
        assertThat(patientUpdated.getEmail(), is(TEST_PATIENT.getEmail()));
    }

    @Test
    public void deleteTest() {
        boolean result = repository.delete(TEST_PATIENT.getId());

        assertThat(result, is(true));
        assertThat(repository.storage.size(), is(0));
    }

    @Test
    public void deleteNonExistingPatientTest() {
        boolean result = repository.delete("xxx");

        assertThat(result, is(false));
        assertThat(repository.storage.size(), is(1));

        Patient patientUpdated = repository.storage.iterator().next();
        assertThat(patientUpdated.getId(), is(TEST_PATIENT.getId()));
        assertThat(patientUpdated.getName(), is(TEST_PATIENT.getName()));
        assertThat(patientUpdated.getEmail(), is(TEST_PATIENT.getEmail()));
    }

    @Test
    public void clearTest() {
        repository.clear();

        assertThat(repository.storage.size(), is(0));
    }
}
