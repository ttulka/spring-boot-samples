package com.ttulka.samples.module1;

public class Application1 {

    public static void main(String[] args) {

        var module1 = new Module1();

        System.out.println(String.format("Module1 has submodules:\n\t%s\n\t%s",
                module1.submodule1, module1.submodule2));
    }
}
