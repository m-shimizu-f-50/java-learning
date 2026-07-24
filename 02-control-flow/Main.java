

public class Main {
  public static void main(String[] args) {
    // 問題1
    int score = 75;

    if(score >= 90) {
      System.out.println("A");
    } else if(score >= 70) {
      System.out.println("B");
    } else if(score >= 60) {
      System.out.println("C");
    } else {
      System.out.println("D");
    }

    // 問題2
    int number = 0;

    for(int i = 1; i <= 10; i++) {
      number += i;
    }
    System.out.println("合計: " + number);

    // 問題3
    int[] nums = { 10, 20, 30, 40 };
    
    for (int num : nums) {
      System.out.println(num);
    }
    
    // 問題4
    int day = 3;
    switch (day) {
      case 1 -> System.out.println("月曜日");
      case 2 -> System.out.println("火曜日");
      case 3 -> System.out.println("水曜日");
      case 4 -> System.out.println("木曜日");
      case 5 -> System.out.println("金曜日");
      case 6 -> System.out.println("土曜日");
      case 7 -> System.out.println("日曜日");
    }
  }
}
