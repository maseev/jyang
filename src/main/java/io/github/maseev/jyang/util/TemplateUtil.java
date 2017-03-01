package io.github.maseev.jyang.util;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.StringWriter;

import io.github.maseev.jyang.model.Module;

public final class TemplateUtil {

  private TemplateUtil() {
  }

  public static String transform(final Module module, final String template) {
    MustacheFactory mustacheFactory = new DefaultMustacheFactory();
    StringWriter stringWriter = new StringWriter();
    Mustache mustache = mustacheFactory.compile(template);

    mustache.execute(stringWriter, module);

    return stringWriter.toString();
  }
}
