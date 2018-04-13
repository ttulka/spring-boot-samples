package com.example.patientcatalogue;

import java.nio.charset.Charset;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class PatientControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
                                                  MediaType.APPLICATION_JSON.getSubtype(),
                                                  Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String patientId1;
    private String patientId2;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PatientRepository patientRepository;

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();

        patientId1 = patientRepository.create("John", "john@example.com");
        patientId2 = patientRepository.create("Paul", "paul@example.com");
    }

    @After
    public void clean() {
        patientRepository.clear();
    }

    @Test
    public void listTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getPatientTest() throws Exception {
        mockMvc.perform(get("/" + patientId1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(patientId1)))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    @Test
    public void getNonExistingPatientTest() throws Exception {
        mockMvc.perform(get("/xxx"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createPatientTest() throws Exception {
        mockMvc.perform(post("/")
                                .content("{\n" +
                                         "\t\"name\":\"Test\",\n" +
                                         "\t\"email\": \"test@example.com\"\n" +
                                         "}")
                                .contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePatientTest() throws Exception {
        mockMvc.perform(put("/" + patientId1)
                                .content("{\n" +
                                         "\t\"name\":\"Test\",\n" +
                                         "\t\"email\": \"test@example.com\"\n" +
                                         "}")
                                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.TRUE.toString()));
    }

    @Test
    public void updateNonExistingPatientTest() throws Exception {
        mockMvc.perform(put("/xxx")
                                .content("{\n" +
                                         "\t\"name\":\"Test\",\n" +
                                         "\t\"email\": \"test@example.com\"\n" +
                                         "}")
                                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.FALSE.toString()));
    }

    @Test
    public void deletePatientTest() throws Exception {
        mockMvc.perform(delete("/" + patientId1))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.TRUE.toString()));
    }

    @Test
    public void deleteNonExistingPatientTest() throws Exception {
        mockMvc.perform(delete("/xxx"))
                .andExpect(status().isOk())
                .andExpect(content().string(Boolean.FALSE.toString()));
    }
}
