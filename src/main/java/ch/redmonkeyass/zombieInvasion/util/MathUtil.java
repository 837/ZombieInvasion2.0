package ch.redmonkeyass.zombieInvasion.util;

import java.util.Random;

public class MathUtil {
  static public final float map(float value, float istart, float istop, float ostart, float ostop) {
    return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
  }

  /**
   * Generate random int
   * 
   * @param start included
   * @param end excluded
   * @return random int between start and end
   */
  static public final int randomInt(int start, int end) {
    return new Random().nextInt((end - start)) + start;
  }
}
