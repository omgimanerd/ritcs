/**
 * KVTuple: A Java implementation of a generic Tuple which can store a key and a
 * value. It is read-only.
 *
 * @param <K> The type of the key
 * @param <V> The type of the value
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class KVTuple<K, V> {

  private K key;
  private V value;

  /**
   * Create a new KVTuple
   *
   * @param newkey The key of the tuple
   * @param newvalue The value of the tuple
   */
  public KVTuple(K newkey, V newvalue) {
    this.key = newkey;
    this.value = newvalue;
  }

  /**
   * Get the key from this tuple
   *
   * @return The key
   */
  public K getKey() {
    return this.key;
  }

  /**
   * Get the value from this tuple
   *
   * @return The value
   */
  public V getValue() {
    return this.value;
  }
}


