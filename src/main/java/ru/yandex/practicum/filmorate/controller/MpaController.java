package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {

    private final MpaService mpaService;


    @GetMapping
    public List<Mpa> findAll() {
        log.info("Получен запрос на получение списка MPA");
        return mpaService.findAll();
    }


    @GetMapping("/{id}")
    public Mpa getMpaRating(@PathVariable("id") int mpaId) {
        log.info("Получен запрос на получение MPA по id: " + mpaId);
        return mpaService.getMpaRating(mpaId);
    }
}