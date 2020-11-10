package com.ttulka.samples.addons.addon2;

import com.ttulka.samples.addons.MyService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${addons.root-url:/}")
@RequiredArgsConstructor
class MyController2 {

    private final MyService myService;

    @GetMapping("${addons.addon2.url:/}")
    public String someString() {
        return String.format("Returned from ADDON2: %s", myService.someString());
    }
}
