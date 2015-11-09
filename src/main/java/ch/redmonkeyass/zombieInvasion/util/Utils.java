package ch.redmonkeyass.zombieInvasion.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class Utils {
  public static int[][] rotateArray90Deg(int[][] originalArray) {
    int[][] a = originalArray;
    int n = 4;
    int tmp = 0;
    for (int i = 0; i < n / 2; i++) {
      for (int j = i; j < n - i - 1; j++) {
        tmp = a[i][j];
        a[i][j] = a[j][n - i - 1];
        a[j][n - i - 1] = a[n - i - 1][n - j - 1];
        a[n - i - 1][n - j - 1] = a[n - j - 1][i];
        a[n - j - 1][i] = tmp;
      }
    }
    return a;
  }

  /**
   * Returns all file paths in a provided directory
   * 
   * @param path
   * @return allPaths
   */
  public static List<Path> getAllFilePathsInDirectory(Path path) {
    final List<Path> files = new ArrayList<>();
    try {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
          if (!attrs.isDirectory()) {
            files.add(file);
          }
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
    return files;
  }
}
