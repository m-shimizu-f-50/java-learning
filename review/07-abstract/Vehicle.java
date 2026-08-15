abstract class Vehicle {
  private String name;

  public Vehicle(String name) {
    this.name = name;
  }

  // 実装済みメソッド
  public void printName() {
    System.out.println("乗り物: " + name);
  }

  // 抽象メソッド
  public abstract double calculateFare(double distance);
}