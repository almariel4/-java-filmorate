package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Film testFilm;

    @BeforeEach
    protected void init() {
        testFilm = Film.builder()
                .name("Тестовый фильм")
                .description("Тестовое описание тестового фильма")
                .releaseDate(LocalDate.of(1999, 12,27))
                .duration(87)
                .build();

    }


    @Test
    void createNewCorrectFilm_isOkTest() throws Exception {
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void createFilm_NameIsBlank_badRequestTest() throws Exception {
        testFilm.setName("");
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_IncorrectDescription_badRequestTest() throws Exception {
        testFilm.setDescription("Размер описания значительно превышает двести символов, а может и не превышает " +
                "(надо посчитать). Нет, к сожалению размер описания фильма сейчас не превышает двести символов," +
                "но вот сейчас однозначно стал превышать двести символов!");
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_RealiseDateInFuture_badRequestTest() throws Exception {
        testFilm.setReleaseDate(LocalDate.of(2026, 12, 9));
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createFilm_RealiseDateBeforeFirstFilmDate_badRequestTest() throws Exception {
        testFilm.setReleaseDate(LocalDate.of(1885, 12,12));
        mockMvc.perform(post("/films")
                        .content(objectMapper.writeValueAsString(testFilm))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

}