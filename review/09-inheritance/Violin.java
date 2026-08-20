public class Violin extends Instrument {
  Violin(String name) {
    super(name);
  }

  @Override
  void play() {
    System.out.println(name + "を優雅に弾く");
  }
}
