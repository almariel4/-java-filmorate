package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    /**
     * Метод для получения списка фильмов
     */
    List<Film> findAllFilms();

    /**
     * Метод для добавления фильма
     */
    Film addFilm(Film film);

    /**
     * Метод для обновления фильма
     */
    Film updateFilm(Film film);

    /**
     * Метод получения фильма по го id
     */
    Film getFilmById(int id);
}
