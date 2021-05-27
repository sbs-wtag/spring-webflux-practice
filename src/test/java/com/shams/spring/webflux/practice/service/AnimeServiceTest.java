package com.shams.spring.webflux.practice.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.shams.spring.webflux.practice.entity.Anime;
import com.shams.spring.webflux.practice.exception.NotFoundException;
import com.shams.spring.webflux.practice.repository.AnimeRepository;
import io.netty.util.concurrent.BlockingOperationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {

  @InjectMocks
  private AnimeService animeService;

  @Mock
  private AnimeRepository animeRepository;

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
  @DisplayName("Find anime by id")
  void findById() {
    Mockito.when(animeRepository.findById(Mockito.anyInt())).thenReturn(Mono.just(anime));

    StepVerifier.create(animeService.findById(1))
        .expectSubscription()
        .expectNext(anime)
        .verifyComplete();
  }

  @Test
  @DisplayName("Anime not found by id")
  void notFoundById() {
    Mockito.when(animeRepository.findById(Mockito.anyInt())).thenReturn(Mono.empty());

    StepVerifier.create(animeService.findById(11))
        .expectSubscription()
        .expectError(NotFoundException.class)
        .verify();
  }
}