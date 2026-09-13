public class ParkingLot {
  public void park(boolean hasSpace) throws ParkingFullException {
    if (!hasSpace) {
      throw new ParkingFullException("満車です");
    }
    System.out.println("駐車できました");
  }
  
}
