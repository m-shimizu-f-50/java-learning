public class IdGenerator {
  private static int nextId = 1000;

  public static int issue() {
    return nextId++;
  }

  public static final String PREFIX = "USER-";
}