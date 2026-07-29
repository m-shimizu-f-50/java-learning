import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<String> fruits = new ArrayList<>();
    fruits.add("りんご");
    fruits.add("バナナ");
    fruits.add("ぶどう");

    for (String fruit : fruits) {
      System.out.println(fruit);
    }

    fruits.remove("バナナ");
    System.out.println(fruits.size());

    List<Integer> numbers = new ArrayList<>();
    numbers.add(1);
    numbers.add(2);
    numbers.add(3);
    numbers.add(4);
    numbers.add(5);

    int sum = 0;
    for (int number : numbers) {
      sum += number;
    }

    System.out.println("合計: " + sum);

 }
}