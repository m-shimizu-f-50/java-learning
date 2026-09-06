# 14. キャストとinstanceof

## 用語集

- **キャスト**: ある型の値を、別の型として扱えるように変換すること
- **アップキャスト**: 子クラス→親クラスへの変換。自動で行われ、安全
- **ダウンキャスト**: 親クラス→子クラスへの変換。明示的に`(型名)`を書く必要があり、実体が違う型だと危険
- **instanceof**: 変数が実際に指しているオブジェクトが、特定の型かどうかを判定する演算子
- **ClassCastException**: ダウンキャストした型と実体の型が違うときに、実行時に発生する例外

## 使い方

### なぜダウンキャストが必要になるのか：コンパイラは静的型だけを見る

```java
Employee[] employees = { new FullTimeEmployee("Alice", 3000), new PartTimeEmployee("Bob", 20, 80) };
employees[0].monthlySalary; // コンパイルエラー！
```

`employees[0]`の実体（動的型）は本当に`FullTimeEmployee`だが、これは**コンパイルエラーになる**。理由は、`07-interface`で学んだ「静的型」「動的型」の区別にある。

- **動的型**：実際に`new`で作られた実体の型（`FullTimeEmployee`）。プログラムを**実行して初めて**分かる
- **静的型**：変数・配列を宣言した時の型（`Employee`）。**コンパイル時に**決まっている

コンパイラはプログラムを実行する前（コンパイル時）にチェックを行うため、この時点では動的型を知りようがない。見えるのは静的型（`Employee`）だけで、`Employee`というクラスの設計図の中に`monthlySalary`は存在しないため、**実体が何であるかに関係なく**エラーになる。「インスタンス化されているから使える」わけではなく、「変数の静的型にそのメンバーが定義されているか」だけで判断される。

### アップキャスト（自動・安全）

```java
FullTimeEmployee fte = new FullTimeEmployee("Alice", 3000);
Employee e = fte; // 子→親は自動で行われる
```

### ダウンキャスト（明示的・危険）

```java
Employee e = new FullTimeEmployee("Alice", 3000);
FullTimeEmployee fte = (FullTimeEmployee) e; // 親→子は明示的なキャストが必要
```

実体と違う型にダウンキャストしようとすると、コンパイルは通るが**実行時に`ClassCastException`が発生する**。

### instanceofで安全に確認

```java
if (e instanceof FullTimeEmployee) {
    FullTimeEmployee fte = (FullTimeEmployee) e;
    System.out.println(fte.monthlySalary);
}
```

### パターンマッチング構文（Java 16以降、推奨）

```java
if (e instanceof FullTimeEmployee fte) {
    // instanceofがtrueの場合、自動的にキャスト済みのfteが使える
    System.out.println(fte.monthlySalary);
}
```

`instanceof`のチェックとキャストを同時に行える省略記法。同じ変数を2回キャストする必要がなくなる。

## 覚えておくべきルール・規約

- 子→親（アップキャスト）は自動、親→子（ダウンキャスト）は明示的なキャストが必要
- ダウンキャストは実体の型が違うと`ClassCastException`（実行時エラー）になる
- 「変数の静的型にメンバーが定義されているか」でコンパイラは判断する。実体（動的型）が何であるかはコンパイル時には関係ない
- ダウンキャスト前に`instanceof`で型を確認するのが安全
- Java 16以降は`instanceof`とキャストを同時に行うパターンマッチング構文が使え、同じキャストの重複を避けられる

## 演習

`14-cast/Main.java`にて（`13-abstract`の`Employee`/`FullTimeEmployee`/`PartTimeEmployee`を再利用）以下を実装。

1. `Employee[]`配列に`FullTimeEmployee`と`PartTimeEmployee`のインスタンスを入れる
2. 拡張for文＋`instanceof`（パターンマッチング構文）で型をチェック
3. `FullTimeEmployee`なら`"正社員: " + monthlySalary`を出力
4. `PartTimeEmployee`なら`"パート: " + hourlyRate + "円 × " + hoursWorked + "時間"`を出力

## つまずきの分析

初回、`instanceof`での型チェック自体は正しくできていたが、`((PartTimeEmployee) employee)`のような明示的キャストを同じ行で2回書いており冗長だった。パターンマッチング構文（`employee instanceof PartTimeEmployee pte`）に書き直すことで、キャストの重複を解消。

### 「インスタンス化されているからアクセスできる」という誤解

**何が起きたか**: 復習チェックポイントで、「`employees[0].monthlySalary`がなぜコンパイルエラーになるか」という質問に対し、「インスタンス化しているためアクセスできるのでは」と回答した。実際には静的型（`Employee`）にそのメンバーが無いため、動的型（`FullTimeEmployee`）が何であるかに関係なくコンパイルエラーになる。

**なぜ**: 「実際に作られたオブジェクト（動的型）」を基準に考えてしまい、「コンパイラは実行前の時点では動的型を知りようがなく、静的型だけを見て判断する」という、静的型付け言語の基本的な制約を見落としていた。

**教訓**: 「コンパイルが通るか」は常に**静的型**（変数・配列の宣言時の型）を基準に判断される。動的型（実体）は実行時ポリモーフィズムには影響するが、コンパイル時のアクセス可否には影響しない。

演習コードは `14-cast/Employee.java`, `FullTimeEmployee.java`, `PartTimeEmployee.java`, `Main.java`。コンパイル・実行して動作確認済み（正社員: 3000.0、パート: 20.0円 × 80時間）。
