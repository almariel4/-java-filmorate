package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql({"/schema.sql", "/data.sql"})
class FilmServiceTests {
    private final FilmService filmService;
    private final UserService userService;

    private Genre genre1 = Genre.builder()
            .id(1)
            .name("Комедия")
            .build();

    private Genre genre2 = Genre.builder()
            .id(2)
            .name("Драма")
            .build();

    private Like like1 = Like.builder()
            .id(1)
            .userId(1)
            .filmId(1)
            .build();

    private Set<Integer> likes = new HashSet<>();
    private Film film1 = Film.builder()
            .id(1)
            .name("Film1")
            .description("descriptionFilm1")
            .releaseDate(LocalDate.of(1999, 11, 5))
            .duration(120)
            .mpa(new Mpa(1, "G"))
            .genres(Set.of(genre1))
            .likes(likes)
            .build();

    private Film film2 = Film.builder()
            .id(2)
            .name("Film2")
            .description("descriptionFIlm2")
            .releaseDate(LocalDate.of(1980, 6, 11))
            .duration(118)
            .mpa(new Mpa(1, "G"))
            .genres(Set.of(genre1, genre2))
            .likes(likes)
            .build();

    private User user = User.builder()
            .id(1)
            .email("test@mail.ru")
            .login("LoginUser1")
            .name("NameUser1")
            .birthday(LocalDate.of(1984, 5, 11))
            .build();

    @Test
    public void addFilmAndGetFilmTest() {
        filmService.addFilm(film1);

        assertEquals(film1, filmService.getFilmById(1));
    }

    @Test
    public void findAllFilmsTest() {
        filmService.addFilm(film1);

        assertEquals(List.of(film1), filmService.findAllFilms());
    }

    @Test
    public void updateMpaFromFilmTest() {
        filmService.addFilm(film1);
        film1.setMpa(new Mpa(2, "PG"));
        filmService.updateFilm(film1);

        assertEquals((film1), filmService.getFilmById(1));
    }

    @Test
    public void getBestFilmsOfGenreAndYearTest() {
        filmService.addFilm(film1);
        filmService.addFilm(film2);

        assertEquals((List.of(film1)), filmService.getBestFilmsOfGenreAndYear(1,1, 1999));
    }

}