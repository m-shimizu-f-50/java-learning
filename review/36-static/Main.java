public class Main {
  public static void main(String[] args) {
    Printer printer1 = new Printer(3);
    Printer printer2 = new Printer(6);
    Printer printer3 = new Printer(2);

    printer1.printInfo();
    System.out.println(Printer.getTotalPrinters());
  }
}
