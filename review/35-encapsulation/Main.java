import java.util.List;

public class Main {
  public static void main(String[] args) {
    ShoppingList shoppingList = new ShoppingList();
    shoppingList.addItem("追加ショップ");

    List<String> shoppingList2 = shoppingList.getItems();
    shoppingList2.add("追加ショップ2");

    System.out.println("元のショップリスト：" + shoppingList.getItems());
    System.out.println("外部から追加したショップリスト：" + shoppingList2);
  }
  
}
