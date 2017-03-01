package io.github.maseev.jyang.model;

import io.github.maseev.jyang.util.AtomicTypeUtil;
import io.github.maseev.jyang.util.XmlUtil;
import io.github.maseev.jyang.util.YANGTypeMatcher;

public class YANGMap extends Node {

  public static class Entry {

    private final String type;

    private final boolean primitive;

    public Entry(final String type, final boolean primitive) {
      this.type = type;
      this.primitive = primitive;
    }

    public String getType() {
      return type;
    }

    public boolean isPrimitive() {
      return primitive;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }

      if (!(o instanceof Entry)) {
        return false;
      }

      Entry entry = (Entry) o;

      return primitive == entry.primitive && type.equals(entry.type);
    }

    @Override
    public int hashCode() {
      int result = type.hashCode();

      result = 31 * result + (primitive ? 1 : 0);

      return result;
    }

    @Override
    public String toString() {
      return "Entry{" +
        "type='" + type + '\'' +
        ", primitive=" + primitive +
        '}';
    }
  }

  private final Entry key;

  private final Entry value;

  public YANGMap(final String name, Class<?> keyClass, Class<?> valueClass) {
    super(name);

    key = getEntry(keyClass);
    value = getEntry(valueClass);
  }

  public Entry getKey() {
    return key;
  }

  public Entry getValue() {
    return value;
  }

  private static Entry getEntry(final Class<?> clazz) {
    if (AtomicTypeUtil.isAtomic(clazz)) {
      return new Entry(YANGTypeMatcher.match(clazz).toString(), true);
    } else {
      return new Entry(XmlUtil.getName(clazz), false);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof YANGMap)) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    YANGMap yangMap = (YANGMap) o;

    return key.equals(yangMap.key) && value.equals(yangMap.value);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();

    result = 31 * result + key.hashCode();
    result = 31 * result + value.hashCode();

    return result;
  }

  @Override
  public String toString() {
    return "YANGMap{" +
      "key=" + key +
      ", value=" + value +
      "} " + super.toString();
  }
}
