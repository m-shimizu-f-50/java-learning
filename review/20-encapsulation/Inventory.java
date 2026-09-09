public class Inventory {
  private int quantity;

  public Inventory(int quantity) {
    this.quantity = quantity;
  }

  public void add(int amount) {
    if (amount <= 0) {
      System.out.println("数量は1以上でなければなりません");
    } else {
      this.quantity += amount;
    }
  }

  public void removeStock(int amount) {
    if (amount > this.quantity) {
      System.out.println("在庫不足です");
    } else if (amount < 0) {
      System.out.println("不正な数量です");
    } else {
      this.quantity -= amount;
    }
  }

  public int getQuantity() {
    return this.quantity;
  }
}
