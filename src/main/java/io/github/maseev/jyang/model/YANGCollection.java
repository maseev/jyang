package io.github.maseev.jyang.model;

import io.github.maseev.jyang.util.AtomicTypeUtil;
import io.github.maseev.jyang.util.XmlUtil;
import io.github.maseev.jyang.util.YANGTypeMatcher;

public class YANGCollection extends Node {

  private final boolean leafList;

  private final String type;

  public YANGCollection(String name, Class<?> genericType) {
    super(name);

    if (AtomicTypeUtil.isAtomic(genericType)) {
      type = YANGTypeMatcher.match(genericType).toString();
      leafList = true;
    } else {
      type = XmlUtil.getName(genericType);
      leafList = false;
    }
  }

  public boolean isLeafList() {
    return leafList;
  }

  public String getType() {
    return type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof YANGCollection)) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    YANGCollection that = (YANGCollection) o;

    return leafList == that.leafList && type.equals(that.type);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();

    result = 31 * result + (leafList ? 1 : 0);
    result = 31 * result + type.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return "YANGCollection{" +
      "leafList=" + leafList +
      ", type='" + type + '\'' +
      "} " + super.toString();
  }
}
