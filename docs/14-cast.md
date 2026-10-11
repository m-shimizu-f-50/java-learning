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

```java
Beverage b = new Tea("緑茶");
Coffee c = (Coffee) b; // 実体はTea、Coffeeではない
c.brew();
// Exception in thread "main" java.lang.ClassCastException: class Tea cannot be cast to class Coffee
```

**なぜコンパイラが防げず、実行時に初めて検出されるのか**：コンパイラは`b`の静的型（`Beverage`）しか見えない。`Coffee`も`Tea`も`Beverage`のサブクラスなので、「`b`の実体がたまたま`Coffee`である可能性」をコンパイラは否定できず、ダウンキャストの構文自体は許可する（可能性があるから許可、というだけで保証ではない）。実行時には、Javaのオブジェクトが内部に持っている動的型の情報をJVMが実際にチェックし、一致しなければその場で`ClassCastException`を投げる。

`ClassCastException`は`RuntimeException`の子孫、つまり**Unchecked例外**（[15. 例外処理](15-exception.md)）。`try-catch`も`throws`宣言も無くてもコンパイルは通り、実行時にしか検出されない。

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

`instanceof`は、ダウンキャストが実行時に行っているのと**全く同じ「実体は本当にその型か」というチェック**をJVM内部で行っている。違うのはチェックに失敗したときの振る舞いだけ。

| | チェックに失敗したら |
|---|---|
| `(Coffee) b`（キャスト） | `ClassCastException`を**投げる**（プログラムが止まる） |
| `b instanceof Coffee`（判定） | `false`を**返す**（プログラムは止まらず、`if`で分岐できる） |

### 実務でキャストが必要になる場面、使いすぎに注意

明示的なダウンキャスト（`(型名)`や`instanceof`での分岐）は、普段のアプリケーションコードではそれほど頻繁には出てこない。理由は、ジェネリクス（[08. コレクション](08-collections.md)）の普及でコレクションからの取り出し時のキャストがほぼ不要になったことと、ポリモーフィズムで設計すれば型ごとの分岐自体が不要になる場面が多いため。

それでも以下のような場面では登場する。

- ポリモーフィックなコレクション（`Staff[]`に`Manager`/`Intern`が混在、など）から、特定のサブクラスだけの機能を呼びたいとき
- Spring Securityで`Authentication.getPrincipal()`を独自の`UserDetails`実装にキャストするなど、外部ライブラリ・フレームワークが汎用的な型で値を返してくるとき

`instanceof`＋キャストの分岐が増えてきたら、「本来は`13-abstract`のように、`abstract`メソッドとして親クラスに持たせ、ポリモーフィズムで解決できないか」を検討する価値がある。キャストは便利だが、設計で解決しきれない場面の最終手段という位置づけ。

なお、`extends`による**アップキャスト**（サブクラスを親クラスの変数に代入するなど）は自動で行われ、普段意識しないだけで実は日常的に起きている。少ないのは「明示的に`(型名)`と書くダウンキャスト」の方。

## 覚えておくべきルール・規約

- 子→親（アップキャスト）は自動、親→子（ダウンキャスト）は明示的なキャストが必要
- ダウンキャストは実体の型が違うと`ClassCastException`（実行時エラー）になる
- 「変数の静的型にメンバーが定義されているか」でコンパイラは判断する。実体（動的型）が何であるかはコンパイル時には関係ない
- ダウンキャスト前に`instanceof`で型を確認するのが安全
- Java 16以降は`instanceof`とキャストを同時に行うパターンマッチング構文が使え、同じキャストの重複を避けられる
- `ClassCastException`はUnchecked例外（`RuntimeException`の子孫）。コンパイルは通り、実行時にしか検出されない
- 明示的なダウンキャストは実務では比較的少ない（ジェネリクス・ポリモーフィズムで代替できることが多い）。`instanceof`の分岐が増えてきたら設計の見直しを検討する

## 演習

`exercises/14-cast/Main.java`にて（`13-abstract`の`Employee`/`FullTimeEmployee`/`PartTimeEmployee`を再利用）以下を実装。

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

演習コードは `exercises/14-cast/Employee.java`, `FullTimeEmployee.java`, `PartTimeEmployee.java`, `Main.java`。コンパイル・実行して動作確認済み（正社員: 3000.0、パート: 20.0円 × 80時間）。

### 復習（`review/38-cast`）：パターンマッチング構文を使わずに書いてしまう

**何が起きたか**: `Staff[]`から`Manager`/`Intern`を判定する際、`if (staff instanceof Manager) { Manager manager = (Manager) staff; ... }`のように、`instanceof`の判定とキャストを別々に書いてしまった（`14-cast`本編のつまずきと同じパターン）。パターンマッチング構文（`if (staff instanceof Manager manager) { ... }`）に直すことで解消。

**なぜ**: 従来の書き方（JDTやネット上の古い情報に多い）に慣れており、Java 16以降の省略記法をとっさに使えなかった。

**教訓**: `instanceof`で型を判定した直後にキャストしている（同じ型名が2回出てくる）コードを見たら、パターンマッチング構文に書き換えられないか確認する習慣をつける。

演習コードは `review/38-cast/Staff.java`, `Manager.java`, `Intern.java`, `Main.java`。コンパイル・実行して動作確認済み（田中が予算を承認する、佐藤が研修を受ける）。
