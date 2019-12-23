import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class TestSolution {

  public static Collection<File[]> data(String folderPath) {
    FilenameFilter inFilter = (dir, name) -> name.startsWith("input");
    FilenameFilter ansFilter = (dir, name) -> name.startsWith("answer");

    File testdir = new File(folderPath);
    File[] inFiles = testdir.listFiles(inFilter);
    File[] ansFiles = testdir.listFiles(ansFilter);
    if (inFiles == null || ansFiles == null) {
      throw new RuntimeException("No test cases found");
    }
    if (inFiles.length != ansFiles.length) {
      throw new RuntimeException("One of the test cases was missing an input "
          + "or output.");
    }
    Arrays.sort(inFiles, 0, inFiles.length);
    Arrays.sort(ansFiles, 0, ansFiles.length);
    ArrayList<File[]> testFiles = new ArrayList<>(inFiles.length);
    File[] tmp;
    for (int i = 0; i < inFiles.length; i++) {
      tmp = new File[2];
      tmp[0] = inFiles[i];
      tmp[1] = ansFiles[i];
      testFiles.add(i, tmp);
    }
    return testFiles;
  }
}
