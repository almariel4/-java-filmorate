package ru.yandex.practicum.filmorate.service.film;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film like(Integer filmId, Integer userId) {
        return filmStorage.like(filmId, userId);
    }

    public Film deleteLike(Integer filmId, Integer userId) {
        return filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getBestFilmsOfGenreAndYear(int count, int genre, int year) {
        return filmStorage.getPopularFIlms(count, genre, year);
    }

    public List<Film> getSearchResults(String query, String by) {
        List<Film> searchResults = new ArrayList<>();
        if (by.equals("title")) {
            searchResults =  filmStorage.searchByTitle(query);
        } else if (by.equals("director")) {
            searchResults = filmStorage.searchByDirector(query);
        }
        return searchResults;
    }
}
