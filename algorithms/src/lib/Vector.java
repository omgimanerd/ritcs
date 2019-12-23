/**
 * Vector: A Java implementation of a Vector class to encapsulate a vector in
 * R^n and perform vector related operations.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class Vector {

  public Fraction[] values;
  private int dimensions;

  public Vector(int dimensions, Fraction... values) {
    if (values.length != dimensions || dimensions < 2) {
      throw new RuntimeException("Parameter mismatch!");
    }
    this.dimensions = dimensions;
    this.values = values;
  }

  public Vector(int dimensions, Integer... values) {
    if (values.length != dimensions || dimensions < 2) {
      throw new RuntimeException("Parameter mismatch!");
    }
    this.dimensions = dimensions;
    this.values = new Fraction[dimensions];
    for (int i = 0; i < dimensions; ++i) {
      this.values[i] = new Fraction(values[i]);
    }
  }

  public Vector(int dimensions) {
    this(dimensions, new Fraction[dimensions]);
  }

  public static Vector add(Vector v1, Vector v2) {
    return v1.copy().add(v2);
  }

  public static Vector cross(Vector v1, Vector v2) {
    return v1.copy().cross(v2);
  }

  public static Fraction dot(Vector v1, Vector v2) {
    return v1.copy().dot(v2);
  }

  public static Vector sub(Vector v1, Vector v2) {
    return v1.copy().sub(v2);
  }

  public Vector add(Vector other) {
    checkDimensions(other);
    for (int i = 0; i < dimensions; ++i) {
      values[i].add(other.values[i]);
    }
    return this;
  }

  public void checkDimensions(Vector other) {
    if (dimensions != other.getDimensions()) {
      throw new RuntimeException("Invalid dimensions!");
    }
  }

  public Vector copy() {
    return null;
  }

  public Vector cross(Vector other) {
    checkDimensions(other);
    if (dimensions != 3) {
      throw new RuntimeException("Cross product only works in 3D");
    }
    return new Vector(3,
        Fraction.sub(Fraction.mult(other.values[2], values[1]),
            Fraction.mult(other.values[1], values[2])),
        Fraction.sub(Fraction.mult(other.values[2], values[0]),
            Fraction.mult(other.values[0], values[2])).neg(),
        Fraction.sub(Fraction.mult(other.values[1], values[0]),
            Fraction.mult(other.values[0], values[1]))
    );
  }

  public Fraction dot(Vector other) {
    checkDimensions(other);
    Fraction product = new Fraction(0);
    for (int i = 0; i < dimensions; ++i) {
      product.add(Fraction.add(values[i], other.values[i]));
    }
    return product;
  }

  public int getDimensions() {
    return dimensions;
  }

  public Vector sub(Vector other) {
    checkDimensions(other);
    for (int i = 0; i < dimensions; ++i) {
      values[i].sub(other.values[i]);
    }
    return this;
  }

  public String toString() {
    StringBuilder result = new StringBuilder("<");
    result.append(this.values[0]);
    for (int i = 1; i < dimensions; i++) {
      result.append(", ");
      result.append(this.values[i]);
    }
    result.append(">");
    return result.toString();
  }
}
