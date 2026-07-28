# 07. インターフェース

## 使い方

### 定義と実装

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

class Duck implements Flyable, Swimmable {
    public void fly() { System.out.println("鴨が飛ぶ"); }
    public void swim() { System.out.println("鴨が泳ぐ"); }
}
```

インターフェースは実装を持たない「契約」（メソッドのシグネチャのみ宣言）。`implements` で複数実装できる点が、1つしか継承できない `extends` と対照的。

インターフェースのメソッドは暗黙的に `public`。実装側で `public` を省略すると「より制限の強いアクセス修飾子への変更はできない」というコンパイルエラーになる。

### ポリモーフィズム

```java
Flyable[] flyers = { new Bird(), new Duck() };
for (Flyable f : flyers) {
    f.fly(); // 実装クラスが違っても同じ呼び出しで処理できる
}
```

インターフェース型の配列・変数に異なる実装クラスを入れても、同じメソッド呼び出しで処理できる。

### 継承とインターフェースの使い分け

- 継承（`extends`）: is-a（〜の一種である）
- インターフェース（`implements`）: can-do（〜ができる）

## 覚えておくべきルール・規約

- インターフェースのメソッドは実装側で`public`を省略できない（暗黙的に`public`のため、より制限を強める変更は不可）
- `implements`は複数指定できる（`extends`は1つだけ）
- `implements A, B`のようにカンマ後にスペースを入れるのがJavaの慣習

## 演習

`07-interface/`にて以下を実装。

1. `Flyable`インターフェース: `void fly();`
2. `Swimmable`インターフェース: `void swim();`
3. `Bird`クラス: `Flyable`を実装
4. `Duck`クラス: `Flyable`と`Swimmable`の両方を実装
5. `Main`クラス: `Flyable[]`配列に`Bird`と`Duck`を入れて拡張for文で`fly()`を呼び出し（ポリモーフィズム確認）、`Duck`単体で`swim()`も呼び出し

演習コードは `07-interface/Flyable.java`, `Swimmable.java`, `Bird.java`, `Duck.java`, `Main.java`。`javac *.java`で一括コンパイルし動作確認済み。

## つまずきの分析

今回は目立ったつまずきなし。インターフェース実装メソッドに`public`を正しく付けられていた（JS経験者が見落としがちなポイント）。

---

これでオブジェクト指向（クラス／継承／インターフェース／ポリモーフィズム）の復習が完了。
