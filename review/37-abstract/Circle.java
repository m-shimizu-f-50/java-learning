public class Circle extends Shape {
  double radius;

  Circle(double radius, String color) {
    super(color);
    this.radius = radius;
  }

  @Override
  public double calculateArea() {
    return Math.PI * radius * radius;
  }
}
