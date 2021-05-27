package com.shams.spring.webflux.practice.repository;

import com.shams.spring.webflux.practice.entity.Anime;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AnimeRepository extends ReactiveCrudRepository<Anime, Integer> {

  Mono<Anime> findById(int id);
}
