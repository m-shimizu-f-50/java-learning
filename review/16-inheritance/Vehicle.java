public class Vehicle {
  String name;

  public Vehicle(String name) {
    this.name = name;
  }

  public void honk() {
    System.out.println(name + "がクラクションを鳴らす");
  }
}
