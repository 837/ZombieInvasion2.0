package ch.redmonkeyass.zombieInvasion.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple sorted list
 *
 */
public class SortedList<E> extends AbstractList<E> {
  /** The list of elements */
  private ArrayList<E> internalList = new ArrayList<E>();

  // Note that add(E e) in AbstractList is calling this one
  @Override
  public void add(int position, E e) {
    internalList.add(e);
    Collections.sort(internalList, null);
  }

  @Override
  public E get(int i) {
    return internalList.get(i);
  }

  @Override
  public int size() {
    return internalList.size();
  }

  public E pop() {
    E retrieve = internalList.get(0);
    internalList.remove(0);
    return retrieve;
  }
}
