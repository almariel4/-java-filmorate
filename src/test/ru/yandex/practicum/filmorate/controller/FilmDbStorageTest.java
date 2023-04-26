package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmDbStorage;

import java.util.List;

@DataJdbcTest
@Sql({"/schema.sql", "/data.sql"})
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;

    @Autowired
    public FilmDbStorageTest(JdbcTemplate jdbcTemplate) {
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
    }

    @Test
    void getAllFilmsTest() {
        List<Film> listFilms = filmDbStorage.findAllFilms();
        Assertions.assertEquals(2, listFilms.size());
    }

    @Test
    void getPopularFilmsTest() {
        List<Film> listFilms = filmDbStorage.getBestFilmsOfGenreAndYear(5, -1, -1);
        Assertions.assertEquals(2, listFilms.size());
        List<Film> listFilms1 = filmDbStorage.getBestFilmsOfGenreAndYear(5, 2, 1999);
        Assertions.assertEquals(1, listFilms1.size());
        Film film1 = listFilms1.get(0);
        Assertions.assertEquals("New film", film1.getName());
        List<Film> listFilms2 = filmDbStorage.getBestFilmsOfGenreAndYear(5, -1, 1989);
        Film film2 = listFilms2.get(0);
        Assertions.assertEquals("Film Updated", film2.getName());
        Assertions.assertEquals(1, listFilms2.size());
    }
}