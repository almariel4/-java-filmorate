package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/films", produces = "application/json")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int idForFilm = 0;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
            filmValidation(film);
            film.setId(getIdForFilm());
            films.put(film.getId(), film);
            log.info("Поступил запрос на добавление фильма. Фильм добавлен");

        return film;
    }

    @PutMapping
    public Film changeFilm(@Valid @RequestBody Film film) {
            if (films.get(film.getId()) != null) {
                filmValidation(film);
                films.put(film.getId(), film);
                log.info("Поступил запрос на изменения фильма. Фильм изменён.");
            } else {
                log.error("Поступил запрос на изменения фильма. Фильм не найден.");
                throw new FilmException("Film not found.");
            }
        return film;
    }

    @GetMapping()
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }


    private int getIdForFilm() {
        return ++idForFilm;
    }

    private void filmValidation(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            throw new ValidationException("Некорректно указана дата релиза.");

        }

    }

}
