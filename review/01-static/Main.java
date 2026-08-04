public class Main {
  public static void main(String[] args) {
    int id1 = IdGenerator.issue();
    int id2 = IdGenerator.issue();
    int id3 = IdGenerator.issue();
    System.out.println(IdGenerator.PREFIX + id1);
    System.out.println(IdGenerator.PREFIX + id2);
    System.out.println(IdGenerator.PREFIX + id3);
  }
}