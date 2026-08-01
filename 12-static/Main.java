public class Main {
  public static void main(String[] args) {
    new Counter();
    new Counter();
    new Counter();
    System.out.println("現在のカウント: " + Counter.getCount());

    System.out.println("カテゴリー: " + Counter.CATEGORY);
  }
}