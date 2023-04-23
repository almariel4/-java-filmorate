package ru.yandex.practicum.filmorate.storage.like;

import java.util.Set;

public interface LikeStorage {

    /**
     * Метод для получения лайков у фильма по его id
     */
    Set<Integer> getLikesForCurrentFilm(int id);
}
