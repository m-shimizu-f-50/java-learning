public class Main {
  public static double totalPrice(double... prices){
    double total = 0;
    for(double price : prices){
      total += price;
    }
    return total;
  }

  public static void printReceipt(String customerName, double... prices){
    double total = totalPrice(prices);
    System.out.println("お客様: " + customerName);
    for(double price : prices){
      System.out.println("価格: " + price);
    }
    System.out.println("合計: " + total);
  }
  public static void main(String[] args) {

    printReceipt("田中", 1200, 350, 980);
    printReceipt("鈴木");
  }
}
