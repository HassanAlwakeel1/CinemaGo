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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieCrewServiceImpl implements MovieCrewService {

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
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id " + dto.getMovieId()));
        Person person = personRepository.findById(dto.getPersonId())
                .orElseThrow(() -> new RuntimeException("Person not found with id " + dto.getPersonId()));

        MovieCrew crew = MovieCrew.builder()
                .movie(movie)
                .person(person)
                .role(dto.getRole())
                .characterName(dto.getCharacterName())
                .build();

        MovieCrew saved = movieCrewRepository.save(crew);
        return mapToDTO(saved);
    }

    @Override
    public MovieCrewResponseDTO getById(Long id) {
        MovieCrew crew = movieCrewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovieCrew not found with id " + id));
        return mapToDTO(crew);
    }

    @Override
    public List<MovieCrewResponseDTO> getAll() {
        return movieCrewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieCrewResponseDTO> getByMovie(Long movieId) {
        return movieCrewRepository.findByMovieId(movieId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieCrewResponseDTO> getByPerson(Long personId) {
        return movieCrewRepository.findByPersonId(personId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MovieCrewResponseDTO updateMovieCrew(Long id, MovieCrewRequestDTO dto) {
        MovieCrew existing = movieCrewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovieCrew not found with id " + id));

        if (dto.getRole() != null) existing.setRole(dto.getRole());
        existing.setCharacterName(dto.getCharacterName());

        MovieCrew updated = movieCrewRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    public void removeFromMovie(Long id) {
        MovieCrew existing = movieCrewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MovieCrew not found with id " + id));
        movieCrewRepository.delete(existing);
    }
}