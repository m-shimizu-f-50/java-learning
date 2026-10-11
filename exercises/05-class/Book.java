public class Book {
  // フィールド
  String title;
  String author;
  int pages;

  // コンストラクタ
  public Book(String title, String author, int pages) {
    this.title = title;
    this.author = author;
    this.pages = pages;
  }

  // メソッド
  void printInfo() {
    System.out.println("『" + title + "』 by " + author + "（" + pages + "ページ）");
  }

}