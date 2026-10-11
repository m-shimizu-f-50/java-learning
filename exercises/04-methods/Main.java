
public class Main {
  // 最大値を返すメソッド
  static int max(int a, int b) {
    return (a > b) ? a : b;
  }

  // 合計を返すメソッド
  static int sum(int[] nums) {
    int total = 0;
    for (int n : nums) {
      total += n;
    }
    return total;
  }

  static String greet(String name) {
    return "Hello, " + name + "!";
  }

  public static void main(String[] args) {
    int num1 = 10;
    int num2 = 20;

    int maximum = max(num1, num2);
    System.out.println("最大値: " + maximum);

    int[] numbers = { 1, 2, 3, 4, 5 };
    int total = sum(numbers);
    System.out.println("合計: " + total);

    String greeting = greet("まさと");
    System.out.println(greeting);
  }
}
