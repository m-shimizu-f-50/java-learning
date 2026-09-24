public class Character {
  private String name;
  private int hp;

  Character(String name, int hp) {
    this.name = name;
    this.hp = hp;
  }

  protected String getName() {return name;}
  protected int getHp() {return hp;}
}
