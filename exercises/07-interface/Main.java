public class Main {
  // Mainクラス: Flyable型の配列にBirdとDuckのインスタンスを入れ、拡張for文でfly()を呼び出す（ポリモーフィズムの確認）。さらにDuckのインスタンスを別途作ってswim()も呼び出す
  public static void main(String[] args) {
    Flyable[] flyables = {new Bird(), new Duck()};

    for (Flyable flyable : flyables) {
      flyable.fly();
    }

    Swimmable[] swimmables = {new Duck()};
    for (Swimmable swimmable : swimmables) {
      swimmable.swim();
    }
  }
}
