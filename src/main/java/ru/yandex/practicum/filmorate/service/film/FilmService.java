package ru.yandex.practicum.filmorate.service.film;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;


    public Film like(int filmId, int userId) {
        return filmStorage.putALike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (filmStorage.getFilmById(filmId).getLikes().contains(userId)) {
            filmStorage.deleteLike(filmId, userId);
        } else {
            throw new NotFoundException("Пользователь не ставил оценку данному фильму");
        }
    }

    public List<Film> getTopFilms(int count) {
        return filmStorage.getRating(count);
    }

}
