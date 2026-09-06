public class Square extends Shape {
  public double side;

  Square(double side) {
    this.side = side;
  }

  @Override
  public double area() {
    return side * side;
  }
}
