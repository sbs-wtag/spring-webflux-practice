package com.shams.spring.webflux.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class SpringWebfluxPracticeApplication {

  static {
    BlockHound.install();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringWebfluxPracticeApplication.class, args);
  }

}
