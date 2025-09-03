package com.CinemaGo.service.impl;

import com.CinemaGo.model.dto.PersonRequestDTO;
import com.CinemaGo.model.dto.PersonResponseDTO;
import com.CinemaGo.model.entity.Person;
import com.CinemaGo.repository.PersonRepository;
import com.CinemaGo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public PersonResponseDTO createPerson(PersonRequestDTO dto) {
        Person person = Person.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .bio(dto.getBio())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .build();

        Person saved = personRepository.save(person);

        return PersonResponseDTO.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .bio(saved.getBio())
                .profilePictureUrl(saved.getProfilePictureUrl())
                .build();
    }

    @Override
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id " + id));
    }

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public PersonResponseDTO updatePerson(Long id, PersonRequestDTO dto) {
        // Fetch existing person
        Person existing = getPersonById(id);

        // Update fields from DTO
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setBio(dto.getBio());
        existing.setProfilePictureUrl(dto.getProfilePictureUrl());

        // Save updated entity
        Person updated = personRepository.save(existing);

        // Return as DTO
        return PersonResponseDTO.builder()
                .id(updated.getId())
                .firstName(updated.getFirstName())
                .lastName(updated.getLastName())
                .bio(updated.getBio())
                .profilePictureUrl(updated.getProfilePictureUrl())
                .build();
    }

    @Override
    public void deletePerson(Long id) {
        Person existing = getPersonById(id);
        personRepository.delete(existing);
    }
}
