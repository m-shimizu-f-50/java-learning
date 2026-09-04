public class Bicycle extends Vehicle {
  public Bicycle(String name) {
    super(name);
  }

  public void pedal() {
    System.out.println(name + "がペダルを漕ぐ");
  }
}
