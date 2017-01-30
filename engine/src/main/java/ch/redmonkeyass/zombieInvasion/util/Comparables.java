package ch.redmonkeyass.zombieInvasion.util;

import ch.redmonkeyass.zombieInvasion.worldmap.Node;
import com.badlogic.gdx.math.Vector2;

import java.util.Comparator;

public class Comparables {
  public static class SortSmallestY implements Comparator<Vector2> {

    public int compare(final Vector2 a, final Vector2 b) {
      if (a.y < b.y) {
        return -1;
      } else if (a.y > b.y) {
        return 1;
      } else {
        return 0;
      }
    }
  }
  public static class SortSmallestX implements Comparator<Vector2> {

    public int compare(final Vector2 a, final Vector2 b) {
      if (a.x < b.x) {
        return -1;
      } else if (a.x > b.x) {
        return 1;
      } else {
        return 0;
      }
    }
  }
  public static class SortXThenYForNodes implements Comparator<Node> {

    public int compare(final Node a, final Node b) {
      int xdiff = new Integer(a.x).compareTo(b.x);
      if (xdiff != 0) return xdiff;
      else return new Integer(a.y).compareTo(b.y);
    }
  }
}
