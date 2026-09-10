import java.util.Objects;

public class Point {
  private int x;
  private int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof Point))
      return false;
    Point other = (Point) obj;
    return this.x == other.x && this.y == other.y;
  }

  @Override
  public String toString() {
    return "Point(" + x + ", " + y + ")";
  }

  // equals()と一貫性を持たせる（Objects.hash(x, y)が使えます）
  @Override
  public int hashCode() {
    return Objects.hash(x, y); // xとyの値を使って一意のハッシュコードを生成する(equals()がtrueを返す場合、hashCode()も同じ値を返す必要があるため)→これによりxとyの値が同じPointオブジェクトは同じハッシュコードを持つことが保証される
  }
}