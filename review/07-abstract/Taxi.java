public class Taxi extends Vehicle {
  // Taxiクラス（Vehicleを継承）: calculateFare(double distance)は400 + distance * 100（初乗り400円＋距離×100円）を返す
  public Taxi(String name) {
    super(name);
  }

  @Override
  public double calculateFare(double distance) {
    return 400 + distance * 100; // 初乗り400円＋距離×100円
  }
}
