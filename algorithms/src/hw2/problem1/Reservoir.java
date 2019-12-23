import java.util.Scanner;

/**
 * Reservoir: In an imaginary 2D world where distances are quantized to the
 * nearest meter, find the largest possible reservoir over a given stretch of
 * land.
 *
 * @author Alvin Lin axl1439@rit.edu
 * @author William Leuschner wel2138@rit.edu
 */
public class Reservoir {

  /**
   * Find the volume of the depression in the graph of data which could be
   * filled with the * most water.
   *
   * @param data The data to examine
   * @return The volume of the largest depression
   */
  public static long getMaxReservoirVolume(long[] data) {
    long highestVolume = 0;
    // We are going to pass over the array left to right and right to left
    // simultaneously, applying the same algorithm to identify the largest
    // reservoir. To do this, we need to keep track of the volumes and apexes
    // calculated by each pass.
    long currentVolumeLTR = 0;
    long currentVolumeRTL = 0;
    long currentApexLTR = data[0];
    long currentApexRTL = data[data.length - 1];
    // Loop over each element in the data
    for (int i = 1; i < data.length; ++i) {
      // The current element we're inspecting going left-to-right is i
      long LTRelement = data[i];
      // THe current element we're inspecting going right-to-left is len-1-i
      long RTLelement = data[data.length - 1 - i];
      // If the current element is higher than the highest point we've found
      // so far, then we replace the apex with it.
      if (LTRelement >= currentApexLTR) {
        currentApexLTR = LTRelement;
        // If the volume we have calculated for this reservoir is higher than
        // any previously calculated volume, then we replace the previous
        // value with the current volume.
        if (currentVolumeLTR > highestVolume) {
          highestVolume = currentVolumeLTR;
        }
        // Reset the volume calculation to 0 for the next reservoir.
        currentVolumeLTR = 0;
      } else {
        // If the current element is lower than the apex, then it is part of
        // the current reservoir we are counting, and we should add it to the
        // volume.
        currentVolumeLTR += currentApexLTR - LTRelement;
      }
      // This logic is repeated for the right to left pass as well.
      if (RTLelement >= currentApexRTL) {
        currentApexRTL = RTLelement;
        if (currentVolumeRTL > highestVolume) {
          highestVolume = currentVolumeRTL;
        }
        currentVolumeRTL = 0;
      } else {
        currentVolumeRTL += currentApexRTL - RTLelement;
      }
    }
    return highestVolume;
  }

  /**
   * The main function for Reservoir. Reads data from stdin and delegates
   * actions appropriately.
   *
   * @param args The command line arguments to this program
   * @time O(n)
   */
  public static void main(String[] args) {
    int n;
    long[] data;
    try (Scanner in = new Scanner(System.in)) {
      n = in.nextInt();
      data = new long[n];
      for (int i = 0; i < n; ++i) {
        data[i] = in.nextLong();
      }
    }
    System.out.println(getMaxReservoirVolume(data));
  }

}
