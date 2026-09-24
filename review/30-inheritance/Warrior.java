public class Warrior extends Character {
  private int strength;

  Warrior(int strength, String name, int hp) {
    super(name, hp);
    this.strength = strength;
  }

  String getInfo() {
    return "名前: " + getName() + ", HP: " + getHp() + ", 力: " + strength;
  }
}
