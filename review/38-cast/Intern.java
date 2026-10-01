public class Intern extends Staff {
  Intern(String name) {
    super(name);
  }
  void learn() {
    System.out.println(name + "が研修を受ける");
  }
}
