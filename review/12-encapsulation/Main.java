public class Main {
  public static void main(String[] args) {
    BankAccount bankAccount = new BankAccount(0);

    bankAccount.setBalance(-500);
    System.out.println("不正：" + bankAccount.getBalance());

    bankAccount.setBalance(3000);
    System.out.println("正常：" + bankAccount.getBalance());
  }
}
