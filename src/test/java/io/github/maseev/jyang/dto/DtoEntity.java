package io.github.maseev.jyang.dto;

public class DtoEntity {

  private final int i;

  private final double d;

  public DtoEntity(int i, double d) {
    this.i = i;
    this.d = d;
  }

  public int getI() {
    return i;
  }

  public double getD() {
    return d;
  }
}
