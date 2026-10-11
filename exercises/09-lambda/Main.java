import java.util.*;


interface Operation {
    int apply(int a, int b);
}


public class Main {
  public static void main(String[] args) {
    Operation addition = (a, b) -> a + b;
    Operation multiplication = (a, b) -> a * b;

    System.out.println("足し算: " + addition.apply(5, 3));
    System.out.println("掛け算: " + multiplication.apply(5, 3));

    List<String> names = new ArrayList<>(List.of("Alice", "Charlie", "Bob"));
    // 名前をアルファベット順にソート
    names.sort((a, b) -> a.compareTo(b));
    System.out.println("ソートされた名前: " + names);
  }
}