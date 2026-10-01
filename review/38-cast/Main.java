public class Main {
  public static void main(String[] args) {
    Staff[] staffs = { new Manager("田中"), new Intern("佐藤") };

    for (Staff staff : staffs) {
      if (staff instanceof Manager manager) {
        manager.approveBudget();
      } else if (staff instanceof Intern intern) {
        intern.learn();
      }
    }
  }
}
