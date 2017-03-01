package io.github.maseev.jyang.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public final class AtomicTypeUtil {

  private static final Set<Class<?>> atomicTypes;

  static {
    atomicTypes = new HashSet<>();
    atomicTypes.add(Date.class);
    atomicTypes.add(String.class);
    atomicTypes.add(Boolean.class);
    atomicTypes.add(Byte.class);
    atomicTypes.add(Character.class);
    atomicTypes.add(Short.class);
    atomicTypes.add(Integer.class);
    atomicTypes.add(Long.class);
    atomicTypes.add(Float.class);
    atomicTypes.add(Double.class);
    atomicTypes.add(Void.class);
  }

  private AtomicTypeUtil() {
  }

  public static boolean isAtomic(final Class<?> type) {
    return type.isPrimitive() || atomicTypes.contains(type);
  }
}
