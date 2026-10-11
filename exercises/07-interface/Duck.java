public class Duck implements Flyable,Swimmable {
  @Override
  public void fly() {
    System.out.println("アヒルが飛ぶ");
  }

  @Override
  public void swim() {
    System.out.println("アヒルが泳ぐ");
  }
}
