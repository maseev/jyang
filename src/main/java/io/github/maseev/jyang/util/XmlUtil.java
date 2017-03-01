package io.github.maseev.jyang.util;

import java.lang.reflect.Field;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public final class XmlUtil {

  private static final String DEFAULT_NAME = "##default";

  private XmlUtil() {
  }

  public static String getName(final Class<?> clazz) {
    XmlRootElement xmlRootElement = clazz.getAnnotation(XmlRootElement.class);
    String rootElementName = xmlRootElement == null ? DEFAULT_NAME : xmlRootElement.name();

    return getName(clazz.getSimpleName(), rootElementName);
  }

  public static String getName(final Field field) {
    XmlElement xmlElement = field.getAnnotation(XmlElement.class);
    String rootElementName = xmlElement == null ? DEFAULT_NAME : xmlElement.name();

    return getName(field.getName(), rootElementName);
  }

  private static String getName(final String elementName, final String defaultName) {
    if (DEFAULT_NAME.equals(defaultName)) {
      return elementName;
    }

    return defaultName;
  }
}
