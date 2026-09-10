# 21. Objectクラス

## 用語集

- **Objectクラス**: すべてのクラスが暗黙的に継承する最上位のクラス。明示的に`extends`しなくても、全クラスは自動的に`Object`のサブクラスになる
- **equals(Object o)**: オブジェクト同士が「等しいか」を判定するメソッド。デフォルト実装は`==`と同じ（参照比較）
- **hashCode()**: オブジェクトを整数値（ハッシュ値）に変換するメソッド。`HashSet`/`HashMap`が要素の重複判定を高速化するために内部で使う
- **toString()**: オブジェクトを文字列として表現するメソッド。`System.out.println(obj)`のように、オブジェクトを文字列として扱う場面で自動的に呼ばれる
- **equals/hashCodeの契約（contract）**: 「`equals()`がtrueを返す2つのオブジェクトは、必ず同じ`hashCode()`を返さなければならない」というルール。これを破ると`HashSet`/`HashMap`が正しく動作しなくなる

## 使い方

### デフォルトの挙動：参照比較

```java
public class Student {
    String name;
    int score;
    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
```

```java
Student s1 = new Student("Alice", 90);
Student s2 = new Student("Alice", 90);

s1 == s2;        // false（実体が違う）
s1.equals(s2);   // false（equalsをオーバーライドしていないため）
```

`equals()`を1行も書いていないクラスでは、`Object`から継承したデフォルトの`equals()`がそのまま使われる。デフォルトの実装は`==`と全く同じで、**実体（参照）が同じかどうか**しか見ない。フィールドの値が同じでも、`new`で作られた実体が別物なら`false`になる。

`String`の`.equals()`が値（内容）を比較してくれるのは、デフォルトの挙動ではなく、**`String`クラス自身が`equals()`を独自にオーバーライドしているから**。

### equals()をオーバーライドする

```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Student)) return false;
    Student s = (Student) o;
    return this.name.equals(s.name) && this.score == s.score;
}
```

「フィールドの値が同じなら等しいとみなしたい」場合は、`equals()`を明示的にオーバーライドする。

### equals()とhashCode()はセットでオーバーライドする

`equals()`だけをオーバーライドして`hashCode()`を元のまま（デフォルト）にすると、`HashSet`が正しく動かなくなる。

```java
// equalsだけオーバーライド、hashCodeは未オーバーライドの場合
Student s1 = new Student("Alice", 90);
Student s2 = new Student("Alice", 90);

s1.equals(s2);     // true（オーバーライドが効いている）

HashSet<Student> set = new HashSet<>();
set.add(s1);
set.add(s2);
set.size();         // 2 になってしまう！（1が期待値）
```

`HashSet`は全要素を`equals()`で総当たり比較するのではなく、**まず`hashCode()`で置き場所（バケット）を絞り込み、同じ場所にあるものだけを`equals()`で細かく比較する**という2段階の仕組みで動いている。`s1`と`s2`は`hashCode()`が違う値（`Object`のデフォルトはオブジェクトごとにバラバラ）を返すため、そもそも別のバケットに置かれ、`equals()`で比較される機会を失う。結果、内容的には等しい（`equals()`がtrue）はずの2つが、別物として扱われてしまう。

これを避けるため、`equals()`をオーバーライドしたら`hashCode()`も必ずセットでオーバーライドし、**同じフィールドを基準に計算する**。

```java
@Override
public int hashCode() {
    return Objects.hash(name, score); // nameとscoreからハッシュ値を計算
}
```

`java.util.Objects.hash(...)`を使うと、複数フィールドから一貫したハッシュ値を簡単に計算できる。

### toString()をオーバーライドする

```java
@Override
public String toString() {
    return "Student{name=" + name + ", score=" + score + "}";
}
```

オーバーライドしないと、`println(obj)`は`クラス名@ハッシュコードの16進数`（例：`Student@1b6d3586`）という人間には意味のない文字列を出力する。デバッグ・ログ出力のために、意味のある文字列を返すようオーバーライドするのが一般的。

## 覚えておくべきルール・規約

- すべてのクラスは暗黙的に`Object`を継承しており、`equals()`/`hashCode()`/`toString()`をデフォルトで持っている
- `equals()`のデフォルトは`==`と同じ（参照比較）。「全クラスがデフォルトで値比較してくれる」というのは誤解（`String`はクラス自身がオーバーライドしているだけ）
- `equals()`をオーバーライドしたら`hashCode()`も必ずセットでオーバーライドする（同じフィールドを基準に計算する）
- `hashCode()`の実装には`Objects.hash(フィールド1, フィールド2, ...)`を使うと簡単
- `equals()`/`hashCode()`/`toString()`をオーバーライドする際は、他のオーバーライドと同様に`@Override`を必ず付ける

## 演習

`review/21-object-class/`にて以下を実装。

1. `Point`クラス：`x`, `y`フィールド、コンストラクタ
2. `equals(Object o)`をオーバーライド：`x`と`y`が両方同じなら等しいと判定
3. `hashCode()`をオーバーライド：`equals()`と一貫性を持たせる（`Objects.hash(x, y)`）
4. `toString()`をオーバーライド：`"Point(x, y)"`形式で出力
5. `Main`：同じ座標の`Point`を2つ作り、`==`/`equals()`の結果を確認、`HashSet<Point>`に両方入れてサイズが`1`になることを確認、`println`で`toString()`が効いていることを確認

演習コードは `review/21-object-class/Point.java`, `Main.java`。コンパイル・実行して動作確認済み（`equals`: true, `==`: false, `toString`: `Point(2, 3)`, 両者の`hashCode`が一致、`HashSet`サイズ: 1）。

## つまずきの分析

### 「equalsはデフォルトで値を比較してくれる」という誤解

**何が起きたか**: `Student`クラス（`equals()`未オーバーライド）で`s1.equals(s2)`（値は同一、実体は別）を予測した際、「値の比較になるのでtrue」と回答した。実際には`Object`のデフォルト`equals()`は`==`と同じ参照比較で、結果は`false`。

**なぜ**: `String`の`.equals()`が値を比較する動作を、「Javaの`equals()`は元々そういうもの」だと一般化してしまっていた。実際には`String`クラス自身が`equals()`を独自にオーバーライドしているだけで、デフォルトの挙動ではない。

**教訓**: `equals()`の挙動は「そのクラスが何を書いているか」で決まる。オーバーライドしていないクラスは、常にデフォルト（`Object`の参照比較）が使われる。「よく使うクラスがそうなっているから、全部そうなっている」と決めつけず、そのクラス自身が何を実装しているかを確認する。

### `equals()`だけをオーバーライドし`hashCode()`を忘れると`HashSet`が壊れる

**何が起きたか**: `equals()`だけをオーバーライドし`hashCode()`を未オーバーライドのまま`HashSet`に同一内容の2オブジェクトを入れたところ、`equals()`はtrueなのに`HashSet`のサイズが`2`になった（実際にコードを実行して確認）。

**なぜ**: `HashSet`は`hashCode()`でバケットを絞り込んだ上で`equals()`比較する2段階の仕組みのため、`hashCode()`が食い違うと`equals()`が比較される機会自体を失う。

**教訓**: `equals()`と`hashCode()`は「同じ基準（同じフィールド）」で計算するというセットのルール。片方だけオーバーライドすると、コンパイルは通っても`HashSet`/`HashMap`が静かに壊れる（実行時にしか発覚しない）ため要注意。

### 初回実装での指摘事項

初回、`equals()`/`toString()`/`hashCode()`のロジック自体は正しかったが、`@Override`アノテーションが3つとも付いていなかった（`06-inheritance`の慣習の見落とし）のと、`hashCode()`の値を2つ並べて表示するだけで、実際に`HashSet`で重複が1つにまとめられることまでは確認していなかった。両方を追加して解決。
