import java.util.List;
import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    Integer[] scores = { 85, 92, 78, 65, 92 };

    List<Integer> scoreList = Arrays.asList(scores);
    System.out.println("要素数: " + scoreList.size());

    Arrays.sort(scores);
    System.out.println("ソート: " + Arrays.toString(scores));

    int index = Arrays.binarySearch(scores, 92);
    System.out.println("92のインデックス: " + index);
  }
}
