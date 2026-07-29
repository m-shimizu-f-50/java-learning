# 08. コレクション（ArrayList）

## 使い方

### 宣言と基本操作

```java
import java.util.ArrayList;
import java.util.List;

List<String> names = new ArrayList<>();
names.add("Alice");
names.get(0);
names.remove(0);   // インデックス指定で削除
names.size();
names.contains("Bob");
```

配列は固定長だが、`ArrayList`は要素数を自由に増減できる。JSの`push`/`splice`に近い感覚。

### ジェネリクスとインターフェース

`List<String> names = new ArrayList<>();` の左辺は`List`（インターフェース）、右辺は`ArrayList`（実装クラス）。「`List`という契約を満たすものとして`ArrayList`を使う」というインターフェースの実践例。

プリミティブ型は直接入れられない（`List<int>`はエラー）。`List<Integer>`のようにラッパークラスを使う（オートボクシングで自動変換される）。

### remove()の2つのオーバーロード

```java
fruits.remove("バナナ"); // 値で削除（remove(Object)）
numbers.remove(2);       // インデックスで削除（remove(int)）— 値の2ではなく3番目の要素が消える
numbers.remove(Integer.valueOf(2)); // 値の2を削除したい場合はこう書く
```

`List<Integer>`に対して`remove()`を使うと、`int`（インデックス）と`Integer`（値）の両方に解釈できるため、コンパイラは常に`remove(int)`（インデックス扱い）を優先する。「値を消したいのにインデックスとして解釈されてしまう」事故が起きやすい。

## 覚えておくべきルール・規約

- 配列は固定長、`ArrayList`は可変長
- `List<T>`はインターフェース、`ArrayList<T>`は実装クラス。宣言は`List<T> x = new ArrayList<>();`とするのが一般的
- プリミティブ型はリストに直接入れられない（ラッパークラスを使う）
- `List<Integer>`への`remove()`はインデックスと値の解釈が衝突する。値を消したい場合は`Integer.valueOf(...)`で明示する

## 演習

`08-collections/Main.java`にて以下を実装。

1. `List<String> fruits`に3要素を`add`
2. 拡張for文で全要素出力
3. `remove`で1要素削除し、`size()`を出力
4. `List<Integer> numbers`に1〜5を`add`し、合計値を計算

## つまずきの分析

今回は目立ったつまずきなし。`remove(Object)`と`remove(int)`のオーバーロードの違いを解説し、`List<Integer>`特有の罠（値のつもりがインデックス扱いされる）を確認した。

演習コードは `08-collections/Main.java`。コンパイル・実行して動作確認済み（りんご/バナナ/ぶどう出力、削除後size=2、合計15）。
