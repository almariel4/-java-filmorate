package ru.yandex.practicum.filmorate.storage.review.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReviewBdStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review create(Review review) {
        return null;
    }

    @Override
    public Optional<Review> update(Review review) {
        return Optional.empty();
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Optional<Review> findById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Review> findAll(int filmId, int count) {
        return null;
    }


}
