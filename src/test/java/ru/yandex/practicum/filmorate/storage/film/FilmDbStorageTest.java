package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final LikeDbStorage likeDbStorage;
    private final JdbcTemplate jdbcTemplate;
    Film film;
    Film film2;
    User user;
    User user2;


    @BeforeEach
    void setup() {
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("DELETE FROM films");
        film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(1976, 11, 12))
                .duration(136)
                .build();
        film.setGenres(new HashSet<>());
        film.setLikes(new HashSet<>());
        film.setMpa(Mpa.builder()
                .id(1)
                .name("NC-17")
                .build());

        film2 = Film.builder()
                .name("name2")
                .description("desc")
                .releaseDate(LocalDate.of(1989, 10, 26))
                .duration(136)
                .build();
        film2.setGenres(new HashSet<>());
        film2.setLikes(new HashSet<>());
        film2.setMpa(Mpa.builder()
                .id(1)
                .name("NC-17")
                .build());

        user = User.builder()
                .email("mail@mail.mail")
                .login("login")
                .birthday(LocalDate.of(1999, 8, 17))
                .build();
        user.setFriends(new HashSet<>());

        user2 = User.builder()
                .email("gmail@gmail.gmail")
                .login("nelogin")
                .birthday(LocalDate.of(2001, 6, 19))
                .build();
        user2.setFriends(new HashSet<>());
    }


    @Test
    void shouldCreateAndGetAndUpdateFilm() {
        filmDbStorage.addFilm(film);
        assertEquals(film, filmDbStorage.getFilmById(film.getId()));

        film.setName("updateName");
        filmDbStorage.updateFilm(film);
        assertEquals("updateName", filmDbStorage.getFilmById(film.getId()).getName());

        assertEquals(1, filmDbStorage.findAllFilms().size());
    }

    /**
     * Тест на постановку лайка и получение рейтинга фильмов
     */
    @Test
    void shouldPutALikeAndGetRating() {
        filmDbStorage.addFilm(film);
        userDbStorage.addUser(user);
        userDbStorage.addUser(user2);
        filmDbStorage.putALike(1, 1);
        assertEquals(1, likeDbStorage.getLikesForCurrentFilm(1).size());

        filmDbStorage.addFilm(film2);
        filmDbStorage.putALike(2, 1);
        filmDbStorage.putALike(2, 2);
        assertEquals(2L, filmDbStorage.getRating(2).get(0).getId());
        assertEquals(2, filmDbStorage.getRating(2).size());

        filmDbStorage.deleteLike(1, 1);
        assertTrue(likeDbStorage.getLikesForCurrentFilm(1).isEmpty());
    }

}