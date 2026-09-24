# 06. 継承

## 用語集

- **スーパークラス（親クラス）**: 継承される側のクラス。`extends`の右に書く
- **サブクラス（子クラス）**: 継承する側のクラス。親の機能をそのまま使える
- **オーバーライド**: 親クラスのメソッドを、子クラスで同じシグネチャのまま再定義すること
- **単一継承**: 1つのクラスは`extends`で1つの親クラスしか持てない、というJavaの制約

## 使い方

### extends / super

```java
class Animal {
    String name;
    Animal(String name) {
        this.name = name;
    }
    void eat() {
        System.out.println(name + "が食べる");
    }
}

class Dog extends Animal {
    Dog(String name) {
        super(name); // 親クラスのコンストラクタを1行目で呼ぶ
    }
    void bark() {
        System.out.println(name + "が吠える");
    }
}
```

`extends` で親クラス（スーパークラス）を継承する。`super(...)` は親クラスのコンストラクタを呼び出すもので、子クラスのコンストラクタの1行目に書く必要がある。子クラスは親クラスの `public`/`protected` なフィールド・メソッドをそのまま使える。

### super()を書かないとどうなるか：暗黙の呼び出し

子クラスのコンストラクタに`super(...)`も`this(...)`も書かなかった場合、Javaは**自動的に、見えない1行目として`super();`（引数なし）を差し込む**。

```java
class Device {
    String brand;
    Device(String brand) { this.brand = brand; }
}

class Smartphone extends Device {
    String os;
    Smartphone(String brand, String os) {
        // super(); ← Javaが自動的にここに差し込む（書かなくても存在する）
        this.os = os;
    }
}
```

`Device`に引数なしのコンストラクタが無い（`Device(String brand)`しか無い）場合、この自動的な`super();`の呼び先が見つからず、コンパイルエラーになる（`コンストラクタ Deviceは指定された型に適用できません`）。

**注意**：`Smartphone`の`brand`という引数は、名前が同じでも**自動的には親に渡されない**。親に値を渡したい場合は`super(brand);`と自分で明示的に書く必要がある。明示的に`super(brand);`を書けば、`Device()`（引数なし）は無くても問題ない。

### オーバーライドと@Override

```java
@Override
void eat() {
    System.out.println(name + "が魚を食べる");
}
```

親と同じシグネチャのメソッドを子クラスで再定義することをオーバーライドという。

`@Override`はいつ書くか: 「2つ以上のクラスが継承している場合」ではなく、**「親クラスに既にあるメソッドを、そのクラス単体で上書きするとき」** に書く。

- 親のメソッドをそのまま使う（継承のみ）→ `@Override` 不要
- 親のメソッドを独自に再定義する（オーバーライド）→ `@Override` が必要

### 継承の制約

Javaは単一継承のみ（1クラスしか継承できない）。複数の型の性質を持たせたい場合はインターフェースを使う（次のトピック）。

### privateフィールドは子クラスから直接アクセスできない

親クラスのフィールドを`private`にすると、子クラスであっても直接アクセスできない（`private`は「同じクラス内のみ」、[11. カプセル化](11-encapsulation.md)のアクセス修飾子の表を参照）。

```java
class Character {
    private String name;
    ...
}
class Warrior extends Character {
    String test() {
        return name; // エラー: nameはCharacterでprivateアクセスされます
    }
}
```

**解決策として`protected`フィールドにするのは推奨されない**（サブクラスから自由に書き換えられてしまい、カプセル化の目的が弱まる）。代わりに、**フィールドは`private`のまま維持し、`protected`（または`public`）なgetterを用意する**のが正しい方法。

```java
class Character {
    private String name;
    protected String getName() { return name; } // これを子クラスから呼ぶ
}
```

## 覚えておくべきルール・規約

- 1クラスにつき`extends`できるのは1つだけ（単一継承）
- `super(...)`は子クラスのコンストラクタの1行目に書かなければならない
- `super(...)`を書かないと、Javaが自動的に引数なしの`super();`を差し込む。親に引数なしのコンストラクタが無ければコンパイルエラーになる
- 親の`private`フィールドは子クラスから直接アクセスできない。`protected`フィールド化ではなく、`protected`なgetterを用意する方が推奨される
- オーバーライドする際は`@Override`を毎回つける（言語仕様上は任意だが慣習）
- `@Override`は「そのクラス単体で親のメソッドを上書きしているか」を示すもので、継承の数とは無関係

## 演習

`06-inheritance/`にて以下を実装。

1. `Animal`クラス: `name`フィールド、コンストラクタ、`eat()`メソッド
2. `Dog`クラス（`Animal`継承）: 独自メソッド`bark()`
3. `Cat`クラス（`Animal`継承）: `eat()`を`@Override`
4. `Main`で`Dog`・`Cat`のインスタンスを生成し、`eat()`と固有メソッドを呼び出し

演習コードは `06-inheritance/Animal.java`, `Dog.java`, `Cat.java`, `Main.java`。コンパイル・実行して動作確認済み。

## つまずきの分析

### print と println の混同

**何が起きたか**: `Dog.bark()` と `Cat.eat()` で `System.out.print`（改行なし）を使っており、2つの出力が1行にくっついてしまった。

**なぜ**: JSの `console.log` は常に改行される感覚に慣れていたため、Javaで改行あり／なしの2種類のメソッドが分かれていることを見落とした。

**教訓**: 出力がくっついたら、まず `print` と `println` の取り違えを疑う。基本は `println` を使う。

### @Overrideの意味の誤解

**何が起きたか**: `@Override` を「2箇所以上でクラスが継承している場合につけるもの」だと誤解していた。

**なぜ**: 「継承」という言葉のイメージから「複数の継承関係が絡むときの目印」だと連想してしまった。

**教訓**: `@Override` は継承の数とは無関係で、「そのクラス単体で親のメソッドを上書きしているかどうか」を示すもの。オーバーライドするたびに毎回つける習慣にすれば迷わない。

### 復習（`review/30-inheritance`）：private継承時のアクセス制限で引数を二重に渡してしまう

**何が起きたか**: `Warrior`（`Character`継承）の`getInfo()`で親の`name`/`hp`（`private`）に直接アクセスできず、`Main`でのインスタンス生成時とメソッド呼び出し時の両方に同じ値を渡す、という回避策をとってしまった。

**なぜ**: `private`フィールドは子クラスからもアクセスできないという制約（`11-encapsulation`のアクセス修飾子の表）を踏まえず、「アクセスできないなら再度渡せばいい」という力技で解決しようとした。

**教訓**: 親の`private`フィールドに子クラスからアクセスしたい場合は、値を再度渡すのではなく、`protected`なgetterを用意する。「`protected`フィールドにする」ではなく「`private`フィールド＋`protected`のgetter」を選ぶことで、カプセル化を保ったままアクセスできる。

演習コードは `review/30-inheritance/Character.java`, `Warrior.java`, `Main.java`。コンパイル・実行して動作確認済み（名前: ゴブリン, HP: 5000, 力: 100）。
