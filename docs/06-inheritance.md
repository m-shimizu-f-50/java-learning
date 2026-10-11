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

### super.メソッド名(...)：オーバーライドした中から親の処理も呼ぶ

```java
class Greeter {
    void greet(String name) {
        System.out.println("こんにちは、" + name + "さん");
    }
}

class PoliteGreeter extends Greeter {
    @Override
    void greet(String name) {
        System.out.println("お世話になっております。");
        super.greet(name); // 親の実装を直接呼ぶ
    }
}
```

`super.greet(name)`は、子クラスで独自の処理（`"お世話になっております。"`）を追加しつつ、**親クラスの元の処理も実行したい**ときに使う。「親の処理に何かを追加したいが、親の処理自体は捨てたくない」という場面の定番。

**`super.`を付けないとどうなるか**：`greet(name)`のように`super.`を付けずに呼ぶと、[07. インターフェース](07-interface.md)で確認した動的ディスパッチにより、常に「実体（この場合は`PoliteGreeter`自身）の`greet()`」が呼ばれる。`PoliteGreeter.greet()`の中から`greet(name)`を呼ぶと、それは自分自身を呼ぶことになり、**無限に自分を呼び続けて`StackOverflowError`になる**。`super.`を付けることで、動的ディスパッチを迂回し、親クラスの実装に直接ジャンプできる。

**注意（コンストラクタの暗黙的`super()`との違い）**：これは「コンストラクタの1行目に書かないと自動で`super()`が差し込まれる」というルール（上記）とは**別の話**。あのルールはコンストラクタ限定で、通常のメソッド呼び出しには適用されない。`greet(name)`を`super.`無しで書いても、コンパイルエラーにはならず、実行時に無限再帰が起きる（コンパイル時には検出できない、`14-cast`/`15-exception`で確認した「実行時にしか分からない」類の問題）。

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
- オーバーライドした中から`super.メソッド名(...)`で親の元の処理を呼べる。「コンストラクタの暗黙的`super()`」とは別の仕組みで、こちらは`super.`を省略すると自分自身を無限に呼び続け`StackOverflowError`になる（コンパイルエラーにはならない）
- オーバーライドする際は`@Override`を毎回つける（言語仕様上は任意だが慣習）
- `@Override`は「そのクラス単体で親のメソッドを上書きしているか」を示すもので、継承の数とは無関係

## 演習

`exercises/06-inheritance/`にて以下を実装。

1. `Animal`クラス: `name`フィールド、コンストラクタ、`eat()`メソッド
2. `Dog`クラス（`Animal`継承）: 独自メソッド`bark()`
3. `Cat`クラス（`Animal`継承）: `eat()`を`@Override`
4. `Main`で`Dog`・`Cat`のインスタンスを生成し、`eat()`と固有メソッドを呼び出し

演習コードは `exercises/06-inheritance/Animal.java`, `Dog.java`, `Cat.java`, `Main.java`。コンパイル・実行して動作確認済み。

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

### 復習（`review/42-inheritance`）：コンストラクタの暗黙的super()と、メソッドのsuper.呼び出しの混同

**何が起きたか**: `super.`を付けずにオーバーライド元のメソッドを呼ぶと無限再帰になる理由を聞いたところ、「`super`を付けないと暗黙的に引数無しの`super()`が自動で呼ばれ、親のメソッドには引数があるのでコンパイルエラーになる」と回答した。これは「コンストラクタの暗黙的`super()`」のルールであり、今回の「通常メソッド内で`super.`を付けずに呼ぶ」という場面とは無関係だった。実際には、`super.`無しの呼び出しは動的ディスパッチで自分自身を呼び続け、コンパイルエラーにはならず実行時に`StackOverflowError`になる。

**なぜ**: 「`super`」というキーワードが登場する場面を、コンストラクタでの暗黙呼び出し（1箇所にしか適用されないルール）と、通常メソッドでの明示的な`super.メソッド名(...)`（全く別の目的の構文）とで、同じルールだと一般化してしまっていた。

**教訓**: `super`は「コンストラクタの頭に暗黙的に差し込まれるもの」と「オーバーライド元の処理を明示的に呼ぶもの（`super.メソッド名`）」の2つの異なる使われ方がある。前者はコンストラクタのみ、後者は通常のメソッドのみで使う、という適用範囲の違いを意識する。

演習コードは `review/42-inheritance/Notification.java`, `UrgentNotification.java`, `Main.java`。コンパイル・実行して動作確認済み（緊急通知: サーバーがダウンしました／通知: サーバーがダウンしました）。
