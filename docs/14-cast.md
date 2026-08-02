# 14. キャストとinstanceof

## 使い方

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

演習コードは `14-cast/Employee.java`, `FullTimeEmployee.java`, `PartTimeEmployee.java`, `Main.java`。コンパイル・実行して動作確認済み（正社員: 3000.0、パート: 20.0円 × 80時間）。
