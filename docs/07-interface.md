# 07. インターフェース

## 要点

- インターフェースは実装を持たない「契約」（メソッドのシグネチャのみ宣言）
- `implements`で複数実装できる（`extends`は1つだけという継承の制約とは対照的）
- インターフェースのメソッドは暗黙的に`public`。実装側で`public`を省略すると「より制限の強いアクセス修飾子への変更はできない」というコンパイルエラーになる
- ポリモーフィズム: インターフェース型の配列・変数に異なる実装クラスを入れても、同じメソッド呼び出しで処理できる
- 継承（is-a: 〜の一種である）とインターフェース（can-do: 〜ができる）の使い分け

## 演習

`07-interface/`にて以下を実装。

1. `Flyable`インターフェース: `void fly();`
2. `Swimmable`インターフェース: `void swim();`
3. `Bird`クラス: `Flyable`を実装
4. `Duck`クラス: `Flyable`と`Swimmable`の両方を実装
5. `Main`クラス: `Flyable[]`配列に`Bird`と`Duck`を入れて拡張for文で`fly()`を呼び出し（ポリモーフィズム確認）、`Duck`単体で`swim()`も呼び出し

## つまずいた点・フィードバック

- 特につまずきなし。インターフェース実装メソッドに`public`を正しく付けられていた（JS経験者が見落としがちなポイント）
- 些細なスタイル: `implements Flyable,Swimmable`のカンマ後にスペースを入れる（`implements Flyable, Swimmable`）のがJavaの慣習
- 演習コードは `07-interface/Flyable.java`, `Swimmable.java`, `Bird.java`, `Duck.java`, `Main.java`。`javac *.java`で一括コンパイルし動作確認済み。

---

これでオブジェクト指向（クラス／継承／インターフェース／ポリモーフィズム）の復習が完了。
