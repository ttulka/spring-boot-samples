package com.example.patientcatalogue;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class PatientController {

    private PatientRepository repository;

    @Autowired
    public PatientController(PatientRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Set<Patient> all() {
        return repository.all();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Patient get(@PathVariable String id) {
        assert id != null && !id.isEmpty();
        return repository.get(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Patient ID %s not found.", id)));
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@RequestBody Map<String, String> body) {
        assert body.containsKey("name") && body.containsKey("email");
        return repository.create(body.get("name"), body.get("email"));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public boolean update(@PathVariable String id, @RequestBody Map<String, String> body) {
        assert id != null && !id.isEmpty();
        assert body.containsKey("name") && body.containsKey("email");
        return repository.update(id, body.get("name"), body.get("email"));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public boolean delete(@PathVariable String id) {
        assert id != null && !id.isEmpty();
        return repository.delete(id);
    }
}

@ControllerAdvice(assignableTypes = PatientController.class)
class PatientControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }
}

