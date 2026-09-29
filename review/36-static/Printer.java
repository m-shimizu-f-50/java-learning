public class Printer {
  int copies;
  static int totalPrinters;

  Printer(int copies) {
    this.copies = copies;
    totalPrinters++;
  }

  public void printInfo() {
    System.out.println(this.copies);
  }

  public static int getTotalPrinters() {
    return totalPrinters;
  }
}
