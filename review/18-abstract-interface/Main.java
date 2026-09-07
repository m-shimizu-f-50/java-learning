public class Main {
  public static void main(String[] args) {
    Robot[] robots = { new WheeledRobot("車輪型"), new FlyingRobot("飛行型") };

    for (Robot robot : robots) {
      robot.greet();
      robot.move();
      if (robot instanceof Chargeable chargeableRobot) {
        chargeableRobot.charge();
      }

    }
  }
  
}
