import java.util.ArrayList;
import java.util.List;

public class ShoppingList {
  private List<String> items = new ArrayList<>();

  public void addItem(String i) {
    items.add(i);
  }

  public List<String> getItems() {
    return new ArrayList<>(items);
  }
}
