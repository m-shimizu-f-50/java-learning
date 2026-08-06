// List<Integer> nums = List.of(5, 12, 8, 3, 20, 15);
// このリストから、Streamを使って10以上の数値だけをフィルタし、それらの合計値を計算して出力してください（filterとmapToInt().sum()を使います）。
// 書けたら共有してください。正解できたら「配列ユーティリティ」に進み、もし詰まるようであれば先にStream APIを軽く復習してから次に進みます。

// mapToInt()は、Streamの要素をint型に変換するためのメソッドです。これを使うことで、Streamの要素を数値として扱い、合計や平均などの計算が簡単に行えます。

import java.util.*;
import java.util.stream.*;

public class Main {
  public static void main(String[] args) {
    List<Integer> nums = List.of(5, 12, 8, 3, 20, 15);
    int sum = nums.stream()
                  .filter(n -> n >= 10)
                  .mapToInt(Integer::intValue)
                  .sum();
    System.out.println("合計: " + sum); // 出力: 合計: 47
  }
}