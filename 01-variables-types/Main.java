public class Main {
    public static void main(String[] args) {

        int quantity = 3;          // 整数
        double unitPrice = 250.5; // 小数
        double total = quantity * unitPrice; // 計算結果を格納
        final double taxRate = 0.1; // 定数（変更不可）
        System.out.println(total * (1 + taxRate)); // 税込み価格を出力

    }
}
