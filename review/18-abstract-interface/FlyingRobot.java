public class FlyingRobot extends Robot {
  public FlyingRobot(String name) {
    super(name);
  }

  @Override
  public void move() {
    System.out.println(name + "が空を飛んで移動する");
  }
}
