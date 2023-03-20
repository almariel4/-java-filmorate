package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    /**
     * Метод для получения списка пользователей
     */
    List<User> findAllUsers();

    /**
     * Метод для добавления пользователя
     */
    User addUser(User user);

    /**
     * Метод для обновления пользователя
     */
    User updateUser(User user);

    /**
     * Метод для получения пользователя по id
     */

    User getUserById(int id);


}
