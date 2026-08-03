# 15. 例外処理

## 使い方

### try-catch-finally

```java
try {
    int result = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("エラー: " + e.getMessage());
} finally {
    System.out.println("finallyは必ず実行される");
}
```

`finally`は例外が発生してもしなくても必ず実行される。

### Checked例外とUnchecked例外

| | Checked例外 | Unchecked例外 |
|---|---|---|
| 継承元 | `Exception`（`RuntimeException`以外） | `RuntimeException` |
| コンパイラのチェック | `try-catch`または`throws`が必須 | 任意 |
| 具体例 | `IOException`, `SQLException` | `NullPointerException`, `ArithmeticException` |
| 意味合い | 予測できる外部要因のエラー | プログラムのバグによるエラー |

JSの例外にはこの区別がない。

### 独自の例外クラス

```java
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
```

```java
static double withdraw(double balance, double amount) throws InsufficientFundsException {
    if (amount > balance) {
        throw new InsufficientFundsException("残高不足です");
    }
    return balance - amount;
}
```

### try-with-resources

```java
try (FileReader reader = new FileReader("data.txt")) {
    // ...
} // 自動的にreader.close()が呼ばれる
```

## 覚えておくべきルール・規約

- Checked例外（`Exception`継承）は`throws`宣言または`try-catch`が必須。書かないとコンパイルエラーになる
- Unchecked例外（`RuntimeException`継承）はコンパイラのチェック対象外
- `finally`は例外の有無にかかわらず必ず実行される
- 「データ（フィールド）を持たず、引数を受け取って計算するだけの処理」は独立したクラスにせず`static`メソッドで十分（`11-encapsulation`のような状態を持つクラスとの使い分け）
- 独自の例外クラスは`Exception`を継承し、コンストラクタで`super(message)`を呼ぶのが基本形

## 演習

`15-exception/InsufficientFundsException.java`, `Main.java`にて以下を実装。

1. `InsufficientFundsException`（`Exception`継承のChecked例外）
2. `withdraw(double balance, double amount)`: 残高不足なら例外をthrow、それ以外は`balance - amount`を返す
3. `withdraw(1000, 1500)`を`try-catch`で呼び出し、例外メッセージを出力
4. `withdraw(1000, 500)`を呼び出し、正常な残高を出力
5. `finally`で「処理が終了しました」を出力

## つまずきの分析

- 独自例外クラス・`throws`・`throw`・`try-catch-finally`の実装自体は初回から正しかったが、`withdraw(1000, 1500)`（異常系）のテストのみで、`withdraw(1000, 500)`（正常系）の動作確認が漏れていた
- **教訓**: 例外処理の実装では「例外が発生するケース」だけでなく「正常に完了するケース」も必ずテストする。特に`finally`は両方のケースで実行されることを確認して初めて「必ず実行される」という性質を体感できる

演習コードは `15-exception/InsufficientFundsException.java`, `Main.java`。コンパイル・実行して動作確認済み（異常系: 残高不足です→処理が終了しました、正常系: 新しい残高500.0→処理が終了しました）。
