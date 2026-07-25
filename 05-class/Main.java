public class Main {
  public static void main(String[] args) {
    Book book1 = new Book("Java入門", "山田太郎", 300);
    Book book2 = new Book("Python入門", "佐藤花子", 250);

    book1.printInfo();
    book2.printInfo();
  }
}
