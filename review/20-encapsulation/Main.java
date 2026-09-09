public class Main {
  public static void main(String[] args) {
    Inventory inventory = new Inventory(100);

    // 正常系
    inventory.add(50);
    inventory.removeStock(30);
    System.out.println("在庫数: " + inventory.getQuantity()); // 在庫数: 120

    // 異常系
    inventory.add(-10); // 不正な数量です
    inventory.removeStock(200); // 在庫不足です
    inventory.removeStock(-5); // 不正な数量です

    System.out.println("最終在庫数: " + inventory.getQuantity()); // 最終在庫数: 120
  }
}
