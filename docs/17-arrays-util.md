# 17. 配列ユーティリティ（Arraysクラス）

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
