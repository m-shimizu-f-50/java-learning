public class Main {
  public static void main(String[] args) {
    ParkingLot parkingLot = new ParkingLot();
    // 異常系
    try {
      parkingLot.park(false);
    } catch (ParkingFullException e) {
      System.out.println(e.getMessage());
    } finally {
      System.out.println("処理が終了します");
    }
    // 正常系
    try {
      parkingLot.park(true);
    } catch (ParkingFullException e) {
      System.out.println(e.getMessage());
    } finally {
      System.out.println("処理が終了します");
    }
  }
  
}
