public class Interval<T extends Comparable<T>> {

  private T start;
  private T end;

  public Interval(T start, T end) {
    this.start = start;
    this.end = end;
  }

  public T getEnd() {
    return end;
  }

  public T getStart() {
    return start;
  }

  public boolean overlaps(Interval<T> other) {
    return ((
        other.start.compareTo(this.end) < 1 &&
            other.start.compareTo(this.start) > -1
    ) || (
        other.end.compareTo(this.end) < 1 &&
            other.end.compareTo(this.start) > -1
    ));
  }
}
