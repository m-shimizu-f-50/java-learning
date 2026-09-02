public class Main {
  static int sumOfSquares(int... numbers) {
    int sum = 0;
    for (int number : numbers) {
      sum += number * number;
    }
    return sum;
  }

  static void printLabeled(String label, String... items) {
    System.out.println(label);
    for (String item : items) {
      System.out.println(" - " + item);
    }
  }
  
  public static void main(String[] args) {
    System.out.println(sumOfSquares(2, 3, 4));
    printLabeled("好きな動物", "犬", "猫", "鳥");
  }
}
