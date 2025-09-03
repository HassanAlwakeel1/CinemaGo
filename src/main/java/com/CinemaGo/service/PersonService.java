package com.CinemaGo.service;

import com.CinemaGo.model.dto.PersonRequestDTO;
import com.CinemaGo.model.dto.PersonResponseDTO;
import com.CinemaGo.model.entity.Person;

import java.util.List;

public interface PersonService {
    PersonResponseDTO createPerson(PersonRequestDTO person);
    Person getPersonById(Long id);
    List<Person> getAllPersons();
    PersonResponseDTO updatePerson(Long id, PersonRequestDTO person);
    void deletePerson(Long id);
}
