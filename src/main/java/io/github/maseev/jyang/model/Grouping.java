package io.github.maseev.jyang.model;

import java.util.ArrayList;
import java.util.List;

public class Grouping extends Entity {

  private final List<Leaf> leafs;

  private final List<Container> containers;

  private final List<YANGCollection> lists;

  private final List<YANGMap> maps;

  public Grouping(final String name) {
    this(name, "");
  }

  public Grouping(final String name, final String description) {
    super(name, description);
    leafs = new ArrayList<>();
    containers = new ArrayList<>();
    lists = new ArrayList<>();
    maps = new ArrayList<>();
  }

  public List<Leaf> getLeafs() {
    return leafs;
  }

  public List<Container> getContainers() {
    return containers;
  }

  public List<YANGCollection> getLists() {
    return lists;
  }

  public List<YANGMap> getMaps() {
    return maps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Grouping)) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    Grouping grouping = (Grouping) o;

    return leafs.equals(grouping.leafs)
      && containers.equals(grouping.containers)
      && lists.equals(grouping.lists)
      && maps.equals(grouping.maps);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();

    result = 31 * result + leafs.hashCode();
    result = 31 * result + containers.hashCode();
    result = 31 * result + lists.hashCode();
    result = 31 * result + maps.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return "Grouping{" +
      "leafs=" + leafs +
      ", containers=" + containers +
      ", lists=" + lists +
      ", maps=" + maps +
      "} " + super.toString();
  }
}
