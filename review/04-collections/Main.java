import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<String> cities = new ArrayList<>(List.of("Tokyo", "Osaka", "Kyoto", "Nagoya"));
    
    for (String city : cities) {
      System.out.println(city);
    }

    cities.remove("Osaka"); // Remove "Osaka"
    System.out.println(cities.size()); // 3

    List<Integer> numbers = new ArrayList<>();
    numbers.add(10);
    numbers.add(20);
    numbers.add(30);
    numbers.add(40);
    numbers.add(50);

    int result = 0;
    for (int number : numbers) {
      result += number;
    }
    System.out.println(result); // 150

  }
}