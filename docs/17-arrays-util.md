# 17. 配列ユーティリティ（Arraysクラス）

## いつ使うか

「配列のソート・操作・検索をするためのもの」という理解で合っている。具体的には以下のような場面で使う。

1. **ソートして順位・順番を作りたい**: 例）「テストの点数配列を並べ替えて上位を確認する」「ランキング表示のために昇順/降順に並べる」
2. **配列の中身をログや画面に分かりやすく出したい**: 例）デバッグ中に配列の中身を確認したい → `Arrays.toString()`
3. **配列の一部だけ切り出したい**: 例）「ページング処理で、全件のうち先頭10件だけ表示する」→ `Arrays.copyOfRange()`
4. **初期値で配列を一括初期化したい**: 例）「集計用の配列を全部0でリセットする」→ `Arrays.fill()`
5. **大量データの中から特定の値を高速に探したい**: 例）「ソート済みの数十万件の配列からある値の位置を探す」→ `Arrays.binarySearch()`（線形に1件ずつforで探すより高速）
6. **配列同士が同じ内容か比較したい**: 例）「期待する結果の配列と、実際の計算結果の配列が一致するかテストで確認する」→ `Arrays.equals()`

### 使い分けの目安: 配列が向く場面 / ArrayListが向く場面

「17-arrays-util」で扱うのは**配列（固定長）**の操作。件数が増減する・頻繁に要素を足し引きするデータは`08-collections`で学んだ`ArrayList`の方が向いている。「サイズが決まっている数値・文字列の塊を、まとめてソート・検索・整形したい」時に`Arrays`クラスの出番、とイメージすると使い分けやすい。

## 使い方

### よく使うメソッド

```java
import java.util.Arrays;

int[] nums = {5, 2, 8, 1, 9};

Arrays.sort(nums);                          // ソート
Arrays.toString(nums);                      // "[1, 2, 5, 8, 9]"
Arrays.copyOf(nums, nums.length);           // コピー
Arrays.copyOfRange(nums, 1, 4);             // 範囲コピー（インデックス1〜3）
Arrays.binarySearch(nums, 8);               // 二分探索（ソート済みが前提）
Arrays.equals(nums, copy);                  // 配列同士の内容比較
Arrays.fill(filled, 7);                     // 全要素を同じ値で埋める
```

### なぜArrays.toString()が必要か

配列を直接`println`すると`[I@1b6d3586`のような参照情報が出る（配列は`Object`のサブタイプで`toString()`をオーバーライドしていないため）。中身を見るには`Arrays.toString()`を使う。

### binarySearchの引数

`Arrays.binarySearch(配列, 探したい値)`。第2引数は「探したい値」であって、インデックスではない。見つかればそのインデックス、見つからなければ負の値を返す。ソート済み配列が前提（未ソートだと結果が保証されない）。

### Arrays.asList()の注意点

```java
List<Integer> list = Arrays.asList(arr); // 固定長リストになる（add/removeでエラー）
List<Integer> mutable = new ArrayList<>(Arrays.asList(arr)); // 可変長にしたい場合はArrayListで包む
```

## 覚えておくべきルール・規約

- 配列の中身を見るときは`Arrays.toString()`を使う（直接`println`しない）
- `binarySearch`はソート済み配列が前提
- `Arrays.asList()`が返すリストは固定長

## 演習

`17-arrays-util/Main.java`にて以下を実装。

1. `int[] nums = {8, 3, 1, 9, 5};` を`Arrays.sort()`でソートし出力
2. `Arrays.copyOfRange()`で最初の3要素を取り出して出力
3. `Arrays.fill()`で長さ4の配列を0で埋めて出力
4. `Arrays.binarySearch(nums, 9)`でインデックスを出力

## つまずきの分析

初回、`java.util.Arrays`とは無関係な`java.lang.reflect.Array`が誤ってimportされていた（未使用のまま残っていた）。動作には影響しないが、コミット前に削除して整理した。

演習コードは `17-arrays-util/Main.java`。コンパイル・実行して動作確認済み（[1, 3, 5, 8, 9] / [1, 3, 5] / [0, 0, 0, 0] / インデックス4）。
