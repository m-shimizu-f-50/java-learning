# 02. 制御構文

## 使い方

### if文

```java
if (score >= 90) {
    // ...
} else if (score >= 70) {
    // ...
} else {
    // ...
}
```

条件式は必ず `boolean` 型でなければならない。JSのように数値や文字列を暗黙的に真偽判定することはできない。

### for文

```java
for (int i = 1; i <= 10; i++) {
    // ...
}

// 拡張for文（JSのfor...of相当）
for (int n : nums) {
    // ...
}
```

通常のfor文はJSと同じ書き方に加えて、配列やコレクションの全要素を走査する拡張for文が使える。

### while / do-while

JSと同じ書き方。

### switch文

```java
switch (day) {
    case 1 -> System.out.println("月曜");
    default -> System.out.println("不明");
}
```

Java 14以降の矢印構文を使うと `break` が不要になる。

## 覚えておくべきルール・規約

- `if`の条件式は必ず`boolean`型（数値やオブジェクトの暗黙的な真偽判定はできない）
- `switch`の矢印構文（`case X -> ...`）を使うと`break`が不要
- 想定外の値に備えて`default`ケースを入れておくのが実務での安全策

## 演習

`02-control-flow/Main.java` にて以下を実装。

1. `int score = 75;` を用意し、`if-else if-else`で90以上A・70以上B・60以上C・それ未満Dを判定
2. `for`文で1から10までの合計を計算
3. 拡張for文で配列 `{10, 20, 30, 40}` の各要素を出力
4. `switch`文（矢印構文）で曜日番号から曜日名を出力

演習コードは `02-control-flow/Main.java`。コンパイル・実行して動作確認済み（B、合計55、10/20/30/40、水曜日）。

## つまずきの分析

今回は目立ったつまずきなし。インデント・コメントも整理されていて読みやすかった。
