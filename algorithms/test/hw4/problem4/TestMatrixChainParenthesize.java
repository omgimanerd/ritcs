import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestMatrixChainParenthesize extends TestSolution {

  public static final String DATA_PATH = "data/hw4/problem4";

  private File inFile;
  private File ansFile;

  public TestMatrixChainParenthesize(File in, File ans) {
    this.inFile = in;
    this.ansFile = ans;
  }

  @Parameters
  public static Collection<File[]> data() {
    return data(DATA_PATH);
  }

  @Test
  public void runTests() throws IOException {
    String correct = null;
    String produced = null;
    try (
        FileInputStream fakeIn = new FileInputStream(this.inFile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream fakeOut = new PrintStream(baos)
    ) {
      correct = new Scanner(this.ansFile).useDelimiter("\\Z").next();
      correct += "\n";
      System.setIn(fakeIn);
      System.setOut(fakeOut);
      MatrixChainParenthesize.main(new String[1]);
      produced = baos.toString();
      assertEquals(correct, produced);
    }
  }
}
