package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    /**
     * Метод для получения списка рейтингов mpa
     */
    List<Mpa> findAll();

    /**
     * Метод для получения рейтинга mpa по его id
     */
    Mpa getMpa(int mpaId);

    /**
     * Метод для присвоения рейтинга фильму
     */
    void addMpaToFilm(Film film);
}
