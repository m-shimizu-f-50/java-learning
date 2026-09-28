import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<String> books = new ArrayList<>();
    books.add("Java入門");
    books.add("Python入門");
    books.add("Java応用");
    books.add("Ruby入門");

    // Predicate: 1つ受け取ってtrue/falseを返す → trueのものが削除される
    // containsは部分一致判定
    books.removeIf(book -> !book.contains("入門"));

    // Comparator: 2つ受け取ってintを返す → compareToで昇順ソート
    books.sort((a, b) -> a.compareTo(b));

    // Consumer: 1つ受け取って何も返さない(void) → 表示するだけの処理
    books.forEach(book -> System.out.println("・" + book));
  }
}
