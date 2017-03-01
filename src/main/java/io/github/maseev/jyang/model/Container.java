package io.github.maseev.jyang.model;

public class Container extends Node {

  private final String type;

  public Container(final String name, final String type) {
    super(name);
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Container)) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    Container container = (Container) o;

    return type.equals(container.type);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();

    result = 31 * result + type.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return "Container{" +
      "type='" + type + '\'' +
      "} " + super.toString();
  }
}
