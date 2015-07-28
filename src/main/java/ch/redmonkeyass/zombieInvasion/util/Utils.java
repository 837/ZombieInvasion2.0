package ch.redmonkeyass.zombieInvasion.util;

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
}
