abstract class Employee {
  String name;

  Employee(String name) {
    this.name = name;
  }

  // 実装済みメソッド
  void printName() {
    System.out.println("社員: " + name);
  }

  // 抽象メソッド
  abstract double calculateSalary();
}