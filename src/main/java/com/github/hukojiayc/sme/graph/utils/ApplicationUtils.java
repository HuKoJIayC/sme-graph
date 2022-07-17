package com.github.hukojiayc.sme.graph.utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Optional;

public class ApplicationUtils {

  /**
   * Добавление значений аргументов в стоку с текстом, содержающую '{}' в местах, где требуется
   * вставка значений
   *
   * @param value Текст для форматирования
   * @param args  Список арументов для вставки
   * @return Форматированный текст
   */
  public static String addArgsToValue(String value, Object... args) {
    if (value == null || value.length() == 0) {
      return value;
    }
    for (Object arg : args) {
      if (!value.contains("{}")) {
        break;
      }
      value = value.replaceFirst("\\{}", arg.toString());
    }
    return value;
  }

  public static <T> Optional<String> getHomePath(Class<T> tClass) {
    String path;
    try {
      path = tClass.getProtectionDomain().getCodeSource().getLocation().toURI()
          .getPath();
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return Optional.empty();
    }
    if (path.contains("/target/classes/")) {
      path = path.replace("/target/classes/", "/app/");
    } else {
      path = new File(path).getParent() + "/";
    }
    return Optional.of(path);
  }
}
