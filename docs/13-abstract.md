# 13. 抽象クラス（abstract class）

## 使い方

### 定義

```java
abstract class Employee {
    String name;

    Employee(String name) {
        this.name = name;
    }

    void printName() { // 実装済みメソッド（共通処理）
        System.out.println("社員: " + name);
    }

    abstract double calculateSalary(); // 抽象メソッド（子クラスに実装を強制）
}
```

```java
class FullTimeEmployee extends Employee {
    double monthlySalary;

    FullTimeEmployee(String name, double monthlySalary) {
        super(name);
        this.monthlySalary = monthlySalary;
    }

    @Override
    double calculateSalary() {
        return monthlySalary;
    }
}
```

`Employee`は直接`new`できない（抽象クラスはインスタンス化不可）。共通する部分（`name`, `printName()`）は継承でそのまま使い回し、種類ごとに絶対に変わる部分（`calculateSalary()`）だけ各サブクラスに実装を強制する。

### インターフェースとの違い

| | インターフェース | 抽象クラス |
|---|---|---|
| 実装 | 一切持てない（契約のみ） | 一部実装済み、一部`abstract`にできる |
| フィールド・コンストラクタ | 実質持てない | 持てる |
| 継承数 | `implements`で複数可 | `extends`で1つだけ |

## 覚えておくべきルール・規約

- `abstract class`は直接インスタンス化できない
- `abstract`メソッドは中身を書かない（子クラスで必ずオーバーライドしないとコンパイルエラー）
- 「共通の実装 + 一部だけ強制的に実装させたい」場合は抽象クラス、「実装を一切持たず契約だけ」の場合はインターフェース
- 抽象メソッドにせず「空の実装（`return 0;`など）」にしてしまうと、サブクラスが実装を忘れてもコンパイルは通ってしまい、実行時までバグに気づけない。`abstract`にすることで実装忘れを**コンパイル時に検出**できる

## 演習

`13-abstract/`にて以下を実装。

1. `abstract class Employee`: `name`フィールド、コンストラクタ、実装済み`printName()`、抽象メソッド`calculateSalary()`
2. `FullTimeEmployee`（`Employee`継承）: `monthlySalary`をそのまま返す
3. `PartTimeEmployee`（`Employee`継承）: `hourlyRate * hoursWorked`を返す
4. `Main`: `Employee[]`配列に両方のインスタンスを入れ、拡張for文で`printName()`・`calculateSalary()`を呼び出し（ポリモーフィズムの復習）

## つまずきの分析

### コンストラクタの実装漏れによる2つのコンパイルエラー

**何が起きたか**: `PartTimeEmployee`にコンストラクタを1つも書かなかったため、以下2つのコンパイルエラーが発生した。

1. `Main`側: `new PartTimeEmployee("Bob", 20, 80)`に対応するコンストラクタが存在しない
2. `PartTimeEmployee`側: コンストラクタを書かないと自動生成される引数なしコンストラクタが、親`Employee`の引数なしコンストラクタ（存在しない）を暗黙に呼ぼうとしてエラーになる

**なぜ**: `FullTimeEmployee`ではコンストラクタを定義していたが、`PartTimeEmployee`では書き忘れていた。抽象クラスを継承する場合も、コンストラクタの実装ルールは通常の継承と同じであることの見落とし。

**教訓**: 親クラスに引数ありのコンストラクタしかない場合、子クラスは必ず`super(...)`を呼ぶ独自のコンストラクタを書く必要がある。コンストラクタを書き忘れると、コンパイラが自動生成した引数なし版が`super()`を呼ぼうとして矛盾が起きる。

演習コードは `13-abstract/Employee.java`, `FullTimeEmployee.java`, `PartTimeEmployee.java`, `Main.java`。コンパイル・実行して動作確認済み（Alice: 3000.0、Bob: 1600.0）。
