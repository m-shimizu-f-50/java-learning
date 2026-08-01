public class PartTimeEmployee extends Employee {
  double hourlyRate;
  int hoursWorked;

  PartTimeEmployee(String name, double hourlyRate, int hoursWorked) {
    super(name);
    this.hourlyRate = hourlyRate; // 時給
    this.hoursWorked = hoursWorked; // 労働時間
  }

  @Override
  double calculateSalary() {
    return hourlyRate * hoursWorked;
  }
}