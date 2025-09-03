package com.CinemaGo.integration.controller;

import com.CinemaGo.model.dto.PersonRequestDTO;
import com.CinemaGo.model.dto.PersonResponseDTO;
import com.CinemaGo.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // ✅ disables JWT filter for this controller test
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    private PersonResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        responseDTO = PersonResponseDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .bio("An actor")
                .profilePictureUrl("http://example.com/john.jpg")
                .build();
    }

    @Test
    void createPerson_ShouldReturnCreatedPerson() throws Exception {
        PersonRequestDTO requestDTO = new PersonRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setBio("An actor");
        requestDTO.setProfilePictureUrl("http://example.com/john.jpg");

        Mockito.when(personService.createPerson(any(PersonRequestDTO.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void getPersonById_ShouldReturnPerson() throws Exception {
        Mockito.when(personService.getPersonById(1L))
                .thenReturn(new com.CinemaGo.model.entity.Person());

        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk());
        // You can expand jsonPath assertions if Person entity has getters populated
    }

    @Test
    void getAllPersons_ShouldReturnList() throws Exception {
        com.CinemaGo.model.entity.Person person1 = new com.CinemaGo.model.entity.Person();
        person1.setId(1L);
        person1.setFirstName("John");
        person1.setLastName("Doe");

        com.CinemaGo.model.entity.Person person2 = new com.CinemaGo.model.entity.Person();
        person2.setId(2L);
        person2.setFirstName("Jane");
        person2.setLastName("Smith");

        Mockito.when(personService.getAllPersons()).thenReturn(List.of(person1, person2));

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }

    @Test
    void updatePerson_ShouldReturnUpdatedPerson() throws Exception {
        PersonRequestDTO requestDTO = new PersonRequestDTO();
        requestDTO.setFirstName("Updated");
        requestDTO.setLastName("User");
        requestDTO.setBio("Updated bio");
        requestDTO.setProfilePictureUrl("http://example.com/updated.jpg");

        PersonResponseDTO updatedResponse = PersonResponseDTO.builder()
                .id(1L)
                .firstName("Updated")
                .lastName("User")
                .bio("Updated bio")
                .profilePictureUrl("http://example.com/updated.jpg")
                .build();

        Mockito.when(personService.updatePerson(eq(1L), any(PersonRequestDTO.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    void deletePerson_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(personService).deletePerson(1L);

        mockMvc.perform(delete("/api/persons/1"))
                .andExpect(status().isNoContent());
    }
}