public class Main {
  public static void main(String[] args) {
    String a = "test";
    String b = "test";
    String c = new String("test");
    System.out.println(a == b); // true
    System.out.println(a == c); // false
    System.out.println(a.equals(c)); // true

    StringBuilder sb = new StringBuilder();
    for(int i = 1; i <= 5; i++) {
      sb.append(i);
    }
    System.out.println(sb.toString()); // 12345
    sb.reverse();
    System.out.println(sb.toString()); // 54321
  }
}