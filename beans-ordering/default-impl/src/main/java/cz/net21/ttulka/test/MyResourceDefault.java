package cz.net21.ttulka.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MyResourceDefault implements MyResource {

    private final String name;
}