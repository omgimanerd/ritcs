public interface List<T> extends Collection<T> {

  /**
   * Appends the specified element to the end of the list.
   *
   * @param element The element to potentially add to the collection
   */
  boolean add(T element);

  /**
   * Inserts the specified element at the specified position in the list.
   *
   * @param index Where to put the element
   * @param element The element to put in
   * @return True if the state of the list was modified
   */
  boolean add(int index, T element);

  /**
   * Get the element at the specified index.
   *
   * @param index The index of the element to get
   * @return The element at that index
   * @throws IndexOutOfBoundsException if the index is not an index into this
   * list
   */
  T get(int index);

  /**
   * Remove the element at the specified index.
   *
   * @param index The index of the element to remove
   * @return The value at that index
   * @throws IndexOutOfBoundsException if the index is not an index into this
   * list
   */
  T remove(int index);

  /**
   * Update the value at the specified index.
   *
   * @param index The location in the list to change
   * @param element The new value to set at that index
   * @return The value previously at that index
   * @throws IndexOutOfBoundsException if the index is not an index into this
   * list
   */
  T set(int index, T element);
}
