import java.util.*;
import java.util.stream.*;

public class Main {
  public static void main(String[] args) {
    // 15より大きい数だけをフィルタし、それぞれ2倍にした値をリストとして収集する
    List<Integer> numbers = List.of(3, 8, 15, 22, 4, 9, 30, 7);
    List<Integer> result = numbers.stream()
        .filter(num -> num > 15)
        .map(num -> num * 2)
        .collect(Collectors.toList());
    System.out.println("結果: " + result);

    // 文字列のリストから、長さが5より大きい単語だけをフィルタして収集する
    List<String> words = List.of("apple", "banana", "kiwi", "fig", "grape");
    List<String> longWords = words.stream()
        .filter(word -> word.length() >= 5)
        .collect(Collectors.toList());
    System.out.println("長い単語: " + longWords);

    // 全単語の合計文字数を計算して出力（mapToIntと.sum()を使う）
    int totalLength = words.stream()
        .mapToInt(String::length)
        .sum();
    System.out.println("全単語の合計文字数: " + totalLength);
  }
}