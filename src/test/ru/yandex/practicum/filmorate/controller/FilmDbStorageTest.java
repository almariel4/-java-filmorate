package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmDbStorage;

@DataJdbcTest
@Sql({"/testdata.sql", "/testschema.sql"})
@RequiredArgsConstructor
public class FilmDbStorageTest {
    private FilmDbStorage filmDbStorage;

}