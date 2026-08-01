public class FullTimeEmployee extends Employee {
  double monthlySalary;

  FullTimeEmployee(String name, double monthlySalary) {
    super(name);
    this.monthlySalary = monthlySalary; // 月給
  }

  @Override
  double calculateSalary() {
    return monthlySalary;
  }
}