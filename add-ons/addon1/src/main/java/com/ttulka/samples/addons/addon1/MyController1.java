package com.ttulka.samples.addons.addon1;

import com.ttulka.samples.addons.MyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${addons.root-url:/}")
@RequiredArgsConstructor
class MyController1 {

    private final MyService myService;

    @GetMapping("${addons.addon1.url:/}")
    public String someString() {
        return String.format("Returned from ADDON1: %s", myService.someString());
    }
}
