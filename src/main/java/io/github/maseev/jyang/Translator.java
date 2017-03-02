package io.github.maseev.jyang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.maseev.jyang.annotation.MapsTo;
import io.github.maseev.jyang.annotation.NetconfProcedure;
import io.github.maseev.jyang.model.Container;
import io.github.maseev.jyang.model.Grouping;
import io.github.maseev.jyang.model.Leaf;
import io.github.maseev.jyang.model.RPC;
import io.github.maseev.jyang.model.YANGCollection;
import io.github.maseev.jyang.model.YANGMap;
import io.github.maseev.jyang.util.AtomicTypeUtil;
import io.github.maseev.jyang.util.NetconfUtil;
import io.github.maseev.jyang.util.XmlUtil;

public class Translator {

  public static class Pair {

    private final List<Grouping> groupings;

    private final List<RPC> rpcs;

    public Pair(List<Grouping> groupings, List<RPC> rpcs) {
      this.groupings = groupings;
      this.rpcs = rpcs;
    }

    public List<Grouping> getGroupings() {
      return groupings;
    }

    public List<RPC> getRpcs() {
      return rpcs;
    }
  }

  private final Map<String, Class<?>> endpoints;

  private final Map<String, Method> procedures;

  private final Map<String, Grouping> groupings;

  public Translator() {
    endpoints = new HashMap<>();
    procedures = new HashMap<>();
    groupings = new HashMap<>();
  }

  public Pair translateEndpoint(final Class<?> endpoint) {
    validate(endpoint);

    Set<Method> netconfProcedures = getProcedures(endpoint);
    List<RPC> rpcs = new ArrayList<>(netconfProcedures.size());
    List<Grouping> rpcGroupings = new ArrayList<>();

    for (Method method : netconfProcedures) {
      Type returnType = method.getGenericReturnType();
      Type[] methodParameters = method.getGenericParameterTypes();
      Grouping output = processTypes(returnType);
      Grouping input = processTypes(methodParameters);

      RPC rpc =
        new RPC(NetconfUtil.getName(method), NetconfUtil.getDescription(method), input, output);

      rpcs.add(rpc);

      List<Type> types = new ArrayList<>(methodParameters.length);
      types.addAll(Arrays.asList(methodParameters));
      types.add(returnType);

      for (Class<?> clazz : getEntityClasses(types)) {
        String name = XmlUtil.getName(clazz);

        if (!groupings.containsKey(name)) {
          Grouping grouping = translateGrouping(clazz);

          rpcGroupings.add(grouping);
          groupings.put(name, grouping);
        }
      }
    }

    return new Pair(rpcGroupings, rpcs);
  }

  public Grouping translateGrouping(final Class<?> clazz) {
    if (clazz.isAnnotationPresent(MapsTo.class)) {
      MapsTo annotation = clazz.getDeclaredAnnotation(MapsTo.class);

      return translateGrouping(annotation.value());
    }

    Grouping grouping = new Grouping(XmlUtil.getName(clazz));

    for (Field field : clazz.getDeclaredFields()) {
      processType(field.getGenericType(), XmlUtil.getName(field), grouping);
    }

    return grouping;
  }

  private void validate(Class<?> endpoint) {
    String endpointName = NetconfUtil.getName(endpoint);

    if (endpoints.containsKey(endpointName)) {
      Class<?> existentEndpoint = endpoints.get(endpointName);

      throw new IllegalArgumentException(String.format("%s endpoint class with %s name is " +
        "already defined in %s class", endpoint, endpointName, existentEndpoint));
    }

    endpoints.put(endpointName, endpoint);
  }

  private static List<Class<?>> getEntityClasses(List<Type> types) {
    List<Class<?>> classes = new ArrayList<>(types.size());

    for (Type type : types) {
      Class<?> clazz = type instanceof ParameterizedType ?
        (Class<?>) ((ParameterizedType) type).getRawType() : (Class<?>) type;

      if (Collection.class.isAssignableFrom(clazz)) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

        classes.addAll(getEntityClasses(Collections.singletonList(genericClass)));
      } else if (Map.class.isAssignableFrom(clazz)) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<?> keyClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        Class<?> valueClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];

        classes.addAll(getEntityClasses(Arrays.asList(keyClass, valueClass)));
      } else if (clazz.isArray()) {
        classes.addAll(getEntityClasses(Collections.singletonList(clazz.getComponentType())));
      } else if (!AtomicTypeUtil.isAtomic(clazz)) {
        classes.add(clazz);
      }
    }

    return classes;
  }

  private Set<Method> getProcedures(final Class<?> endpoint) {
    Set<Method> netconfProcedures = new HashSet<>();

    for (Method method : endpoint.getDeclaredMethods()) {
      if (method.isAnnotationPresent(NetconfProcedure.class)) {
        validate(method);

        netconfProcedures.add(method);
      }
    }

    return netconfProcedures;
  }

  private void validate(Method method) {
    String procedureName = NetconfUtil.getName(method);

    if (procedures.containsKey(procedureName)) {
      Method existentProcedure = procedures.get(procedureName);

      throw new IllegalArgumentException(String.format("%s method with %s name is " +
        "already defined in %s method", method, procedureName, existentProcedure));
    }

    procedures.put(procedureName, method);
  }

  private static Grouping processTypes(final Type... types) {
    if (types.length == 0 || types.length == 1 && types[0] == void.class) {
      return null;
    }

    Grouping grouping = new Grouping("");

    for (Type type : types) {
      processType(type, null, grouping);
    }

    return grouping;
  }

  private static void processType(final Type type, final String name, final Grouping grouping) {
    Class<?> clazz = type instanceof ParameterizedType ?
      (Class<?>) ((ParameterizedType) type).getRawType() : (Class<?>) type;
    String entityName = name;

    if (name == null) {
      entityName = XmlUtil.getName(clazz);
    }

    if (isVoid(clazz)) {
      return;
    }

    if (AtomicTypeUtil.isAtomic(clazz)) {
      grouping.getLeafs().add(new Leaf(entityName, clazz));
    } else if (clazz.isArray()) {
      String arrayEntityName = clazz.getComponentType().getSimpleName() + "-array";
      grouping.getLists().add(new YANGCollection(arrayEntityName, clazz.getComponentType()));
    } else if (Collection.class.isAssignableFrom(clazz)) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Class<?> genericClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];

      grouping.getLists().add(new YANGCollection(entityName, genericClass));
    } else if (Map.class.isAssignableFrom(clazz)) {
      ParameterizedType parameterizedType = (ParameterizedType) type;
      Class<?> keyClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
      Class<?> valueClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];

      grouping.getMaps().add(new YANGMap(entityName, keyClass, valueClass));
    } else {
      grouping.getContainers().add(new Container(entityName, XmlUtil.getName(clazz)));
    }
  }

  private static boolean isVoid(final Class<?> clazz) {
    return clazz == void.class || clazz == Void.class;
  }
}
