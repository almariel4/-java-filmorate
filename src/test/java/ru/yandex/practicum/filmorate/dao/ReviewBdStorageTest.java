package ru.yandex.practicum.filmorate.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.likeReview.dao.LikeReviewDbStorage;
import ru.yandex.practicum.filmorate.storage.review.dao.ReviewBdStorage;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJdbcTest
@Sql(value = {"/schematest.sql", "/datatest.sql"})
public class ReviewBdStorageTest {

    private final ReviewBdStorage reviewBdStorage;
    private final LikeReviewDbStorage likeReviewDbStorage;

    @Autowired
    public ReviewBdStorageTest (JdbcTemplate jdbcTemplate){
        reviewBdStorage = new ReviewBdStorage(jdbcTemplate);
        likeReviewDbStorage = new LikeReviewDbStorage(jdbcTemplate);
    }

@Test
    public void createTest(){
    Review review = Review.builder()
            .content("Positive test review")
            .isPositive(Boolean.TRUE)
            .userId(1)
            .filmId(1)
            .build();

    reviewBdStorage.create(review);
    assertEquals(Optional.of(review) ,reviewBdStorage.findById(1) );
}



}
