package com.github.hukojiayc.sme.graph;

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
}
