package com.shams.spring.webflux.practice.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.shams.spring.webflux.practice.entity.Anime;
import com.shams.spring.webflux.practice.repository.AnimeRepository;
import com.shams.spring.webflux.practice.service.AnimeService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@WebFluxTest
@Import(AnimeService.class)
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

  @MockBean
  private AnimeRepository animeRepository;

  @Autowired
  private WebTestClient webTestClient;

  private final Anime anime = new Anime().withId(1).withName("My Anime");

  @BeforeAll
  public static void blockHoundSetup() {
    BlockHound.install();
  }

  @Test
  @DisplayName("Check working of block hound")
  void blockHoundWorks() {
    FutureTask<?> task =
        new FutureTask<>(
            () -> {
              Thread.sleep(0);
              return 0;
            });
    Schedulers.parallel().schedule(task);
    ExecutionException ex =
        assertThrows(ExecutionException.class,
            () -> task.get(10, TimeUnit.SECONDS));
    assertTrue(ex.getCause() instanceof BlockingOperationError);
  }

  @Test
  @DisplayName("Get one anime by id")
  void getOne() {
    Mockito.when(animeRepository.findById(Mockito.anyInt())).thenReturn(Mono.just(anime));

    webTestClient
        .get()
        .uri("/animes/{id}", 1)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(Anime.class)
        .isEqualTo(anime);
  }

  @Test
  @DisplayName("Anime not found by id")
  void notFoundById() {
    Mockito.when(animeRepository.findById(Mockito.anyInt())).thenReturn(Mono.empty());

    webTestClient
        .get()
        .uri("/animes/{id}", 11)
        .exchange()
        .expectStatus()
        .isNotFound();
  }
}