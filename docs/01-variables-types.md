# 01. 変数と型

## 要点

- Javaは静的型付け言語。変数宣言時に型を明示し、後から別の型は代入できない。
- プリミティブ型: `int`, `double`, `boolean`, `char`, `long`, `float`, `byte`, `short`
- 参照型: `String`, 配列, クラスなど（オブジェクトへの参照を保持）
- `final` をつけると再代入不可（JSの `const` 相当）

```java
int age = 25;
double price = 19.99;
boolean isActive = true;
char grade = 'A';
String name = "Alice";
final double TAX_RATE = 0.1;
```

## 演習

1. 整数型の変数 `quantity` に `3` を代入
2. 小数型の変数 `unitPrice` に `250.5` を代入
3. `quantity * unitPrice` の合計金額を計算し `double` 型の変数 `total` に格納
4. `final` を使って税率 `0.1` を定数として宣言
5. 合計金額に税率を加えた金額を標準出力に表示

## つまずいた点・フィードバック

- Javaは全ての実行コードをクラス＋`main`メソッドの中に書く必要がある（JSのようにトップレベルに文を書けない）
- `public class`名とファイル名は一致させる必要がある（数字始まり・ハイフンは不可）
- `double`は2進数表現のため`0.1`のような小数を正確に表現できず、計算結果に微小な誤差が出る（例: `826.65` → `826.6500000000001`）
  - 実務で金額を正確に扱う場合は`BigDecimal`を使う（応用トピックとして今後扱う）
- 演習コードは `01-variables-types/Main.java` に配置。コンパイル・実行して動作確認済み。
