import java.time.LocalDate;
import java.time.Period;

public class Main {
  public static void main(String[] args) {
    LocalDate today = LocalDate.now();

    // 入社日
    LocalDate hireDate = LocalDate.of(2020, 4, 1);
    // 一年後
    LocalDate oneYearDate = hireDate.plusYears(1);
    System.out.println("入社日: " + hireDate);
    System.out.println("一年後: " + oneYearDate);

    Period period = Period.between(hireDate, today);
    System.out.println(period.getYears() + "年" + period.getMonths() + "ヶ月" + period.getDays() + "日");

    //入社日が今日より前かどうかを判定
    boolean isBefore = hireDate.isBefore(today);
    System.out.println("入社日は今日より前か: " + isBefore);
  }
}