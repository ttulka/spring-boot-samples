# Spring HTTP Message Converters Customizing

Source codes for the [post on medium.com](https://medium.com/@ttulka/spring-http-message-converters-customizing-770814eb2b55)

## Run the tests
```
mvn clean test
```

## Run the application
```
mvn spring-boot:run
```

Open in browser: `http://localhost:8080/data`

You see the following:
```xml
<SampleData>
    <data>sample1</data>
    <data>sample2</data>
</SampleData>
```

In `SampleApplication.java` remove the bean definition `mappingJackson2XmlHttpMessageConverter` and rerun the application.

You see the following:
```xml
<SampleData>
    <data>
        <data>sample1</data>
        <data>sample2</data>
    </data>
</SampleData>
```
