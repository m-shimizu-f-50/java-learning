public class Dog extends Animal {
  Dog(String name) {
    super(name); // コンストラクタ
  }

  void bark() {
    System.out.println(name + "はワンワン吠える");
  }
}