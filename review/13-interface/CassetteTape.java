public class CassetteTape implements Playable, Recordable {
  @Override
  public void play() {
    System.out.println("カセットテープを再生する");
  }

  @Override
  public void record() {
    System.out.println("カセットテープに録音する");
  }
}
