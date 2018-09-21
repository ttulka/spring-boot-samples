package cz.net21.ttulka.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyResourcesInitLogHolder {

    private static final List<String> inits = new ArrayList<String>();

    public static synchronized void add(String init) {
        inits.add(init);
    }

    public static synchronized List<String> list() {
        return Collections.unmodifiableList(inits);
    }
}
