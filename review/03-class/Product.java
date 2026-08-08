public class Product {
  String name;
  double price;

  public Product(String name, double price) {
    this.name = name;
    this.price = price;
  }

  void printInfo() {
    System.out.println("商品: " + name + "(" + price + "円)");
  }
}