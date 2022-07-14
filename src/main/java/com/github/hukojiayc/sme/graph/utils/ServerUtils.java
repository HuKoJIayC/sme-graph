package com.github.hukojiayc.sme.graph.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

public class ServerUtils {

  public static Optional<String> getRequestBody(InputStream inputStream) {
    InputStreamReader inputStreamReader = null;
    BufferedReader bufferedReader = null;
    StringBuilder stringBuilder = new StringBuilder(512); // todo get BUFFER_SIZE
    try {
      inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
      bufferedReader = new BufferedReader(inputStreamReader);
      int b;
      while ((b = bufferedReader.read()) != -1) {
        stringBuilder.append((char) b);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      Objects.requireNonNull(bufferedReader).close();
      Objects.requireNonNull(inputStreamReader).close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (stringBuilder.length() == 0) {
      return Optional.empty();
    }
    return Optional.of(decode(stringBuilder.toString()));
  }

  public static String decode(final String in) {
    String working = in;
    int index;
    index = working.indexOf("\\u");
    while (index > -1) {
      int length = working.length();
      if (index > (length - 6)) {
        break;
      }
      int numStart = index + 2;
      int numFinish = numStart + 4;
      String substring = working.substring(numStart, numFinish);
      int number = Integer.parseInt(substring, 16);
      String stringStart = working.substring(0, index);
      String stringEnd = working.substring(numFinish);
      working = stringStart + ((char) number) + stringEnd;
      index = working.indexOf("\\u");
    }
    return working;
  }
}
