public class Main {
  public static void main(String[] args) {
    //Vehicle[]配列にTaxiとBusのインスタンスを入れ、拡張for文でprintName()とcalculateFare(5.0)（距離5km想定）を呼び出して出力
    Vehicle[] vehicles = new Vehicle[]{
      new Taxi("Taxi1"),
      new Bus("Bus1")
    };

    for (Vehicle vehicle : vehicles) {
      vehicle.printName();
      System.out.println("Fare for 5.0 km: " + vehicle.calculateFare(5.0));
    }
  }
}
