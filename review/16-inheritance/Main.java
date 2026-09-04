public class Main {
  public static void main(String[] args) {
    Car car = new Car("車");
    car.honk(); // 車がクラクションをけたたましく鳴らす

    Bicycle bicycle = new Bicycle("自転車");
    bicycle.honk(); // 自転車がクラクションを鳴らす
    bicycle.pedal(); // 自転車がペダルを漕ぐ
  }
  
}
