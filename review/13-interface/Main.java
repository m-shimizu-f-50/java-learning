public class Main {
  public static void main(String[] args) {
    Playable[] playables = { new CD(), new CassetteTape() };
    for (Playable playable : playables) {
      playable.play();
    }

    CassetteTape cassetteTape = new CassetteTape();
    cassetteTape.record();
  }
}
