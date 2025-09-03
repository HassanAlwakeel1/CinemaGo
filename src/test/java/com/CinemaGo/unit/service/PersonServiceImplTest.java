package com.CinemaGo.unit.service;

import com.CinemaGo.model.dto.PersonRequestDTO;
import com.CinemaGo.model.dto.PersonResponseDTO;
import com.CinemaGo.model.entity.Person;
import com.CinemaGo.repository.PersonRepository;
import com.CinemaGo.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private PersonRequestDTO requestDto;
    private Person person;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new PersonRequestDTO();
        requestDto.setFirstName("John");
        requestDto.setLastName("Doe");
        requestDto.setBio("Actor");
        requestDto.setProfilePictureUrl("url.jpg");

        person = Person.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .bio("Actor")
                .profilePictureUrl("url.jpg")
                .build();
    }

    @Test
    void createPerson_ShouldReturnPersonResponseDTO() {
        when(personRepository.save(any(Person.class))).thenReturn(person);

        PersonResponseDTO response = personService.createPerson(requestDto);

        assertNotNull(response);
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("Actor", response.getBio());
        assertEquals("url.jpg", response.getProfilePictureUrl());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void getPersonById_ExistingId_ShouldReturnPerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        Person result = personService.getPersonById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(personRepository, times(1)).findById(1L);
    }

    @Test
    void getPersonById_NonExistingId_ShouldThrowException() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> personService.getPersonById(99L));

        assertEquals("Person not found with id 99", exception.getMessage());
        verify(personRepository, times(1)).findById(99L);
    }

    @Test
    void getAllPersons_ShouldReturnListOfPersons() {
        when(personRepository.findAll()).thenReturn(Arrays.asList(person));

        List<Person> result = personService.getAllPersons();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void updatePerson_ExistingId_ShouldReturnUpdatedPersonResponseDTO() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(person);

        requestDto.setFirstName("Jane");
        requestDto.setLastName("Smith");

        PersonResponseDTO response = personService.updatePerson(1L, requestDto);

        assertNotNull(response);
        assertEquals("Jane", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void updatePerson_NonExistingId_ShouldThrowException() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> personService.updatePerson(99L, requestDto));
    }

    @Test
    void deletePerson_ExistingId_ShouldDeletePerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        personService.deletePerson(1L);

        verify(personRepository, times(1)).delete(person);
    }

    @Test
    void deletePerson_NonExistingId_ShouldThrowException() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> personService.deletePerson(99L));

        verify(personRepository, never()).delete(any(Person.class));
    }
}