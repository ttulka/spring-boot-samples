package com.ttulka.samples;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringJUnitConfig(classes = {ThymeleafAutoConfiguration.class, IndexController.class})
@WebAppConfiguration
@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension.class)
class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private WebDriver webDriver;

    @BeforeEach
    void setupDriver() {
        this.webDriver = new HtmlUnitDriver(false);
    }

    @Test
    void test() throws Exception {
        String htmlContent = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andReturn().getResponse().getContentAsString();

        assertThat(htmlContent).contains("My Data");
    }
}