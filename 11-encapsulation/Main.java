public class Main {
  public static void main(String[] args){
    Student student = new Student("まさと", 31);

    student.setScore(90);
    System.out.println("更新されたスコア: " + student.getScore());

    student.setScore(110); // 不正なスコア
    
    System.out.println("最終スコア: " + student.getScore());
  }
}