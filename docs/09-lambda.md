# 09. ラムダ式

## 用語集

- **ラムダ式**: その場で使い捨ての処理（関数）を書ける短い構文（`(引数) -> 処理`）。JSのアロー関数に近い
- **関数型インターフェース**: 抽象メソッドを1つだけ持つインターフェース。ラムダ式を代入できる対象になる
- **メソッド参照**: 既存のメソッドをラムダ式の代わりとして渡す省略記法（例: `String::compareTo`）

## 使い方

### 抽象メソッドとは（おさらい）

**抽象メソッドとは、中身（`{}`の処理）を持たず、シグネチャ（名前・引数・戻り値の型）だけを宣言したメソッド**（[13. 抽象クラス](13-abstract.md)参照）。

```java
boolean test(T t);   // 抽象メソッド：中身が無く、セミコロンで終わる
void accept(T t) {   // 普通のメソッド：中身（{}の処理）がある
    System.out.println(t);
}
```

### 関数型インターフェース

```java
interface Operation {
    int apply(int a, int b); // 抽象メソッドを1つだけ持つ
}
```

ラムダ式は「抽象メソッドを1つだけ持つインターフェース（関数型インターフェース）」に対してのみ使える。

**関数型インターフェースとは「メソッドが1つしかないインターフェース」ではない**。`default`メソッドや`static`メソッド（[07. インターフェース](07-interface.md)）はいくつ持っていてもよく、カウントされない。条件はあくまで「**抽象メソッドがちょうど1つ**」であること。

```java
interface Notifiable {
    String getMessage();                    // 抽象メソッド（1つ）
    default void sendNotification() { ... } // defaultメソッドは何個あってもOK
}
```

**なぜ「ちょうど1つ」でなければならないか**：ラムダ式`(a, b) -> a + b`には名前が付いていない（匿名クラスの`@Override public int apply(...)`のように「どのメソッドの実装か」を明示する情報が無い）。抽象メソッドが2つあると、コンパイラは「どちらの実装のつもりか」を判断できずエラーになる。

```java
interface TwoMethods {
    int apply(int a, int b);
    int reverseApply(int a, int b); // 抽象メソッドが2つ
}
TwoMethods t = (a, b) -> a + b; // エラー: TwoMethodsは機能インタフェースではありません
```

抽象メソッドが1つだけなら、他に選択肢が無いため「当然それの実装だ」と迷わず決まる。この「迷わず決まる」ことこそが、関数型インターフェースの条件の本質。

### ラムダ式の書き方

```java
Operation add = (a, b) -> a + b;
Operation multiply = (a, b) -> {
    int result = a * b;
    return result;
};
```

JSのアロー関数 `(a, b) => a + b` とほぼ同じ書き方。ただしJavaでは、代入先の型（関数型インターフェース）を満たすものとして扱われる。

### ラムダ式の正体：匿名クラスの省略記法

**「インターフェースを定義すること」と「ラムダ式を書くこと」は別の役割**なので混同しないよう注意する。

- インターフェースの定義（`interface Operation { int apply(int a, int b); }`）→ 「どんな形の処理を受け付けるか」という**契約（型）**を決めているだけ
- ラムダ式（`(a, b) -> a + b`）→ その契約の中身を実際に**実装**したもの

ラムダ式が登場する前は、この「実装」を匿名クラスというやや冗長な書き方で表現していた。

```java
// ラムダ式がなかった頃の書き方（匿名クラス）
Operation add = new Operation() {
    @Override
    public int apply(int a, int b) {
        return a + b;
    }
};

// ラムダ式（↑と全く同じ意味）
Operation add = (a, b) -> a + b;
```

つまりラムダ式は「`new Operation() { @Override public int apply(...) {...} }`という決まり文句（クラス名・メソッド名・`@Override`）を省略して、`引数 -> 処理`だけ書けばよくなった**省略記法**」であり、「1行で書けること」自体が定義ではない（`multiply`の例のように複数行でも書ける）。

### よく使う場面: Comparator

```java
List<String> names = new ArrayList<>(List.of("Charlie", "Alice", "Bob"));
names.sort((a, b) -> a.compareTo(b));
```

`Comparator`も`compare(a, b)`という抽象メソッド1つだけを持つ関数型インターフェースなので、ラムダ式を渡せる。

#### `compareTo`とラムダ式の関係（混同しやすいポイント）

ここには**別々の2つの仕組み**が重なっている。

1. **`Comparator`（ラムダ式で実装する側）**: `sort()`が要求する型。抽象メソッド`compare(a, b)`を持つ。ここにラムダ式を渡している
2. **`compareTo`（ラムダ式の中で呼んでいる側）**: `String`や`Integer`が最初から持っている、ラムダ式とは無関係な普通のメソッド（`Comparable`インターフェース由来）。2つの値を比べて、小さければ負・等しければ0・大きければ正の数を返す

`Comparator.compare`が返すべき値の形と`compareTo`が返す値の形がたまたま完全に一致しているため、「`compare`の中身を自分で書く代わりに、既にある`compareTo`の結果をそのまま使う」のが`(a, b) -> a.compareTo(b)`という書き方。`compareTo`はラムダ式の一部ではなく、**ラムダ式の中で呼び出している既存の別メソッド**という位置づけ。

```java
(a, b) -> a.compareTo(b) // 昇順
(a, b) -> b.compareTo(a) // aとbを入れ替えるので符号が逆転し降順になる
```

### java.util.functionの標準の関数型インターフェース

`Operation`のように毎回自作しなくても、Javaは`java.util.function`パッケージに「よく使う形」の関数型インターフェースを標準で用意している。`08-collections`（`removeIf`）、`09-lambda`（`sort`/`Comparator`）、そして`forEach`（`Consumer`）で、実は既に3種類を使っている。

| インターフェース | 抽象メソッド | 形（引数→戻り値） | 使用例 |
|---|---|---|---|
| `Predicate<T>` | `boolean test(T t)` | `T → boolean` | `list.removeIf(x -> ...)` |
| `Consumer<T>` | `void accept(T t)` | `T → void` | `list.forEach(x -> ...)` |
| `Comparator<T>` | `int compare(T a, T b)` | `(T, T) → int` | `list.sort((a, b) -> ...)` |

```java
List<String> books = new ArrayList<>();
books.removeIf(book -> !book.contains("入門"));      // Predicate: testの実装
books.sort((a, b) -> a.compareTo(b));                 // Comparator: compareの実装
books.forEach(book -> System.out.println("・" + book)); // Consumer: acceptの実装
```

ラムダ式の書き方（`(引数) -> 処理`）はどれも同じ。違うのは、渡す先のメソッド（`removeIf`/`sort`/`forEach`）が「どの関数型インターフェースの、どの抽象メソッドの実装を要求しているか」だけ。

### さらに簡潔な書き方（メソッド参照）

```java
names.sort(Comparator.naturalOrder());
names.sort(String::compareTo); // メソッド参照
```

## 覚えておくべきルール・規約

- ラムダ式が使えるのは「抽象メソッドを1つだけ持つインターフェース（関数型インターフェース）」のみ。`default`/`static`メソッドは何個あってもよく、この「1つ」にはカウントされない
- コンパクトになっているのはインターフェースの**定義**ではなく、インターフェースの**実装（中身）**。インターフェース自体は普通に`interface`で定義する
- `java.util.function`には`Predicate<T>`（`T→boolean`）、`Consumer<T>`（`T→void`）、`Comparator<T>`（`(T,T)→int`）のような標準の関数型インターフェースが用意されている。`removeIf`/`forEach`/`sort`はそれぞれこれらを受け取っている
- JSのアロー関数と書き方は近いが、Javaでは代入先の型（関数型インターフェース）で扱われる点が違う
- `Comparator`へのラムダ式は実務で頻出。`String::compareTo`のようなメソッド参照でさらに簡潔に書けることもある
- ラムダ式は「1行で書けるもの」という定義ではない。匿名クラス（`new インターフェース名() { @Override ... }`）の冗長な書き方を省略した記法、というのが正しい理解
- `(a, b) -> a.compareTo(b)`の`compareTo`は、`Comparator`とは別に元々`Integer`/`String`が持っているメソッド。ラムダ式の中で呼び出している「既存の部品」であり、ラムダ式の構文そのものではない

## 演習

`09-lambda/Main.java`にて以下を実装。

1. `interface Operation { int apply(int a, int b); }` を定義
2. ラムダ式で足し算・掛け算の`Operation`を作成し呼び出す
3. `List<String>`をラムダ式の`Comparator`でアルファベット順にソート

## つまずきの分析

- 初回、ソート対象のリストを`List.of("Alice", "Bob", "Charlie")`と最初からソート済みの順番で用意してしまい、ソートが実際に機能しているか検証できていなかった → `List.of("Charlie", "Alice", "Bob")`のように順不同で用意し直して動作確認
- **教訓**: 処理の正しさを検証するテストデータは、「たまたま結果が合っている」ケースを避けるため、意図的に崩した状態で用意する
- 分散学習の復習チェックポイント（`review/08-lamda`）自体は正解したが、事後の質問で「ラムダ式＝インターフェースの型を定義すること」「ラムダ式＝1行で処理を完結させること」という2つの誤解があることが判明
  - **教訓**: ラムダ式は「関数型インターフェースという契約に対する、匿名クラスの省略記法」。インターフェース定義（契約）とラムダ式（実装）は別の役割であり、行数の長さは定義に関係ない

演習コードは `09-lambda/Main.java`。コンパイル・実行して動作確認済み（足し算8、掛け算15、ソート後[Alice, Bob, Charlie]）。

### 復習（`review/33-lambda`）：用語の再確認に時間がかかった

**何が起きたか**: `Predicate`/`Consumer`/`Comparator`という3つの標準関数型インターフェースを使った実装自体はスムーズにできたが、事後の質疑応答で「関数型インターフェース＝メソッドが1つしかないインターフェース（`default`メソッドも含めて1つという誤解）」「ラムダ式＝関数型インターフェースの定義自体をコンパクトにしたもの（実装ではなく定義を圧縮していると誤解）」「抽象メソッドとは何か」を、それぞれ改めて確認する必要があった。

**なぜ**: 個々の実装パターン（`removeIf`/`forEach`/`sort`にラムダ式を渡す）は手が覚えていたが、その背後にある用語（抽象メソッド・関数型インターフェース・ラムダ式の関係）の言語化は、時間が経つと薄れやすい。特に「関数型インターフェース＝メソッドが1つだけ」という表現が、「`default`メソッドも含めて合計1つ」という意味に誤解されやすい。

**教訓**: 「関数型インターフェースの条件は**抽象メソッドがちょうど1つ**（`default`/`static`はカウント外）」「ラムダ式が省略しているのはインターフェースの**実装**であって**定義**ではない」という2点は特に混同しやすいので、次回の復習でも重点的に確認する。実際に2つの抽象メソッドを持つインターフェースにラムダ式を渡してコンパイルエラーを見る、という実験が「なぜちょうど1つなのか」の理解に効果的だった。

演習コードは `review/33-lambda/Main.java`。コンパイル・実行して動作確認済み（・Java入門、・Python入門、・Ruby入門）。
