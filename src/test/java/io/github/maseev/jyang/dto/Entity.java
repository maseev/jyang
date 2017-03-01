package io.github.maseev.jyang.dto;

import java.util.List;

import io.github.maseev.jyang.annotation.MapsTo;

@MapsTo(DtoEntity.class)
public class Entity {

  private final List<Integer> numbers;

  public Entity(List<Integer> numbers) {
    this.numbers = numbers;
  }

  public List<Integer> getNumbers() {
    return numbers;
  }
}