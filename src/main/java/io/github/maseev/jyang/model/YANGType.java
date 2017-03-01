package io.github.maseev.jyang.model;

public enum YANGType {

  BOOLEAN("boolean"),
  BYTE("int8"),
  CHAR("char"),
  SHORT("int16"),
  INT("int32"),
  FLOAT("float"),
  LONG("int64"),
  DOUBLE("double"),
  STRING("string"),
  DATE("timestamp");

  private final String type;

  YANGType(final String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return type;
  }
}