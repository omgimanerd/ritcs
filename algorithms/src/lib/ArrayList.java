/**
 * ArrayList: A Java reimplementation of java.util.ArrayList.
 *
 * @param <T> The type of the values in the list.
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class ArrayList<T> implements List<T> {

  private Object[] list;
  private int size;

  /**
   * Constructor for our custom ArrayList.
   */
  public ArrayList() {
    list = new Object[10];
    size = 0;
  }

  /**
   * Constructor for our custom ArrayList that creates the internal array with a
   * specified size, in order to prevent resizing when inserting a known number
   * of elements
   *
   * @param size The size to make the internal array
   */
  public ArrayList(int size) {
    list = new Object[size];
    this.size = 0;
  }

  @SuppressWarnings("unchecked")
  public ArrayList(T... elements) {
    list = elements;
    this.size = elements.length;
    resize();
  }

  public ArrayList(ArrayList<T> other) {
    list = new Object[other.list.length];
    ArrayList.copy(other, 0, this, 0, other.size);
  }

  /**
   * Copies <pre>size</pre> elements from <pre>src</pre>, starting at
   * <pre>srcPos</pre>, to <pre>dst</pre>, starting at <pre>dstPos</pre>
   *
   * @param src The ArrayList to copy from
   * @param srcPos The index into <pre>src</pre> from which to begin copying
   * @param dst The ArrayList to copy to
   * @param dstPos The index into <pre>dst</pre> at which to begin inserting
   * @param size The number of elements to copy
   */
  public static void copy(ArrayList src, int srcPos, ArrayList dst, int dstPos,
      int size) {
    System.arraycopy(src.list, srcPos, dst.list, dstPos, size);
    dst.size = size;
  }

  /**
   * Used internally to check if an index parameter is within the valid bounds
   * of the list. This method will throw an IndexOutOfBoundsException if the
   * given index is not a valid index.
   *
   * @param index The index to check.
   */
  private void checkIndex(int index) {
    if (index >= size) {
      throw new ArrayIndexOutOfBoundsException();
    }
  }

  /**
   * Resizes the internal array containing the elements in this ArrayList,
   * called internally when we've inserted enough elements to fill the internal
   * array and we need more space.
   */
  private void resize() {
    Object[] resized = new Object[list.length * 2];
    System.arraycopy(list, 0, resized, 0, size);
    list = resized;
  }

  /**
   * Adds a value to the ArrayList.
   *
   * @param value The value to add to the ArrayList
   */
  public boolean add(T value) {
    list[size++] = value;
    if (size == list.length) {
      resize();
    }
    return true;
  }

  /**
   * @inheritDoc
   */
  public boolean add(int index, T value) {
    checkIndex(index);
    for (int i = size - 1; i >= index; --i) {
      list[i + 1] = list[i];
    }
    list[index] = value;
    if (++size == list.length) {
      resize();
    }
    return true;
  }

  /**
   * @inheritDoc
   */
  public void clear() {
    list = new Object[10];
    size = 0;
  }

  /**
   * @inheritDoc
   */
  public boolean contains(T o) {
    for (int i = 0; i < size; i++) {
      if (list[i].equals(o)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @inheritDoc
   */
  public boolean equals(Object o) {
    if (o instanceof ArrayList) {
      ArrayList other = (ArrayList) o;
      if (other.size == this.size) {
        for (int i = 0; i < size; i++) {
          if (!(other.list[i] == this.list[i])) {
            return false;
          }
        }
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  /**
   * Gets an element from the ArrayList.
   *
   * @param index The index of the element to fetch
   * @return The element from the ArrayList
   */
  @SuppressWarnings("unchecked")
  public T get(int index) {
    checkIndex(index);
    return (T) list[index];
  }

  /**
   * @inheritDoc
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * @inheritDoc
   */
  @SuppressWarnings("unchecked")
  public T remove(int index) {
    checkIndex(index);
    T value = (T) list[index];
    System.arraycopy(list, index + 1, list, index, size - (index + 1));
    size--;
    return value;
  }

  /**
   * Removes the first occurrence of a specific object stored inside the
   * ArrayList.
   *
   * @param o The object to remove from this ArrayList
   * @return True if the state of the list was modified
   */
  public boolean remove(T o) {
    for (int i = 0; i < size; ++i) {
      if (get(i).equals(o)) {
        remove(i);
        return true;
      }
    }
    return false;
  }

  /**
   * @inheritDoc
   */
  @SuppressWarnings("unchecked")
  public T set(int index, T value) {
    checkIndex(index);
    T oldValue = (T) list[index];
    list[index] = value;
    return oldValue;
  }

  /**
   * @inheritDoc
   */
  public int size() {
    return size;
  }

  /**
   * @inheritDoc
   */
  public Object[] toArray() {
    Object[] retval = new Object[this.size];
    System.arraycopy(this.list, 0, retval, 0, this.size);
    return retval;
  }

  /**
   * Obtain a string representation of this ArrayList
   *
   * @return A string representation of this ArrayList
   */
  public String toString() {
    StringBuilder result = new StringBuilder("[ ");
    for (int i = 0; i < size; i++) {
      result.append(get(i));
      result.append(" ");
    }
    result.append("]");
    return result.toString();
  }

}
