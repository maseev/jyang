package io.github.maseev.jyang.dto;

public class PlainEntity {

  private final int[] data;

  public PlainEntity(int[] data) {
    this.data = data;
  }

  public int[] getData() {
    return data;
  }
}
