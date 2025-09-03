package com.CinemaGo.controller;

import com.CinemaGo.model.dto.PersonRequestDTO;
import com.CinemaGo.model.dto.PersonResponseDTO;
import com.CinemaGo.model.entity.Person;
import com.CinemaGo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    private static final Logger logger = Logger.getLogger(PersonController.class.getName());


    @PostMapping
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody PersonRequestDTO dto) {
        try {
            PersonResponseDTO response = personService.createPerson(dto);
            logger.info("Created person with ID: " + response.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create person", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        try {
            Person person = personService.getPersonById(id);
            logger.info("Fetched person with ID: " + id);
            return ResponseEntity.ok(person);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch person with ID: " + id, e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        try {
            List<Person> persons = personService.getAllPersons();
            logger.info("Fetched all persons, count: " + persons.size());
            return ResponseEntity.ok(persons);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to fetch all persons", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> updatePerson(@PathVariable Long id, @RequestBody PersonRequestDTO person) {
        try {
            PersonResponseDTO updatedPerson = personService.updatePerson(id, person);
            logger.info("Updated person with ID: " + id);
            return ResponseEntity.ok(updatedPerson);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to update person with ID: " + id, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        try {
            personService.deletePerson(id);
            logger.info("Deleted person with ID: " + id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete person with ID: " + id, e);
            throw e;
        }
    }
}
