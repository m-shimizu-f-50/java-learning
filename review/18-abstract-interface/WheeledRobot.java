public class WheeledRobot extends Robot implements Chargeable {

  public WheeledRobot(String name) {
    super(name);
  }

  @Override
  public void move() {
    System.out.println(name + "が車輪で移動する");
  }

  @Override
  public void charge() {
    System.out.println(name + "が充電する");
  }
}
