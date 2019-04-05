# Spring Boot App with Multiple Web Modules

Thymeleaf-based web modules are integrated into a single Spring Boot app.

## Build
```
mvn clean install
```

## Run
```
mvn spring-boot:run -f app/pom.xml
```  

Open in a browser: `http://localhost:8080/ui/`


## Concept

Web modules are integrated thru different prefixes of their URLs:

```
@RequestMapping("${path.web1:}")
```

Bean names must be unique:

```
@Controller("web1IndexController")
class IndexController {
``` 

View template identifiers must be prefixed:

```
@GetMapping
public String index() {
    return "web1/index";
}
``` 

When needed a `MessageSource` bean with an unique name for modules messages bundles could be defined:

```
@Bean
public MessageSource web1MessageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames("classpath:/messages/web1/messages");
    return messageSource;
}
```

The app must import modules config classes:

```
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableWebMvc
@Import({
        com.example.samples.web1.Config.class
})
```

## Security

- demo/demo 
- demoadmin/demo

## Global Menu

By adding the dependency:

```
<dependency>
    <groupId>com.example.samples</groupId>
    <artifactId>spring-boot-multiple-webs-web-global-menu</artifactId>
    <version>...</version>
</dependency>
```

Will redefine the fragment `"global/header :: header"` in globally templates by showing the global menu.