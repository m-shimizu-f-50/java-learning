public class Reservation {
  private static int totalReservations = 0;
  private final String reservationCode;

  public Reservation() {
    totalReservations++;
    this.reservationCode = "R-" + totalReservations;
  }

  public static int getTotalReservations() {
    return totalReservations;
  }

  public String getReservationCode() {
    return reservationCode;
  }
}
