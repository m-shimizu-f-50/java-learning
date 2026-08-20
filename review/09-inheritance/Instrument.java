public class Instrument {
  String name;

  Instrument(String name) {
    this.name = name;
  }

  void play() {
    System.out.println( name + "を演奏する");
  }
}