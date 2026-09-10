import java.util.HashSet;

public class Main {
  public static void main(String[] args) {
    Point p1 = new Point(2, 3);
    Point p2 = new Point(2, 3);

    // equalsメソッドをオーバーライドしているため、内容が同じであればtrueを返す
    System.out.println(p1.equals(p2)); // true
    System.out.println(p1 == p2); // false

    // toStringメソッドをオーバーライドしているため、オブジェクトの内容を文字列として表示できる
    System.out.println(p1); // Point(2, 3)

    // hashCodeメソッドをオーバーライドしているため、内容が同じであれば同じハッシュコードを返す
    System.out.println(p1.hashCode()); // ハッシュコードを表示
    System.out.println(p2.hashCode()); // ハッシュコードを表示

    // HashSetに同じ内容のPointオブジェクトを追加しても、重複は許可されない
    HashSet<Point> set = new HashSet<>();
    set.add(p1);
    set.add(p2);
    System.out.println(set.size()); // 1

  }
}
