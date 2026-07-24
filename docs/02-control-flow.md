# 02. 制御構文

## 要点

- `if`の条件式は必ず`boolean`型（JSのように数値などの真偽判定はできない）
- `for`はJSと同じ書き方に加え、拡張for文（`for (int n : nums)`、JSの`for...of`相当）がある
- `while` / `do-while`はJSと同じ
- `switch`はJava 14以降、矢印構文（`case 1 -> ...`）でbreak不要な書き方が可能

```java
switch (day) {
    case 1 -> System.out.println("月曜");
    default -> System.out.println("不明");
}
```

## 演習

`02-control-flow/Main.java` にて以下を実装。

1. `int score = 75;` を用意し、`if-else if-else`で90以上A・70以上B・60以上C・それ未満Dを判定
2. `for`文で1から10までの合計を計算
3. 拡張for文で配列 `{10, 20, 30, 40}` の各要素を出力
4. `switch`文（矢印構文）で曜日番号から曜日名を出力

## つまずいた点・フィードバック

- 特につまずきなし。インデント・コメントも整理されていて読みやすかった。
- 豆知識: 想定外の値（`day`が1〜7の範囲外など）に備えて`switch`に`default`ケースを入れておくと実務では安全（今回の演習では範囲固定のため未対応でも問題なし）。
- 演習コードは `02-control-flow/Main.java`。コンパイル・実行して動作確認済み（B、合計55、10/20/30/40、水曜日）。
