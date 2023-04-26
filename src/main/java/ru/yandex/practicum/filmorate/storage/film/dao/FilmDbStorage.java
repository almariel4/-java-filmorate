package ru.yandex.practicum.filmorate.storage.film.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilmById(Integer filmId) {
        String sqlQuery =
                "SELECT film_id, name, description, release_date, duration, rating_mpa_id " +
                        "FROM films " +
                        "WHERE film_id=?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, filmId);
        } catch (RuntimeException e) {
            throw new NotFoundException("Фильм не найден.");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        List<Film> films = new ArrayList<>();

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(
                "SELECT film_id, name, description, release_date, duration, rating_mpa_id " +
                        "FROM films");

        while (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getInt("film_id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(Objects.requireNonNull(filmRows.getDate("release_date")).toLocalDate())
                    .duration(filmRows.getInt("duration"))
                    .mpa(getMpaById(filmRows.getInt("rating_mpa_id")))
                    .build();
            film.setGenres(getGenreForCurrentFilm(film.getId()));
            film.setLikes(getLikesForCurrentFilm(film.getId()));

            films.add(film);
        }
        return films;
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        film.setId(simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue());
        addMpaToFilm(film);
        addGenreNameToFilm(film);
        addGenresForCurrentFilm(film);
        log.info("Поступил запрос на добавление фильма. Фильм добавлен.");

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery =
                "UPDATE films " +
                        "SET name=?, description=?, release_date=?, duration=?, rating_mpa_id=? " +
                        "WHERE film_id=?";

        int rowsCount = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );

        addMpaToFilm(film);
        updateGenresForCurrentFilm(film);
        addGenreNameToFilm(film);
        film.setGenres(getGenreForCurrentFilm(film.getId()));

        if (rowsCount > 0) {
            return film;
        }
        throw new NotFoundException("Фильм не найден.");
    }


    @Override
    public Film like(Integer filmId, Integer userId) {
        Film film = getFilmById(filmId);
        String sqlQuery =
                "INSERT " +
                        "INTO likes (film_id, user_id) " +
                        "VALUES(?, ?)";

        jdbcTemplate.update(sqlQuery, filmId, userId);

        return film;
    }

    @Override
    public Film deleteLike(Integer filmId, Integer userId) {
        if (getUserById(userId) == null) {
            throw new NotFoundException("Пользователь не найден.");
        }

        Film film = getFilmById(filmId);
        String sqlQuery =
                "DELETE " +
                        "FROM likes " +
                        "WHERE film_id = ? AND user_id = ?";

        jdbcTemplate.update(sqlQuery, filmId, userId);

        return film;
    }

    @Override
    public List<Film> getRating(int count) {
        String sqlQuery =
                "SELECT films.*, COUNT(l.film_id) as count " +
                        "FROM films\n" +
                        "LEFT JOIN likes l ON films.film_id=l.film_id\n" +
                        "GROUP BY films.film_id\n" +
                        "ORDER BY count DESC\n" +
                        "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public List<Film> getBestFilmsOfGenreAndYear(int count, int genre, int year) {
        String sqlQuery;
        String sqlQueryStart =
                "SELECT films.*, COUNT(l.film_id) as count, G.*, GT.* " +
                        "FROM films " +
                        "LEFT JOIN likes l ON films.film_id=l.film_id " +
                        "LEFT JOIN GENRE G on FILMS.FILM_ID = G.FILM_ID " +
                        "LEFT JOIN GENRE_TYPE GT on G.ID = GT.GENRE_ID ";


        String sqlQueryFinish = "GROUP BY films.film_id, gt.NAME " +
                "ORDER BY count DESC " +
                "LIMIT ?";

        if (year == -1 && genre == -1) {
            return getRating(count);
        } else if (genre == -1 && year > 0) {
            String sqlQueryMiddle = "WHERE EXTRACT(YEAR FROM CAST(FILMS.RELEASE_DATE AS DATE)) = ? ";
            sqlQuery = sqlQueryStart + sqlQueryMiddle + sqlQueryFinish;
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, year, count);
        } else if (genre > 0 && year == -1) {
            String sqlQueryMiddle = "WHERE G.GENRE_ID = ? ";
            sqlQuery = sqlQueryStart + sqlQueryMiddle + sqlQueryFinish;
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, genre, count);
        } else {
            String sqlQueryMiddle = "WHERE G.GENRE_ID = ? AND EXTRACT(YEAR FROM CAST(FILMS.RELEASE_DATE AS DATE)) = ? ";
            sqlQuery = sqlQueryStart + sqlQueryMiddle + sqlQueryFinish;
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, genre, year, count);
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(getMpaById(resultSet.getInt("rating_mpa_id")))
                .build();
        film.setLikes(getLikesForCurrentFilm(film.getId()));
        film.setGenres(getGenreForCurrentFilm(film.getId()));

        return film;
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_mpa_id", film.getMpa().getId());
        return values;
    }

    public Mpa getMpaById(int mpaId) {
        String sqlQuery =
                "SELECT rating_mpa_id, name " +
                        "FROM mpa_type " +
                        "WHERE rating_mpa_id=?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, mpaId);
        } catch (RuntimeException e) {
            throw new NotFoundException("Рейтинг mpa не найден.");
        }
    }

    public void addMpaToFilm(Film film) {
        findAllMpa().forEach(mpa -> {
            if (Objects.equals(film.getMpa().getId(), mpa.getId())) {
                film.setMpa(mpa);
            }
        });
    }


    public List<Mpa> findAllMpa() {
        List<Mpa> mpaList = new ArrayList<>();

        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(
                "SELECT rating_mpa_id, name " +
                        "FROM mpa_type");

        while (mpaRows.next()) {
            Mpa mpa = Mpa.builder()
                    .id(mpaRows.getInt("rating_mpa_id"))
                    .name(mpaRows.getString("name"))
                    .build();
            mpaList.add(mpa);
        }
        return mpaList;
    }
    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("rating_mpa_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public Genre getGenreForId(int id) {
        String sqlQuery =
                "SELECT genre_id, name " +
                        "FROM genre_type " +
                        "WHERE genre_id=?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (RuntimeException e) {
            throw new NotFoundException("Жанр не найден.");
        }
    }
    public Set<Genre> getGenreForCurrentFilm(int id) {
        Set<Genre> genreSet = new LinkedHashSet<>();

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT id, film_id, genre_id " +
                        "FROM genre " +
                        "ORDER BY genre_id ASC");

        while (genreRows.next()) {
            if (genreRows.getLong("film_id") == id) {
                genreSet.add(getGenreForId(genreRows.getInt("genre_id")));
            }
        }
        return genreSet;
    }

    public void addGenresForCurrentFilm(Film film) {
        if (Objects.isNull(film.getGenres())) {
            return;
        }

        film.getGenres().forEach(g -> {
            String sqlQuery =
                    "INSERT " +
                            "INTO genre(film_id, genre_id) " +
                            "VALUES (?, ?)";

            jdbcTemplate.update(sqlQuery, film.getId(), g.getId());
        });
    }

    public void addGenreNameToFilm(Film film) {
        if (Objects.isNull(film.getGenres())) {
            return;
        }
        film.getGenres().forEach(g -> g.setName(getGenreForId(g.getId()).getName()));
    }

    public void updateGenresForCurrentFilm(Film film) {
        String sqlQuery =
                "DELETE " +
                        "FROM genre " +
                        "WHERE film_id = ?";

        jdbcTemplate.update(sqlQuery, film.getId());
        addGenresForCurrentFilm(film);
    }
    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build();
    }

    public User getUserById(Integer id) {
        String sqlQuery =
                "SELECT user_id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE user_id=?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (RuntimeException e) {
            throw new NotFoundException("Пользователь не найден.");
        }
    }

    public List<User> getFriendsByUserId(Integer id) {
        String sqlQuery =
                "SELECT user_id, email, login, name, birthday " +
                        "FROM users " +
                        "WHERE user_id " +
                        "IN(SELECT friend_id " +
                        "FROM friends " +
                        "WHERE user_id=?)";

        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToUser, id));
    }
    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        user.setFriends(getFriendsByUserId(user.getId()).stream().map(User::getId).collect(Collectors.toSet()));

        return user;
    }

    public Set<Integer> getLikesForCurrentFilm(int id) {
        Set<Integer> likes = new HashSet<>();
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(
                "SELECT like_id, film_id, user_id " +
                        "FROM likes");

        while (likeRows.next()) {
            if (likeRows.getInt("film_id") == id) {
                likes.add(likeRows.getInt("like_id"));
            }
        }
        return likes;
    }
}
