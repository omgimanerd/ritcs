/**
 * Fraction: A Java implementation of a Fraction class that encapsulates
 * fractional numbers.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class Fraction extends Number implements Comparable<Fraction> {

  public int numerator;
  public int denominator;

  public Fraction(int numerator, int denominator) {
    this.numerator = numerator;
    this.denominator = denominator;
  }

  public Fraction(int numerator) {
    this(numerator, 1);
  }

  public static Fraction add(Fraction f1, Fraction f2) {
    return f1.copy().add(f2);
  }

  public static Fraction div(Fraction f1, Fraction f2) {
    return f1.copy().div(f2);
  }

  public static int gcf(int a, int b) {
    if (b == 0) {
      return a;
    }
    return gcf(b, a % b);
  }

  public static int lcm(int a, int b) {
    return Math.abs(a * b) / gcf(a, b);
  }

  public static Fraction mult(Fraction f1, Fraction f2) {
    return f1.copy().mult(f2);
  }

  public static Fraction sub(Fraction f1, Fraction f2) {
    return f1.copy().sub(f2);
  }

  public Fraction add(Fraction other) {
    int lcm = lcm(denominator, other.denominator);
    int f1 = lcm / denominator;
    int f2 = lcm / other.denominator;
    numerator = (numerator * f1) + (other.numerator * f2);
    denominator *= f1;
    return this.reduce();
  }

  public Fraction add(int addend) {
    numerator += denominator * addend;
    return this.reduce();
  }

  public byte byteValue() {
    return 0;
  }

  public int compareTo(Fraction other) {
    return Fraction.sub(this, other).numerator;
  }

  public Fraction copy() {
    return new Fraction(numerator, denominator);
  }

  public Fraction div(Fraction other) {
    numerator *= other.denominator;
    denominator *= other.numerator;
    return this.reduce();
  }

  public Fraction div(int divisor) {
    denominator *= divisor;
    return this.reduce();
  }

  public double doubleValue() {
    return ((double) this.numerator) / ((double) this.denominator);
  }

  @Override
  public boolean equals(Object other) {
    Fraction o = (Fraction) other;
    return numerator == o.numerator && denominator == o.denominator;
  }

  public float floatValue() {
    return ((float) this.numerator) / ((float) this.denominator);
  }

  public int intValue() {
    return this.numerator / this.denominator;
  }

  public long longValue() {
    return (long) (this.numerator / this.denominator);
  }

  public Fraction mult(Fraction other) {
    numerator *= other.numerator;
    denominator *= other.denominator;
    return this.reduce();
  }

  public Fraction mult(int factor) {
    numerator *= factor;
    return this.reduce();
  }

  public Fraction neg() {
    numerator *= -1;
    return this;
  }

  public Fraction reduce() {
    int f = gcf(numerator, denominator);
    numerator /= f;
    denominator /= f;
    if (denominator < 0) {
      numerator *= -1;
      denominator *= -1;
    }
    return this;
  }

  public short shortValue() {
    return (short) (this.numerator / this.denominator);
  }

  public Fraction sub(Fraction other) {
    int lcm = lcm(denominator, other.denominator);
    int f1 = lcm / denominator;
    int f2 = lcm / other.denominator;
    numerator = (numerator * f1) - (other.numerator * f2);
    denominator *= f1;
    return this.reduce();
  }

  public Fraction sub(int subtrahend) {
    numerator -= denominator * subtrahend;
    return this.reduce();
  }

  public String toString() {
    if (denominator == 1) {
      return "" + numerator;
    }
    return numerator + "/" + denominator;
  }
}
