public class Counter {
  static int count = 0;
  static final String CATEGORY = "サンプル";

  Counter(){
    count++;
  }

  public static int getCount()
  {
    return count;
  }
}