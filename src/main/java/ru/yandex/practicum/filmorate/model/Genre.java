package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Genre {
    @NotNull
    private Integer id;

    @NotNull
    private String name;

    public int compareTo(Genre genre) {
        return (genre.getId().compareTo(this.getId()));
    }
}
