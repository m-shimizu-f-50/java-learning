import java.util.*;

interface StringOp {
  String apply(String s);
}

public class Main {
  public static void main(String[] args) {
    StringOp toUpperCase = s -> s.toUpperCase();
    System.out.println(toUpperCase.apply("hello")); // "HELLO"
    StringOp reverseString = s -> new StringBuilder(s).reverse().toString();
    System.out.println(reverseString.apply("hello")); // "olleh"

    List<Integer> nums = new ArrayList<>(List.of(5, 1, 4, 2, 3));

    // 降順にソート
    nums.sort((a, b) -> b.compareTo(a));
    System.out.println("ソートされた数値: " + nums); // [5, 4, 3, 2, 1]
  }
}