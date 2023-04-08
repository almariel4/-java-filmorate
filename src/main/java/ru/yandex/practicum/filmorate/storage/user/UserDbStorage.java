package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public class UserDbStorage implements  UserStorage {
    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User addFriend(Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public User deleteFriend(Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public List<User> getMutualFriends(Integer id, Integer otherId) {
        return null;
    }

    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public List<User> getFriendsByUserId(Integer id) {
        return null;
    }
}
