public class Product {
  private int price;
  private String name;

  public Product(int price, String name) {
    this.price = price;
    this.name = name;
  }

  public String getInfo() {
    return "商品名: " + this.name + ", 価格: " + this.price + "円";
  }
}
