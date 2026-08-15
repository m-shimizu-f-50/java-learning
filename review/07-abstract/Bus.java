public class Bus extends Vehicle {
  // calculateFare(double distance)は距離に関わらず固定210を返す
  public Bus(String name) {
    super(name);
  }

  @Override
  public double calculateFare(double distance){
    return 210; // 距離に関わらず固定210円
  }
}