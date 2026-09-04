public class Car extends Vehicle {
  public Car(String name) {
    super(name);
  }

  @Override
  public void honk() {
    System.out.println(name + "がクラクションをけたたましく鳴らす");
  }
}
