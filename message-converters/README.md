# Spring HTTP Message Converters Customizing

Source codes for [my blog post](https://blog.ttulka.com/spring-http-message-converters-customizing).

## Run the tests
```
mvn clean test
```

## Run the application
```
mvn spring-boot:run
```

Open `http://localhost:8080/data` in the browser or with cURL:

```sh
curl http://localhost:8080/data -H 'Accept: application/xml'
```

You see the following:
```xml
<SampleData>
    <data>sample1</data>
    <data>sample2</data>
</SampleData>
```

In `SampleApplication.java` remove the bean definition `mappingJackson2XmlHttpMessageConverter` and restart the application.

You see the following:
```xml
<SampleData>
    <data>
        <data>sample1</data>
        <data>sample2</data>
    </data>
</SampleData>
```
