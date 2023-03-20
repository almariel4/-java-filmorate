package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int idForUser = 0;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        userValidation(user);
        user.setId(getIdForUser());
        users.put(user.getId(), user);
        log.info("Поступил запрос на добавление пользователя. Пользователь добавлен.");
         return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) != null) {
            userValidation(user);
            users.put(user.getId(), user);
            log.info("Поступил запрос на изменения пользователя. Пользователь изменён.");
        } else {
            log.error("Поступил запрос на изменения пользователя. Пользователь не найден.");
            throw new UserException("User not found.");
        }
        return user;
    }

    @Override
    public User getUserById(int id) {
        return null;
    }


    private int getIdForUser() {
        return ++idForUser;
    }

    private void userValidation(User user) throws ValidationException {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }
}
