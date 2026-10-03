public class Main {
  public static void main(String[] args) {
    PaymentProcessor processor = new PaymentProcessor();

    try{
      processor.process(false);
    } catch (PaymentException e) {
      System.out.println("決済エラー: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("予期せぬエラー: " + e.getMessage());
    } finally {
      System.out.println("処理終了");
    }
  }
  
}
