import java.util.Arrays;

public class Main {
  public static void main(String[] args) {
    int[] steps = { 8200, 6500, 10300, 7400, 9100 };
    int maxStep = 0;
    for (int step : steps){
      if(step > maxStep){
        maxStep = step;
      }
    }
    System.out.println("最大値：" + maxStep);

    int sum = 0;
    for (int i = 0; i < steps.length; i++) {
      sum += steps[i];
    }
    double average = (double) sum / steps.length;
    System.out.println("合計：" + sum);
    System.out.println("平均：" + average);

    // 代入は「同じ実体への参照のコピー」なので、copyを書き換えるとstepsも変わる
    int[] copy = steps;
    copy[0] = 0;
    System.out.println("代入後のsteps[0]：" + steps[0]);

    // Arrays.copyOfは中身をコピーした別の実体を作るので、書き換えても元は変わらない
    int[] original = { 8200, 6500, 10300, 7400, 9100 };
    int[] independent = Arrays.copyOf(original, original.length);
    independent[0] = 0;
    System.out.println("copyOf後のoriginal[0]：" + original[0]);

    int[][] table = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
    int diagonalSum = 0;
    for (int i = 0; i < table.length; i++) {
      diagonalSum += table[i][i];
    }
    System.out.println("対角成分の合計：" + diagonalSum);
  }
  
}
