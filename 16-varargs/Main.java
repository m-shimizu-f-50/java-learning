public class Main {
  // 可変長引数を使った平均値を求めるメソッド
  static double average(double... numbers) {
    // 渡させた引数が0個の場合は0を返す
    if (numbers.length == 0) {
      return 0;
    }
    // 渡させた引数の平均を求める
    double sum = 0;
    for (double number : numbers) {
      sum += number;
    }
    return sum / numbers.length;
  }

  // 可変長引数を使った全要素の出力メソッド
  static void printAll(String label, String... items) {
    System.out.println(label);
    for (String item : items) {
      System.out.println("- " + item);
    }
  }

  public static void main(String[] args) {
    System.out.println(average(10, 20, 30));
    System.out.println(average());
    printAll("好きな果物", "りんご", "ばなな", "ぶどう");
  }
}