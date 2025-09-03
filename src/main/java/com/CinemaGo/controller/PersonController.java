package com.CinemaGo.controller;

import com.CinemaGo.model.dto.PersonRequestDTO;
import com.CinemaGo.model.dto.PersonResponseDTO;
import com.CinemaGo.model.entity.Person;
import com.CinemaGo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @PostMapping
    public ResponseEntity<PersonResponseDTO> createPerson(@RequestBody PersonRequestDTO dto) {
        return ResponseEntity.ok(personService.createPerson(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> updatePerson(@PathVariable Long id, @RequestBody PersonRequestDTO person) {
        return ResponseEntity.ok(personService.updatePerson(id, person));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
