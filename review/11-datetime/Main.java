import java.time.LocalDate;
import java.time.Period;

// LocalDate borrowDate = LocalDate.of(2026, 8, 1);（本の貸出日）を用意
// borrowDate.plusDays(14)で返却期限日を計算し、dueDateという別変数で受け取る（borrowDate自体が変わっていないことも確認）
// LocalDate today = LocalDate.of(2026, 8, 20);（今日の日付、と仮定）を用意
// Period.between(borrowDate, today)を使って、貸出日から今日までの経過期間を「◯年◯ヶ月◯日」の形式で出力
// today.isAfter(dueDate)を使って、返却期限を過ぎているかどうかを判定して出力
public class Main {
  public static void main(String[] args) {
    LocalDate borrowDate = LocalDate.of(2026, 8, 1); //（本の貸出日）

    // 返却期限日
    LocalDate dueDate = borrowDate.plusDays(14);
    System.out.println("本の貸出日" + borrowDate);
    System.out.println("返却期限日" + dueDate);

    // 貸出日から今日までの経過期間を「◯年◯ヶ月◯日」の形式で出力
    LocalDate today = LocalDate.of(2026, 8, 20); //（今日の日付、と仮定）を用意
    Period period = Period.between(borrowDate, today);
    System.out.println(period.getYears() + "年" + period.getMonths() + "ヶ月" + period.getDays() + "日");


    // 返却期限を過ぎているかどうかを判定して出力
    boolean isAfter = today.isAfter(dueDate);
    System.out.println("返却期限を過ぎているか：" + isAfter);

  }
}
