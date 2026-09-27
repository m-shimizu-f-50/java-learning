import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {
    List<Integer> salaries = new ArrayList<>();
    salaries.add(250000);
    salaries.add(320000);
    salaries.add(180000);
    salaries.add(410000);
    salaries.add(290000);

    List<BigDecimal> result = salaries.stream()
        .filter(n -> n > 300000)
        .map(n -> new BigDecimal(n).multiply(new BigDecimal("1.1")))
        .collect(Collectors.toList());
    long count = salaries.stream()
        .filter(n -> n > 300000)
        .count();

    System.out.println(result);
    System.out.println(count);
  }
}
