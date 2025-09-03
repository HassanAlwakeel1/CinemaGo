package com.CinemaGo.service.impl;

import com.CinemaGo.model.dto.PersonRequestDTO;
import com.CinemaGo.model.dto.PersonResponseDTO;
import com.CinemaGo.model.entity.Person;
import com.CinemaGo.repository.PersonRepository;
import com.CinemaGo.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private static final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);
    private final PersonRepository personRepository;

    @Override
    public PersonResponseDTO createPerson(PersonRequestDTO dto) {
        logger.info("Creating person with id: {}", dto.getFirstName()+" "+dto.getLastName());
        Person person = Person.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .bio(dto.getBio())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .build();

        Person saved = personRepository.save(person);

        logger.info("Saved person with id: {}", saved.getId());
        return PersonResponseDTO.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .bio(saved.getBio())
                .profilePictureUrl(saved.getProfilePictureUrl())
                .build();
    }

    @Override
    @Cacheable(value = "PERSON_CACHE", key = "#id")
    public Person getPersonById(Long id) {
        logger.info("Fetching person with id: {}", id);
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id " + id));
    }

    @Override
    public List<Person> getAllPersons() {
        logger.info("Fetching all persons");
        return personRepository.findAll();
    }

    @Override
    @CachePut(value = "PERSON_CACHE", key = "#id")
    public PersonResponseDTO updatePerson(Long id, PersonRequestDTO dto) {
        logger.info("Updating person with id: {}", id);
        // Fetch existing person
        Person existing = getPersonById(id);

        // Update fields from DTO
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setBio(dto.getBio());
        existing.setProfilePictureUrl(dto.getProfilePictureUrl());

        // Save updated entity
        Person updated = personRepository.save(existing);

        logger.info("Updated person with id: {}", updated.getId());
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
    @CacheEvict(value = "PERSON_CACHE", key = "#id")
    public void deletePerson(Long id) {
        logger.info("Deleting person with id: {}", id);
        Person existing = getPersonById(id);
        personRepository.delete(existing);
    }
}