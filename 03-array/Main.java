public class Main {
  public static void main(String[] args){
    int[] scores = { 80, 65, 90, 72, 55 };
    for (int score : scores) {
      System.out.println(score);
    }

    int sum = 0;
    for (int i = 0; i < scores.length; i++) {
      sum += scores[i];
    }
    System.out.println("合計: " + sum);

    //合計点から平均点（double）を計算し出力
    double average = (double) sum / scores.length;
    System.out.println("平均: " + average);

    int[][] table = { { 1, 2 }, { 3, 4 } };
    System.out.println(table[1][0]);
  }
}