package com.tenghe.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesUtil {
  private static final Logger LOGGER = Logger.getLogger(PropertiesUtil.class);

  private PropertiesUtil() {
  }

  public static Properties initProperties(String path) {
    Properties prop = new Properties();
    InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(path);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
      prop.load(br);
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
    return prop;
  }
}
