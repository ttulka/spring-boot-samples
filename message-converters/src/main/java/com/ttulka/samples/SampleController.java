package com.ttulka.samples;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping(path = "/data", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public SampleData sampleData() {
        return new SampleData(new String[]{"sample1", "sample2"});
    }
}
