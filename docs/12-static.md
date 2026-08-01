# 12. static

## 使い方

### staticフィールド

```java
public class Counter {
    static int count = 0; // クラス全体で1つだけ共有される

    Counter() {
        count++; // インスタンスが作られるたびに増える
    }
}
```

```java
new Counter();
new Counter();
new Counter();
System.out.println(Counter.count); // 3
```

`static`フィールドはインスタンスごとではなく、クラス全体で1つだけ共有される。`this.name`のようなインスタンスフィールドとは対照的。

### staticメソッド

```java
static int add(int a, int b) { return a + b; }
```

インスタンスを作らずに`クラス名.メソッド()`で直接呼べる。`public static void main(String[] args)`もその一例。

### static final（クラス定数）

```java
public static final double PI = 3.14159;
```

`static final`は「変更されないクラス共通の定数」を表す、実務で頻出のパターン。

### static初期化ブロック

```java
static {
    // クラスロード時に1度だけ実行される
}
```

## 覚えておくべきルール・規約

- `static`フィールドは全インスタンスで共有される（インスタンスごとに別の値を持つ通常のフィールドとは異なる）
- `static`メンバーは「個々のインスタンスに紐づくか、クラス全体に紐づくか」で使い分ける
- `static final`はクラス定数の定義によく使う

## 演習

`12-static/Counter.java`, `Main.java`にて以下を実装。

1. `Counter`クラス: `static int count = 0;`
2. コンストラクタで`count++`（インスタンス生成のたびに増加）
3. `static int getCount()`
4. `static final String CATEGORY = "サンプル";`
5. `Main`で`Counter`のインスタンスを3つ作り、`Counter.getCount()`で`3`を確認、`Counter.CATEGORY`も出力

## つまずきの分析

### インスタンス生成を伴わない実装

**何が起きたか**: 初回、`Counter`のインスタンスを一度も作らず、代わりに手動の`static increment()`メソッドを3回呼んで`count`を`3`にしていた。出力結果自体は同じ`3`だったが、「インスタンス生成のたびに自動で増える」という`static`の核心（全インスタンスで共有される）を確認できていなかった。

**なぜ**: `static`メソッドを直接呼ぶ操作に気を取られ、コンストラクタでの自動カウントという設計意図を見落とした。

**教訓**: 出力結果が仕様と一致していても、実装が学習の狙い（今回は「コンストラクタ経由での共有フィールドの更新」）を満たしているかは別に確認する必要がある。`new Counter()`を3回呼ぶ形に修正し、狙い通りの実装で動作確認済み。

演習コードは `12-static/Counter.java`, `Main.java`。コンパイル・実行して動作確認済み（現在のカウント3、カテゴリーサンプル）。
