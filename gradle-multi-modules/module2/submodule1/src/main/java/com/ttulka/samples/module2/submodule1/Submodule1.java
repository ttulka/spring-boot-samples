package com.ttulka.samples.module2.submodule1;

import com.ttulka.samples.module2.submobule2.Submodule2;

public class Submodule1 {

    Submodule2 submodule2 = new Submodule2();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + submodule2 + "]";
    }
}
