package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    /**
     * Метод для получения списка всех жанров
     */
    List<Genre> findAll();

    /**
     * Метод для получения списка жанров у фильма
     */
    Set<Genre> getGenreForCurrentFilm(int id);

    /**
     * Метод для присвоения жанра фильму
     */
    void addGenresForCurrentFilm(Film film);

    /**
     * Метод для изменения жанра у фильма
     */
    void updateGenresForCurrentFilm(Film film);

    /**
     * Метод для получения жанра по id
     */
    Genre getGenreForId(int id);


}
