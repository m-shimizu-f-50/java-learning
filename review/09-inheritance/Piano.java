public class Piano extends Instrument {
  
  Piano(String name) {
    super(name);
  }
  
  void tune() {
    System.out.println( name + "を調律する");
  }
}
