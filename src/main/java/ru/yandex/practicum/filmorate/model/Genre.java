package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Genre {
    @NotNull
    private Integer id;

    @NotNull
    private String name;

    public int compareTo(Genre genre) {
        return (genre.getId().compareTo(this.getId())) * -1;
    }
}
