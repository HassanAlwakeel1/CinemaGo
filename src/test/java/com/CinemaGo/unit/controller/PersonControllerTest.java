package com.CinemaGo.unit.controller;

import com.CinemaGo.controller.PersonController;
import com.CinemaGo.model.dto.PersonRequestDTO;
import com.CinemaGo.model.dto.PersonResponseDTO;
import com.CinemaGo.model.entity.Person;
import com.CinemaGo.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private PersonRequestDTO requestDTO;
    private PersonResponseDTO responseDTO;
    private Person person;

    @BeforeEach
    void setUp() {
        requestDTO = new PersonRequestDTO();
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setBio("Actor");
        requestDTO.setProfilePictureUrl("http://image.com/john.jpg");

        responseDTO = PersonResponseDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .bio("Actor")
                .profilePictureUrl("http://image.com/john.jpg")
                .build();

        person = new Person();
        person.setId(1L);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setBio("Actor");
        person.setProfilePictureUrl("http://image.com/john.jpg");
    }

    @Test
    void createPerson_ShouldReturnCreatedPerson() {
        when(personService.createPerson(any(PersonRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<PersonResponseDTO> response = personController.createPerson(requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDTO, response.getBody());
        verify(personService, times(1)).createPerson(requestDTO);
    }

    @Test
    void getPersonById_ShouldReturnPerson() {
        when(personService.getPersonById(anyLong())).thenReturn(person);

        ResponseEntity<Person> response = personController.getPersonById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(person, response.getBody());
        verify(personService, times(1)).getPersonById(1L);
    }

    @Test
    void getAllPersons_ShouldReturnListOfPersons() {
        when(personService.getAllPersons()).thenReturn(Collections.singletonList(person));

        ResponseEntity<List<Person>> response = personController.getAllPersons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(personService, times(1)).getAllPersons();
    }

    @Test
    void updatePerson_ShouldReturnUpdatedPerson() {
        when(personService.updatePerson(anyLong(), any(PersonRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<PersonResponseDTO> response = personController.updatePerson(1L, requestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(responseDTO, response.getBody());
        verify(personService, times(1)).updatePerson(1L, requestDTO);
    }

    @Test
    void deletePerson_ShouldReturnNoContent() {
        doNothing().when(personService).deletePerson(anyLong());

        ResponseEntity<Void> response = personController.deletePerson(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(personService, times(1)).deletePerson(1L);
    }
}