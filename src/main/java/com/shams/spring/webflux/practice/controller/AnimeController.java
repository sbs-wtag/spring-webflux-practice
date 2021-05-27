package com.shams.spring.webflux.practice.controller;

import com.shams.spring.webflux.practice.entity.Anime;
import com.shams.spring.webflux.practice.repository.AnimeRepository;
import com.shams.spring.webflux.practice.service.AnimeService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("animes")
@RequiredArgsConstructor
public class AnimeController {

  private final AnimeService animeService;

  @GetMapping
  public Flux<Anime> getAll() {
    return animeService.findAll();
  }

  @GetMapping("/{id}")
  public Mono<Anime> getOne(@PathVariable int id) {
    return animeService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Anime> create(@Valid @RequestBody Anime anime) {
    return animeService.save(anime);
  }

  @PutMapping("/{id}")
  public Mono<Anime> update(@PathVariable int id, @Valid @RequestBody Anime anime) {
    return animeService.update(id, anime);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> delete(@PathVariable int id) {
    return animeService.delete(id);
  }
}
