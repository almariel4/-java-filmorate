package ru.yandex.practicum.filmorate.controller;

import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.jdbc.Sql;

@DataJdbcTest
@Sql({"/schema.sql", "/data.sql"})
public class FilmDbStorageTest {
//    private final JdbcTemplate jdbcTemplate;
//    private final FilmDbStorage filmDbStorage;
//
//    public FilmDbStorageTest(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//        this.filmDbStorage = new FilmDbStorage(jdbcTemplate);
//    }
//
//    @Test
//    public void getFilmByIdTest() {
//        Film film1 = filmDbStorage.getFilmById(1);
//        Assertions.assertEquals("Film Updated", film1.getName());
//        Film film2 = filmDbStorage.getFilmById(2);
//        Assertions.assertEquals("New film", film2.getName());
//    }
//
//    @Test
//    public void getAllFilmsTest() {
//        List<Film> listFilms = filmDbStorage.findAllFilms();
//        Assertions.assertEquals(2, listFilms.size());
//    }
//
//    @Test
//    public void getPopularFilmsTest() {
//        List<Film> listFilms = filmDbStorage.getPopularFIlms(5, -1, -1);
//        Assertions.assertEquals(2, listFilms.size());
//        List<Film> listFilms1 = filmDbStorage.getPopularFIlms(5, 2, 1999);
//        Assertions.assertEquals(1, listFilms1.size());
//        Film film1 = listFilms1.get(0);
//        Assertions.assertEquals("New film", film1.getName());
//        List<Film> listFilms2 = filmDbStorage.getPopularFIlms(5, -1, 1989);
//        Film film2 = listFilms2.get(0);
//        Assertions.assertEquals("Film Updated", film2.getName());
//        Assertions.assertEquals(1, listFilms2.size());
//    }
}