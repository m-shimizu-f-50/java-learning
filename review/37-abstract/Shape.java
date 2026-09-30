abstract class Shape {
  String color;

  Shape(String color) {
    this.color = color;
  }

  public void describe(){
    System.out.println("色: " + color + ", 面積: " + calculateArea());
  }

  abstract double calculateArea();
}
