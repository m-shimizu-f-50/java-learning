public class Student {
  private String name;
  private int score;

  // コンストラクタ
  public Student(String name, int score) {
    this.name = name;
    this.score = score;
  }

  // ゲッター
  public String getName() {
    return name;
  }

  public int getScore() {
    return score;
  }

  // セッター
  public void setScore(int score) {
    if(score < 0 || score > 100) {
      System.out.println("不正な点数です");
    } else {
      this.score = score;
    }
  }
}