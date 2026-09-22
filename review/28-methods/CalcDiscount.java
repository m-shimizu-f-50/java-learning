public class CalcDiscount {
  static int calcDiscount(int price, int percent) {
    return price - (price * percent / 100);
  }

  static double calcDiscount(double price, double rate) {
    return price - (price * rate );
  }
}