package com.example.jaxbmoxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@WebMvcTest
class DemoJaxbMoxyApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void correctMarshaling() throws Exception {
		mockMvc.perform(get("/api/my"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_XML))
				.andDo(print())
				.andExpect(xpath("//myDocument/myVersion/text()").string("12345"))
				.andExpect(xpath("//myDocument/myObjects").exists())
				.andExpect(xpath("//myDocument/myObjects/obj_0/text()").string("100"))
				.andExpect(xpath("//myDocument/myObjects/obj_1/text()").string("101"))
				.andExpect(xpath("//myDocument/myObjects/obj_2/text()").string("102"))
				.andExpect(xpath("//myDocument/myObjects/obj_4").doesNotExist());
	}

}
