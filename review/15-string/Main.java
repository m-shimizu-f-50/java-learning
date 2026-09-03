public class Main {
  public static void main(String[] args) {
    String x = "hello";
    String y = "hello";
    String z = new String("hello");

    System.out.println(x == y); // true
    System.out.println(x == z); // false
    System.out.println(x.equals(z)); // true

    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= 5; i++) {
      sb.append(i);
    }
    
    sb.reverse();
    System.out.println(sb.toString()); // 54321
  }
  
}
