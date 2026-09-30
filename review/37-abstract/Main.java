public class Main {
  public static void main(String[] args) {
    Shape[] tests = { new Circle(5, "赤"), new Rectangle(3.5, 3.5, "青") };

    for(Shape test : tests ){
      test.describe();
    }
  }
  
}
