package io.github.maseev.jyang.util;

import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;

import io.github.maseev.jyang.model.YANGType;

public final class YANGTypeMatcher {

  private static final Map<Class<?>, YANGType> map;

  static {
    map = new IdentityHashMap<>();
    map.put(boolean.class, YANGType.BOOLEAN);
    map.put(Boolean.class, YANGType.BOOLEAN);
    map.put(double.class, YANGType.DOUBLE);
    map.put(Double.class, YANGType.DOUBLE);
    map.put(byte.class, YANGType.BYTE);
    map.put(Byte.class, YANGType.BYTE);
    map.put(char.class, YANGType.CHAR);
    map.put(Character.class, YANGType.CHAR);
    map.put(short.class, YANGType.SHORT);
    map.put(Short.class, YANGType.SHORT);
    map.put(int.class, YANGType.INT);
    map.put(Integer.class, YANGType.INT);
    map.put(float.class, YANGType.FLOAT);
    map.put(Float.class, YANGType.FLOAT);
    map.put(long.class, YANGType.LONG);
    map.put(Long.class, YANGType.LONG);
    map.put(String.class, YANGType.STRING);
    map.put(Date.class, YANGType.DATE);
  }

  private YANGTypeMatcher() {
  }

  public static YANGType match(final Class<?> type) {
    YANGType yangType = map.get(type);

    if (yangType == null) {
      throw new IllegalArgumentException(String.format("%s type isn't supported.", type));
    }

    return yangType;
  }
}
