import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    List<String> tasks = new ArrayList<>();
    tasks.add("resume");
    tasks.add("buy milk");
    tasks.add("resume");
    tasks.add("call mom");

    for (String task : tasks) {
      if (task.equals("resume")) {
        // この書き方はエラーの原因になる
        // 理由はfor分にはイテレータという仕組みが裏で働いており(順番などを管理)それを無視して削除してしまうとイテレータがそれに気づかずズレが起きてしまうため専用のメソッドを使用する
        // tasks.remove(task);
      }
    }
    
    tasks.removeIf((task -> task.equals("resume")));

    System.out.println(tasks);
  }
}
