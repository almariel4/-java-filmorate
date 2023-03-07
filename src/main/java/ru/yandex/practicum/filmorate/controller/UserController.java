package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = "/users", produces = "application/json")
public class UserController {

    private int idForUser = 0;
    private final HashMap<Integer, User> users = new HashMap<>();


    @PostMapping
    public User create(@Valid @RequestBody User user) {
        userValidation(user);
        user.setId(getIdForUser());
        users.put(user.getId(), user);
        log.info("Поступил запрос на добавление пользователя. Пользователь добавлен.");
        return user;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        if (users.get(user.getId()) != null) {
            userValidation(user);
            users.put(user.getId(), user);
            log.info("Поступил запрос на изменения пользователя. Пользователь изменён.");
        } else {
            log.debug("Поступил запрос на изменения пользователя. Пользователь не найден.");
            throw new UserException("User not found.");
        }
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }


    private int getIdForUser() {
        idForUser++;
        return idForUser;
    }

    private void userValidation(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

}


