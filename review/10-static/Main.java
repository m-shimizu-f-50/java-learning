public class Main {
  public static void main(String[] args) {
    new Ticket();
    new Ticket();
    new Ticket();
    new Ticket();

    System.out.println("現在のカウント：" + Ticket.getTicketNumber());
    System.out.println(Ticket.PREFIX);
  }
}
