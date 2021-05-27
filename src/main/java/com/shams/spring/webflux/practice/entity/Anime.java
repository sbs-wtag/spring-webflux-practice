package com.shams.spring.webflux.practice.entity;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@With
@Table("anime")
@NoArgsConstructor
@AllArgsConstructor
public class Anime {

  @Id
  private int id;

  @NotBlank(message = "Name can not be blank")
  private String name;
}
