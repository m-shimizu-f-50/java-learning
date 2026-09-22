public class Main {
  public static void main(String[] args){
    System.out.println("整数の割引率：" + CalcDiscount.calcDiscount(2000, 30));

    System.out.println("小数の割引率：" + CalcDiscount.calcDiscount(2000, 0.5));
  }
}
