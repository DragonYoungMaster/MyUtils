package com.tenghe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * author: teng.he
 * time: 16:51 2017/9/12
 * desc:
 */
public final class FileUtil {
  private FileUtil() {
  }

  public static void writeToOutputStream(File file, HttpServletResponse response) {
    try (FileInputStream inputStream = new FileInputStream(file);
         OutputStream out = response.getOutputStream()) {
      int i = 0;
      byte[] buffer = new byte[1024];
      while ((i = inputStream.read(buffer)) > 0) {
        //写到输出流中
        out.write(buffer, 0, i);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
