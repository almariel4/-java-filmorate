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
     * Метод добавления в друзья
     */
    User addFriend(Integer userId, Integer friendId);

    /**
     * Метод удаления из друзей
     */
    User deleteFriend(Integer userId, Integer friendId);


    /**
     * Метод для обновления пользователя
     */

    List<User> getMutualFriends(Integer id, Integer otherId);

    /**
     * Метод для получения пользователя по id
     */

    User getUserById(Integer id);

    /**
     * Метод для получения списка друзей
     */
    List<User> getFriendsByUserId(Integer id);

}
