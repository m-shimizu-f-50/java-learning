public class Main {
  public static void main(String[] args) {
    int temperature = 28;

    if (temperature >= 30) {
      System.out.println("猛暑");
    } else if (temperature >= 25) {
      System.out.println("暑い");
    } else {
      System.out.println("過ごしやすい");
    }

    String[] members = {"田中", "鈴木", "佐藤"};

    for (String member : members) {
      System.out.println(member);
    }
    
    // 月
    int month = 6;

    switch(month) {
      case 12 , 1 , 2 -> System.out.println("冬");
      case 3 , 4 , 5 -> System.out.println("春");
      case 6 , 7 , 8 , 9 -> System.out.println("夏");
      case 10 , 11 -> System.out.println("秋");
      default -> System.out.println("不明");
    }
  }
  
}
