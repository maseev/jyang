package io.github.maseev.jyang.util;

import java.lang.reflect.Method;

import io.github.maseev.jyang.annotation.NetconfEndpoint;
import io.github.maseev.jyang.annotation.NetconfProcedure;

public final class NetconfUtil {

  private NetconfUtil() {
  }

  public static String getName(final Class<?> endpoint) {
    if (!endpoint.isAnnotationPresent(NetconfEndpoint.class)) {
      throw new IllegalArgumentException(String.format("%s class doesn't have an %s annotation",
        endpoint, NetconfEndpoint.class));
    }

    NetconfEndpoint annotation = endpoint.getDeclaredAnnotation(NetconfEndpoint.class);

    if (annotation.value().isEmpty()) {
      return endpoint.getSimpleName();
    }

    return annotation.value();
  }

  public static String getName(final Method method) {
    if (!method.isAnnotationPresent(NetconfProcedure.class)) {
      throw new IllegalArgumentException(String.format("%s method doesn't have an %s annotation",
        method, NetconfProcedure.class));
    }

    NetconfProcedure annotation = method.getDeclaredAnnotation(NetconfProcedure.class);

    String endpointName = getName(method.getDeclaringClass());

    if (annotation.value().isEmpty()) {
      return endpointName + '.' + method.getName();
    }

    return endpointName + '.' + annotation.value();
  }

  public static String getDescription(final Method method) {
    if (!method.isAnnotationPresent(NetconfProcedure.class)) {
      throw new IllegalArgumentException(String.format("%s method doesn't have an %s annotation",
        method, NetconfProcedure.class));
    }

    NetconfProcedure annotation = method.getDeclaredAnnotation(NetconfProcedure.class);

    return annotation.description();
  }
}
