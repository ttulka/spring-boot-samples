package cz.net21.ttulka.samples.app;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootConfiguration
@EnableAutoConfiguration
public class MyApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MyApplication.class)
                .properties("spring.jmx.enabled=false")
                .run(args);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationWeb1(WebApplicationContext wac) {
        return servletRegistration(wac, "web1", "/web1/*",
                                   cz.net21.ttulka.samples.web1.WebConfig.class,
                                   cz.net21.ttulka.samples.web1.ThymeleafConfig.class);
    }

    @Bean
    public ServletRegistrationBean servletRegistrationWeb2(WebApplicationContext wac) {
        return servletRegistration(wac, "web2", "/web2/*",
                                   cz.net21.ttulka.samples.web2.WebConfig.class,
                                   cz.net21.ttulka.samples.web2.ThymeleafConfig.class);
    }

    private ServletRegistrationBean servletRegistration(WebApplicationContext wac, String servletName, String servletPath, Class<?>... configs) {
        AnnotationConfigServletWebServerApplicationContext ac = new AnnotationConfigServletWebServerApplicationContext();
        ac.setServletContext(wac.getServletContext());
        ac.register(configs);
        ac.refresh();

        ServletRegistrationBean registration = new ServletRegistrationBean(new DispatcherServlet(ac), servletPath);
        registration.setOrder(10);
        registration.setName(servletName);

        return registration;
    }
}
