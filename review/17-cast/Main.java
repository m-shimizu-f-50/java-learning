public class Main {
  public static void main(String[] args){
    Shape[] shapes = { new Circle(5), new Square(4) };

    for (Shape shape : shapes) {
      if (shape instanceof Circle circle) {
        System.out.println("半径: " + circle.getRadius());
      } else if (shape instanceof Square square) {
        System.out.println(square.area());
      }
    }
  }
}
