public class Circle extends Shape {
  public double radius;

  Circle(double radius){
    this.radius = radius;
  }

  public double getRadius(){
    return radius;
  }

  @Override
  public double area(){
    return radius * radius * Math.PI;
  }
}
