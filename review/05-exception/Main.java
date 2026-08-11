public class Main {
  static void validateAge(int age) throws AgeValidationException {
    if (age < 0 || age > 120) {
      throw new AgeValidationException("不正な年齢です");
    }
  }

  public static void main(String[] args) {
    try {
      validateAge(150);
    } catch (AgeValidationException e) {
      System.out.println(e.getMessage());
    } finally {
      System.out.println("検証処理が終了しました");
    }

    // 正常系
    try {
      validateAge(30);
    } catch (AgeValidationException e) {
      System.out.println(e.getMessage());
    } finally {
      System.out.println("検証処理が終了しました");
    }
  }
}