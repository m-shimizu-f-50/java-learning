import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    // インデックスで削除する場合は、removeメソッドにインデックスを渡す
    List<Integer> productIds = new ArrayList<>();
    productIds.add(101);
    productIds.add(202);
    productIds.add(303);
    productIds.add(404);

    productIds.remove(2); // 303を削除
    productIds.remove(Integer.valueOf(202)); // 202を削除

    for(Integer productId : productIds) {
      System.out.println(productId);
    }

    // 値で削除する場合は、Integer型のオブジェクトを作成して渡す必要がある
    List<String> productNames = new ArrayList<>();
    productNames.add("りんご");
    productNames.add("みかん");
    productNames.add("バナナ");

    productNames.remove(String.valueOf("みかん")); // みかんを削除
    for(String productName : productNames) {
      System.out.println(productName);
    }
  }
}
