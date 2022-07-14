package com.github.hukojiayc.sme.graph.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceUtils {

  public static Optional<String> getFileAsString(String fileName) {
    ClassLoader classLoader = ResourceUtils.class.getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream(fileName);
    if (inputStream == null) {
      log.error("File {} not found", fileName);
      return Optional.empty();
    }
    try {
      return Optional.of(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

}
