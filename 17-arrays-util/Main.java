import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    int[] nums = { 8, 3, 1, 9, 5 };
    Arrays.sort(nums);
    System.out.println("ソートされた配列: " + Arrays.toString(nums));

    // Arrays.copyOfRange()で、ソート後の配列から最初の3要素を取り出した新しい配列を作り、Arrays.toString()で出力
    int[] newArray = Arrays.copyOfRange(nums, 0, 3);
    System.out.println("新しい配列: " + Arrays.toString(newArray));

    // Arrays.fill()で長さ4のint[]配列を全て0で埋めて出力
    int[] filledArray = new int[4];
    Arrays.fill(filledArray, 0);
    System.out.println("埋められた配列: " + Arrays.toString(filledArray));

    // ソート済みのnumsに対してArrays.binarySearch(nums, 9)を実行し、見つかったインデックスを出力 Arrays.binarySearch(配列, 探したい値)
    int index = Arrays.binarySearch(nums, 9);
    System.out.println("インデックス: " + index);
  }
}