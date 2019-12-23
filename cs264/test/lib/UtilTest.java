import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilTest {

  @Test
  public void testGetDigit() {
    assertEquals(0, Util.getDigit(12345L, 6));
    assertEquals(0, Util.getDigit(12345L, 5));
    assertEquals(1, Util.getDigit(12345L, 4));
    assertEquals(2, Util.getDigit(12345L, 3));
    assertEquals(3, Util.getDigit(12345L, 2));
    assertEquals(4, Util.getDigit(12345L, 1));
    assertEquals(5, Util.getDigit(12345L, 0));
    assertEquals(5, Util.getDigit(12345L, -1));

    assertEquals(0, Util.getDigit(0L, 4));
    assertEquals(0, Util.getDigit(0L, 1));
    assertEquals(0, Util.getDigit(0L, 0));
  }

  @Test
  public void testGetSign() {
    assertEquals(1, Util.getSign(3));
    assertEquals(1, Util.getSign(3442));
    assertEquals(0, Util.getSign(0));
    assertEquals(0, Util.getSign(-0));
    assertEquals(-1, Util.getSign(-1));
    assertEquals(-1, Util.getSign(-342));
  }
}
