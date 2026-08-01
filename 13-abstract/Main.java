public class Main {
  public static void main(String[] args) {
    //Employee[]配列にFullTimeEmployeeとPartTimeEmployeeのインスタンスを入れ、拡張for文でprintName()とcalculateSalary()を呼び出して出力（ポリモーフィズムの復習も兼ねる）
    Employee[] employees = new Employee[]{
      new FullTimeEmployee("Alice", 3000),
      new PartTimeEmployee("Bob", 20, 80)
    };

    for (Employee employee : employees) {
      employee.printName();
      System.out.println("Salary: " + employee.calculateSalary());
    }
  }
  
}
