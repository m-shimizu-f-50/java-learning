public class Manager extends Staff {
  Manager(String name) {
    super(name);
  }
  void approveBudget() {
    System.out.println(name + "が予算を承認する");
  }
}
