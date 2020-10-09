package com.ttulka.samples;

public class SampleServiceImpl implements SampleService {

    @Override
    public SampleResponse toUpperCase(SampleRequest request) {
        return new SampleResponse(request.getValue().toUpperCase());
    }
}
