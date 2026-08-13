public class Main {
  public static void main(String[] args) {
    int[] nums = { 42, 7, 19, 3, 88 };
    
    // 配列の要素を昇順にソートする
    java.util.Arrays.sort(nums);
    System.out.println("昇順にソートされた配列: " + java.util.Arrays.toString(nums));

    // ソート後の配列から最初の2要素を取り出した新しい配列を作成する
    int[] newArray = java.util.Arrays.copyOfRange(nums, 0, 2);
    System.out.println("新しい配列: " + java.util.Arrays.toString(newArray)); // [3, 7]

    // 配列の要素をすべて9に置き換える
    int[] nums2 = { 1, 2, 3};
    java.util.Arrays.fill(nums2, 9);
    System.out.println("すべての要素を9に置き換えた配列: " + java.util.Arrays.toString(nums2)); // [9, 9, 9]

    // 見つかったインデックスを出力
    System.out.println("42のインデックス: " + java.util.Arrays.binarySearch(nums, 42)); // 3

  }
}
