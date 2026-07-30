# 09. ラムダ式

## 使い方

### 関数型インターフェース

```java
interface Operation {
    int apply(int a, int b); // 抽象メソッドを1つだけ持つ
}
```

ラムダ式は「抽象メソッドを1つだけ持つインターフェース（関数型インターフェース）」に対してのみ使える。

### ラムダ式の書き方

```java
Operation add = (a, b) -> a + b;
Operation multiply = (a, b) -> {
    int result = a * b;
    return result;
};
```

JSのアロー関数 `(a, b) => a + b` とほぼ同じ書き方。ただしJavaでは、代入先の型（関数型インターフェース）を満たすものとして扱われる。

### よく使う場面: Comparator

```java
List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob"));
names.sort((a, b) -> a.compareTo(b));
```

`Comparator`も`compare(a, b)`という抽象メソッド1つだけを持つ関数型インターフェースなので、ラムダ式を渡せる。

### さらに簡潔な書き方（メソッド参照）

```java
names.sort(Comparator.naturalOrder());
names.sort(String::compareTo); // メソッド参照
```

## 覚えておくべきルール・規約

- ラムダ式が使えるのは「抽象メソッドを1つだけ持つインターフェース（関数型インターフェース）」のみ
- JSのアロー関数と書き方は近いが、Javaでは代入先の型（関数型インターフェース）で扱われる点が違う
- `Comparator`へのラムダ式は実務で頻出。`String::compareTo`のようなメソッド参照でさらに簡潔に書けることもある

## 演習

`09-lambda/Main.java`にて以下を実装。

1. `interface Operation { int apply(int a, int b); }` を定義
2. ラムダ式で足し算・掛け算の`Operation`を作成し呼び出す
3. `List<String>`をラムダ式の`Comparator`でアルファベット順にソート

## つまずきの分析

- 初回、ソート対象のリストを`List.of("Alice", "Bob", "Charlie")`と最初からソート済みの順番で用意してしまい、ソートが実際に機能しているか検証できていなかった → `List.of("Charlie", "Alice", "Bob")`のように順不同で用意し直して動作確認
- **教訓**: 処理の正しさを検証するテストデータは、「たまたま結果が合っている」ケースを避けるため、意図的に崩した状態で用意する

演習コードは `09-lambda/Main.java`。コンパイル・実行して動作確認済み（足し算8、掛け算15、ソート後[Alice, Bob, Charlie]）。
