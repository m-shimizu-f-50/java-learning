public class Cat extends Animal {
  Cat(String name){
    super(name); // コンストラクタ
  }

  @Override
  void eat(){
    System.out.println(name + "は魚を食べる");
  }
}
