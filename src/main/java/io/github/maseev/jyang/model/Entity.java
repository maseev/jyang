package io.github.maseev.jyang.model;

public abstract class Entity {

  private final String name;

  private final String description;

  protected Entity(final String name) {
    this(name, "");
  }

  protected Entity(final String name, final String description) {
    this.name = name;
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Entity)) {
      return false;
    }

    Entity entity = (Entity) o;

    return name.equals(entity.name) && description.equals(entity.description);
  }

  @Override
  public int hashCode() {
    int result = name.hashCode();

    result = 31 * result + description.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return "Entity{" +
      "name='" + name + '\'' +
      ", description='" + description + '\'' +
      '}';
  }
}
