package com.CinemaGo.service.impl;

import com.CinemaGo.model.dto.MovieCrewRequestDTO;
import com.CinemaGo.model.dto.MovieCrewResponseDTO;
import com.CinemaGo.model.entity.Movie;
import com.CinemaGo.model.entity.MovieCrew;
import com.CinemaGo.model.entity.Person;
import com.CinemaGo.repository.MovieCrewRepository;
import com.CinemaGo.repository.MovieRepository;
import com.CinemaGo.repository.PersonRepository;
import com.CinemaGo.service.MovieCrewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieCrewServiceImpl implements MovieCrewService {

    private static final Logger logger = LoggerFactory.getLogger(MovieCrewServiceImpl.class);

    private final MovieCrewRepository movieCrewRepository;
    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;

    private MovieCrewResponseDTO mapToDTO(MovieCrew crew) {
        return MovieCrewResponseDTO.builder()
                .id(crew.getId())
                .movieId(crew.getMovie().getId())
                .movieTitle(crew.getMovie().getTitle())
                .personId(crew.getPerson().getId())
                .personName(crew.getPerson().getFirstName() + " " + crew.getPerson().getLastName())
                .role(crew.getRole())
                .characterName(crew.getCharacterName())
                .build();
    }

    @Override
    public MovieCrewResponseDTO addPersonToMovie(MovieCrewRequestDTO dto) {
        logger.info("Adding person with ID {} to movie with ID {} as {}", 
            dto.getPersonId(), dto.getMovieId(), dto.getRole());
            
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> {
                    String error = "Movie not found with id " + dto.getMovieId();
                    logger.error(error);
                    return new RuntimeException(error);
                });
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> {
                    String error = "Person not found with id " + dto.getPersonId();
                    logger.error(error);
                    return new RuntimeException(error);
                });

        MovieCrew crew = MovieCrew.builder()
                .movie(movie)
                .person(person)
                .role(dto.getRole())
                .characterName(dto.getCharacterName())
                .build();

        MovieCrew saved = movieCrewRepository.save(crew);
        logger.debug("Successfully added person {} to movie {} with role {}", 
            person.getFirstName() + " " + person.getLastName(), 
            movie.getTitle(), 
            dto.getRole());
        return mapToDTO(saved);
    }

    @Override
    @Cacheable(value = "MOVIE_CREW_CACHE", key = "#id")
    public MovieCrewResponseDTO getById(Long id) {
        logger.debug("Fetching movie crew with ID: {}", id);
        MovieCrew crew = movieCrewRepository.findById(id)
                .orElseThrow(() -> {
                    String error = "MovieCrew not found with id " + id;
                    logger.warn(error);
                    return new RuntimeException(error);
                });
        return mapToDTO(crew);
    }

    @Override
    public List<MovieCrewResponseDTO> getAll() {
        return movieCrewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "MOVIE_CREW_BY_MOVIE", key = "#movieId")
    public List<MovieCrewResponseDTO> getByMovie(Long movieId) {
        logger.debug("Fetching all crew for movie ID: {}", movieId);
        List<MovieCrewResponseDTO> result = movieCrewRepository.findByMovieId(movieId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        logger.trace("Found {} crew members for movie ID: {}", result.size(), movieId);
        return result;
    }

    @Override
    @Cacheable(value = "MOVIE_CREW_BY_PERSON", key = "#personId")
    public List<MovieCrewResponseDTO> getByPerson(Long personId) {
        logger.debug("Fetching all movies for person ID: {}", personId);
        List<MovieCrewResponseDTO> result = movieCrewRepository.findByPersonId(personId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        logger.trace("Found {} movies for person ID: {}", result.size(), personId);
        return result;
    }

    @Override
    @CachePut(value = "MOVIE_CREW_CACHE", key = "#id")
    public MovieCrewResponseDTO updateMovieCrew(Long id, MovieCrewRequestDTO dto) {
        logger.info("Updating movie crew with ID: {}", id);
        MovieCrew existing = movieCrewRepository.findById(id)
                .orElseThrow(() -> {
                    String error = "MovieCrew not found with id " + id;
                    logger.error(error);
                    return new RuntimeException(error);
                });

        if (dto.getRole() != null) existing.setRole(dto.getRole());
        existing.setCharacterName(dto.getCharacterName());

        MovieCrew updated = movieCrewRepository.save(existing);
        logger.debug("Successfully updated movie crew with ID: {}", id);
        return mapToDTO(updated);
    }

    @Override
    @CacheEvict(value = {"MOVIE_CREW_CACHE", "MOVIE_CREW_BY_MOVIE", "MOVIE_CREW_BY_PERSON"}, allEntries = true)
    public void removeFromMovie(Long id) {
        logger.info("Attempting to remove movie crew with ID: {}", id);
        MovieCrew existing = movieCrewRepository.findById(id)
                .orElseThrow(() -> {
                    String error = "MovieCrew not found with id " + id;
                    logger.warn(error);
                    return new RuntimeException(error);
                });
        movieCrewRepository.delete(existing);
        logger.debug("Successfully removed movie crew with ID: {}", id);
    }
}