public class Ticket {
  static int ticketNumber = 0;
  static final String PREFIX = "TKT-";

  Ticket(){
    ticketNumber++;
  }

  static int getTicketNumber() {
    return ticketNumber;
  }
}
