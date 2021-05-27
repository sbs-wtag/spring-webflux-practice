package com.shams.spring.webflux.practice.service;

import com.shams.spring.webflux.practice.entity.Anime;
import com.shams.spring.webflux.practice.exception.NotFoundException;
import com.shams.spring.webflux.practice.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnimeService {

  private final AnimeRepository animeRepository;

  @Transactional(readOnly = true)
  public Mono<Anime> findById(int id) {
    return animeRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Anime not found")));
  }

  @Transactional(readOnly = true)
  public Flux<Anime> findAll() {
    return animeRepository.findAll();
  }

  @Transactional
  public Mono<Anime> save(Anime anime) {
    return animeRepository.save(anime);
  }

  @Transactional
  public Mono<Anime> update(int id, Anime anime) {
    return findById(id)
        .map(animeToUpdate -> anime.withId(animeToUpdate.getId()))
        .flatMap(animeRepository::save);
  }

  @Transactional
  public Mono<Void> delete(int id) {
    return findById(id).flatMap(animeRepository::delete);
  }
}
