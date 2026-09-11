public class Main {
  public static void main(String[] args) {
    Reservation r1 = new Reservation();
    Reservation r2 = new Reservation();
    Reservation r3 = new Reservation();

    System.out.println("Total Reservations: " + Reservation.getTotalReservations());

    System.out.println("Reservation Code for r1: " + r1.getReservationCode());
    System.out.println("Reservation Code for r2: " + r2.getReservationCode());
    System.out.println("Reservation Code for r3: " + r3.getReservationCode());
  }
}