package dev.agasen.junit.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.ServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SampleRestControllerTest {

  @Autowired private MockMvc mvc;

  @Test
  public void testGetCountries() throws Exception {
    mvc.perform(get("/countries"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$", hasSize(3))); // As time of making, /countries returns 3 countries
  }

  @Test
  void testGetCountry() {
    Throwable throwable = assertThrows(
      ServletException.class, 
      () -> mvc.perform(get("/countries/USA"))
        // here, we can change our code to status().badRequest() -- this is more clear
        // change the exception handling in main class
        .andExpect(status().isInternalServerError())
    );
    
    // Extend RuntimeException, name it CountryNotFoundException 
    assertEquals(RuntimeException.class, throwable.getCause().getClass());
  }
}