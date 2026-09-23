public class Main {
  public static void main(String[] args) {
    Product product1 = new Product(3000, "消しゴム");
    Product product2 = new Product(450, "鉛筆");

    System.out.println(product1.getInfo());
    System.out.println(product2.getInfo());
  }
  
}
