import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FractionTest {

  @Test
  public void testAddFrac() {
    Fraction addend1 = new Fraction(5, 12);
    Fraction addend2 = new Fraction(31, 64);
    Fraction sum = new Fraction(173, 192);
    assertTrue(
        String.format("%s and %s did not sum to %s.", addend1, addend2, sum),
        addend1.add(addend2).compareTo(sum) == 0
    );
  }

  @Test
  public void testAddInt() {
    Fraction addend = new Fraction(5, 12);
    Fraction sum = new Fraction(17, 12);
    assertTrue(
        String.format("%s and %d did not sum to %s.", addend, 1, sum),
        addend.add(1).compareTo(sum) == 0
    );
  }

  @Test
  public void testCompareToEqual() {
    Fraction testFrac = new Fraction(1, 2);
    Fraction equalFrac = new Fraction(1, 2);
    assertTrue(
        String.format(
            "%s was incorrectly determined to be unequal to %s.",
            testFrac,
            equalFrac
        ),
        testFrac.compareTo(equalFrac) == 0
    );
  }

  @Test
  public void testCompareToEqualUnreduced() {
    Fraction testFrac = new Fraction(1, 2);
    Fraction equalFrac = new Fraction(2, 4);
    assertTrue(
        String.format(
            "%s was incorrectly determined to be unequal to %s.",
            testFrac,
            equalFrac
        ),
        testFrac.compareTo(equalFrac) == 0
    );
  }

  @Test
  public void testCompareToGreater() {
    Fraction testFrac = new Fraction(1, 2);
    Fraction greaterFrac = new Fraction(2, 3);
    assertTrue(
        String.format(
            "%s was incorrectly determined to not be greater than %s.",
            testFrac,
            greaterFrac
        ),
        testFrac.compareTo(greaterFrac) < 0
    );
  }

  @Test
  public void testCompareToLess() {
    Fraction testFrac = new Fraction(1, 2);
    Fraction lessFrac = new Fraction(1, 3);
    assertTrue(
        String.format(
            "%s was incorrectly determined to not be less than %s.",
            testFrac,
            lessFrac
        ),
        testFrac.compareTo(lessFrac) > 0
    );
  }

  @Test
  public void testCompareToSlightlyGreater() {
    Fraction testFrac = new Fraction(5000, 10000);
    Fraction greaterFrac = new Fraction(5001, 10000);
    assertTrue(
        String.format(
            "%s was incorrectly determined to not be greater than %s.",
            testFrac,
            greaterFrac
        ),
        testFrac.compareTo(greaterFrac) < 0
    );
  }

  @Test
  public void testCompareToSlightlyLess() {
    Fraction testFrac = new Fraction(5000, 10000);
    Fraction lessFrac = new Fraction(4999, 10000);
    assertTrue(
        String.format(
            "%s was incorrectly determined to not be less than %s.",
            testFrac,
            lessFrac
        ),
        testFrac.compareTo(lessFrac) > 0
    );
  }

  @Test
  public void testCopy() {
    Fraction testFrac = new Fraction(7, 22);
    Fraction copyFrac = testFrac.copy();
    assertTrue(
        "Fraction.copy() did not produce a Fraction with the same numerator "
            + "and denominator.",
        testFrac.compareTo(copyFrac) == 0
    );
  }

  @Test
  public void testDivFrac() {
    Fraction dividend = new Fraction(1, 4);
    Fraction divisor = new Fraction(4, 1);
    Fraction quotient = new Fraction(1, 16);
    assertTrue(
        String.format("%s and %s did not divide to %s.", dividend, divisor,
            quotient),
        dividend.div(divisor).compareTo(quotient) == 0
    );
  }

  @Test
  public void testDivInt() {
    Fraction dividend = new Fraction(1, 4);
    Fraction quotient = new Fraction(1, 16);
    assertTrue(
        String.format("%s and %s did not divide to %s.", dividend, 4,
            quotient),
        dividend.div(4).compareTo(quotient) == 0
    );
  }

  @Test
  public void testEquals() {
    Fraction f1 = new Fraction(22, 2);
    Fraction f2 = new Fraction(22, 2);
    assertEquals(f1, f2);
  }

  @Test
  public void testMultFrac() {
    Fraction multiplicand1 = new Fraction(4, 9);
    Fraction multiplicand2 = new Fraction(5, 11);
    Fraction product = new Fraction(20, 99);
    assertTrue(
        String.format("%s and %s did not multiply to %s.", multiplicand1,
            multiplicand2, product),
        multiplicand1.mult(multiplicand2).compareTo(product) == 0
    );
  }

  @Test
  public void testMultInt() {
    Fraction multiplicand = new Fraction(4, 9);
    Fraction product = new Fraction(8, 9);
    assertTrue(
        String.format("%s and %d did not multiply to %s.", multiplicand, 2,
            product),
        multiplicand.mult(2).compareTo(product) == 0
    );
  }

  @Test
  public void testNeg() {
    Fraction testFrac = new Fraction(4, 9);
    assertEquals(-4, testFrac.neg().numerator);
  }

  @Test
  public void testNumeratorConstructor() {
    Fraction testFrac = new Fraction(10);
    assertEquals(10, testFrac.numerator);
    assertEquals(1, testFrac.denominator);
  }

  @Test
  public void testNumeratorDenominatorConstructor() {
    Fraction testFrac = new Fraction(5, 2);
    assertEquals(5, testFrac.numerator);
    assertEquals(2, testFrac.denominator);
  }

  @Test
  public void testReduce() {
    Fraction testFrac = new Fraction(256, 2);
    Fraction reducedFrac = new Fraction(128, 1);
    assertTrue(
        String.format("%s did not reduce to %s.", testFrac, reducedFrac),
        testFrac.reduce().compareTo(reducedFrac) == 0
    );
  }

  @Test
  public void testStaticAdd() {
    Fraction addend1 = new Fraction(5, 12);
    Fraction addend2 = new Fraction(31, 64);
    assertEquals(addend1.copy().add(addend2), Fraction.add(addend1, addend2));
  }

  @Test
  public void testStaticDiv() {
    Fraction dividend = new Fraction(1, 4);
    Fraction divisor = new Fraction(4, 1);
    assertEquals(dividend.copy().div(divisor), Fraction.div(dividend, divisor));
  }

  @Test
  public void testStaticGcfNonzero() {
    assertEquals(2, Fraction.gcf(2, 4));
  }

  @Test
  public void testStaticGcfZero() {
    assertEquals(7, Fraction.gcf(7, 0));
  }

  @Test
  public void testStaticLcm() {
    assertEquals(10, Fraction.lcm(5, 10));
  }

  @Test
  public void testStaticMult() {
    Fraction multiplicand1 = new Fraction(4, 9);
    Fraction multiplicand2 = new Fraction(5, 11);
    assertEquals(multiplicand1.copy().mult(multiplicand2),
        Fraction.mult(multiplicand1,
            multiplicand2));
  }

  @Test
  public void testStaticSub() {
    Fraction minuend = new Fraction(5, 12);
    Fraction subtrahend = new Fraction(31, 64);
    assertEquals(minuend.copy().sub(subtrahend),
        Fraction.sub(minuend, subtrahend));
  }

  @Test
  public void testSubFrac() {
    Fraction minuend = new Fraction(5, 12);
    Fraction subtrahend = new Fraction(31, 64);
    Fraction difference = new Fraction(-13, 192);
    assertTrue(
        String.format("%s and %s did not subtract to %s.", minuend, subtrahend,
            difference),
        minuend.sub(subtrahend).compareTo(difference) == 0
    );
  }

  @Test
  public void testSubInt() {
    Fraction minuend = new Fraction(5, 12);
    Fraction difference = new Fraction(-7, 12);
    assertTrue(
        String.format("%s and %d did not subtract to %s.", minuend, 1,
            difference),
        minuend.sub(1).compareTo(difference) == 0
    );
  }

  @Test
  public void testToString() {
    Fraction testFrac = new Fraction(2, 3);
    assertEquals("2/3", testFrac.toString());
  }
}
