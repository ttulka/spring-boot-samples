package cz.net21.ttulka.test;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MyResourceHolder {

    Collection<MyResource> myResources;
}
