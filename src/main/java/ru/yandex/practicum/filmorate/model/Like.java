package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Like {
    @NotNull
    private Integer id;

    @NotNull
    @Positive
    private Integer userId;

    @NotNull
    @Positive
    private Integer filmId;

    public Like(Integer id, Integer userId, Integer filmId) {
        this.id = id;
        this.userId = userId;
        this.filmId = filmId;
    }
}
