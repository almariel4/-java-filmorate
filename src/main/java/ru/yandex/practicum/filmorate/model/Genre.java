package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Genre {
    @NotNull
    private Integer id;

    @NotNull
    private String name;

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int compareTo(Genre genre) {
        return (genre.getId().compareTo(this.getId())) * -1;
    }
}
