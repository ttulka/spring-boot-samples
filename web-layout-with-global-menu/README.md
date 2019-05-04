# Spring Boot App with Spring MVC and Thymeleaf

As a dependency for creating a simple uniform layouted web applications.

## Build
```
mvn clean install
```

## Run a sample app
```
mvn spring-boot:run -f sample/app/pom.xml
```  

Open in a browser: `http://localhost:8080/ui/sample/`     

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

If modules config classes are not included as factories (`META-INF/spring.factories`), the app must import them:

```
@SpringBootConfiguration
@EnableAutoConfiguration
@Import({
        com.ttulka.sample.web1.Config.class
})
```

## Security

Sample credentials: 

- User: `demo`/`demo` (can't access the web at all)
- Admin: `demoadmin`/`demo` (full access)

## Global Menu

By adding the dependency:

```
<dependency>
    <groupId>com.ttulka.sample.web</groupId>
    <artifactId>web-global-menu</artifactId>
    <version>...</version>
</dependency>
```

Will redefine the bean `layoutHeaderTemplate` to set the header template to its own `globmenu/header`.