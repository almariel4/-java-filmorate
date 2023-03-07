package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    protected void init() {
        testUser = User.builder()
                .email("mail@mail.ru")
                .login("Boetticher")
                .name("Nick Name")
                .birthday(LocalDate.of(1987, 4, 14))
                .build();

    }


    @Test
    void createNewCorrectUser_Test() throws Exception {
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    void createUser_NameIsBlank_NameIsLoginTest() throws Exception {
        testUser.setName("");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.name").value(""));
    }

    @Test
    void createUser_IncorrectEmailTest() throws Exception {
        testUser.setEmail("incorrectEmail.ru");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_LoginIsBlank_badRequestTest() throws Exception {
        testUser.setLogin("");
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_BirthdayInFuture_badRequestTest() throws Exception {
        testUser.setBirthday(LocalDate.parse("2024-10-12"));
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(testUser))
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

}