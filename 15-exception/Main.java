public class Main {

  // withdraw(double balance, double amount)メソッドを作成: amountがbalanceより大きい場合、throw new InsufficientFundsException("残高不足です")する。それ以外はbalance - amountを返す
  static double withdraw(double balance, double amount) throws InsufficientFundsException {
    if (amount > balance) {
      throw new InsufficientFundsException("残高不足です");
    }
    return balance - amount;
  }


  public static void main(String[] args) {
    // 例外処理テスト: withdrawメソッドを呼び出し、残高が1000円で、引き出す金額が1500円の場合、InsufficientFundsExceptionが発生することを確認する
    try {
      double balance = 1000.0;
      double amount = 1500.0;
      double newBalance = withdraw(balance, amount);
      System.out.println("新しい残高: " + newBalance);
    } catch (InsufficientFundsException e) {
      System.out.println(e.getMessage());
    } finally {
      System.out.println("処理が終了しました");
    }

    // 正常系テスト: withdrawメソッドを呼び出し、残高が1000円で、引き出す金額が500円の場合、正常に処理されることを確認する
    try {
      double newBalance = withdraw(1000.0, 500.0);
      System.out.println("新しい残高: " + newBalance);
    } catch (InsufficientFundsException e) {
      System.out.println(e.getMessage());
    } finally {
      System.out.println("処理が終了しました");
    }
  }
}