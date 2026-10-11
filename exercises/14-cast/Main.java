public class Main {
  public static void main(String[] args) {
    //Employee[]配列にFullTimeEmployeeとPartTimeEmployeeのインスタンスを入れ、拡張for文でprintName()とcalculateSalary()を呼び出して出力（ポリモーフィズムの復習も兼ねる）
    Employee[] employees = new Employee[]{
      new FullTimeEmployee("Alice", 3000),
      new PartTimeEmployee("Bob", 20, 80)
    };

    for (Employee employee : employees) {
      if (employee instanceof FullTimeEmployee fte) {
        // FullTimeEmployee型にキャストして、月給を取得
        System.out.println("正社員: " + fte.monthlySalary);
      }else if (employee instanceof PartTimeEmployee pte) {
        // PartTimeEmployee型にキャストして、時給と勤務時間を取得
        System.out.println("パート: " + pte.hourlyRate + "円 × " + pte.hoursWorked + "時間");
      }
    }
  }
  
}