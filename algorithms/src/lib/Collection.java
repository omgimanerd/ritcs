public interface Collection<T> {

  /**
   * Ensure that the collection contains the specified element.
   *
   * @param element The element to potentially add to the collection
   * @return True if the state of the collection was modified
   */
  boolean add(T element);

  /**
   * Removes all of the elements from this collection.
   */
  void clear();

  /**
   * Returns true if the collection contains the specified element.
   *
   * @param o The element to check for
   * @return True if that element is in the collection
   */
  boolean contains(T o);

  /**
   * Returns true if this collection contains no elements.
   *
   * @return True if this collection contains no elements
   */
  boolean isEmpty();

  /**
   * Removes a single instance of the specified element from this collection, if
   * it is present.
   *
   * @param o The object to potentially remove
   * @return True if the state of the collection was modified
   */
  boolean remove(T o);

  /**
   * Return the number of elements in this collection.
   *
   * @return The number of elements in this collection
   */
  int size();

  /**
   * Returns an array containing all of the elements in this collection.
   *
   * @return an array containing all of the elements in this collection
   */
  Object[] toArray();
}
