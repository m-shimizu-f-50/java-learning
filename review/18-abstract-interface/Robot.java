public abstract class Robot {
  public String name;

  public Robot(String name) {
    this.name = name;
  }

  public void greet() {
    System.out.println(name + "はこんにちはと言う");
  }

  public abstract void move();
}
