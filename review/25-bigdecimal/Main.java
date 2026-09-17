import java.math.BigDecimal;

public class Main {
  public static void main(String[] args) {
    int quantity = 3;
    BigDecimal unitPrice = new BigDecimal("250.5");
    final BigDecimal taxPate = new BigDecimal("0.1");

    BigDecimal total = unitPrice.multiply(new BigDecimal(quantity));

    System.out.println("税込価格：" + total.multiply(BigDecimal.ONE.add(taxPate)));


  }
}
