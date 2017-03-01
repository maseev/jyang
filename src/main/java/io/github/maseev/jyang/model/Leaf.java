package io.github.maseev.jyang.model;

import io.github.maseev.jyang.util.YANGTypeMatcher;

public class Leaf extends Node {

  private final YANGType type;

  public Leaf(final String name, final Class<?> clazz) {
    super(name);
    type = YANGTypeMatcher.match(clazz);
  }

  public YANGType getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Leaf)) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    Leaf leaf = (Leaf) o;

    return type == leaf.type;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();

    result = 31 * result + type.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return "Leaf{" +
      "type=" + type +
      "} " + super.toString();
  }
}
