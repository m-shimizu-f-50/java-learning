# 15. 例外処理

## 用語集

- **例外（Exception）**: プログラム実行中に発生した異常事態を表すオブジェクト
- **throw**: 実際に例外オブジェクトを発生させ、投げる命令
- **throws**: メソッドが例外を投げる可能性があることを、メソッドの宣言部分で予告するキーワード
- **Checked例外**: コンパイラが`try-catch`または`throws`宣言を強制する例外（`Exception`の子孫、`RuntimeException`を除く）
- **Unchecked例外**: コンパイラのチェック対象外の例外（`RuntimeException`の子孫）
- **Throwable**: `throw`/`catch`できる全てのクラスの最上位の親。`Exception`も`Error`もこの子孫

## 全体像（まずここを掴む）

例外処理は「異常が起きた時に、どう対応するか」を管理する仕組み。登場人物は4つだけ。

1. **例外クラス**: 「どんな異常が起きたか」を表すオブジェクトの設計図（例: `InsufficientFundsException`）
2. **`throw`**: その異常が実際に発生した瞬間、例外オブジェクトを`new`して投げる（動詞・命令）
3. **`throws`**: そのメソッドが「私は例外を投げる可能性があります」とメソッドの宣言部分で予告する（メソッド定義の一部、宣言）
4. **`try-catch-finally`**: 投げられた例外を呼び出し側で受け止めて処理する

`throw`と`throws`は綴りが似ていて紛らわしいが、役割は全く別物。

```java
static double withdraw(double balance, double amount) throws InsufficientFundsException {
    //                                                 ^^^^^^ 「このメソッドはこの例外を投げる可能性があります」という予告（メソッドの外側から見える）
    if (amount > balance) {
        throw new InsufficientFundsException("残高不足です");
        // ^^^^^ 実際にここで例外オブジェクトを生成して投げる（実行される命令）
    }
    return balance - amount;
}
```

## 独自の例外クラスの作り方

```java
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
```

これは`05-class`のクラス・コンストラクタと**構造は完全に同じ**。特別なのは`extends Exception`の部分だけ。

- **`extends Exception`の役割**: Javaでは`throw`/`catch`できるのは`Throwable`の子孫クラスだけ、というルールがある。`Exception`を継承することで、①`throw`/`catch`の対象にできる資格を得る、②`getMessage()`など便利なメソッドを自作せずに継承で手に入れる、の2つが同時に得られる
- **コンストラクタ`InsufficientFundsException(String message)`の役割**: `new InsufficientFundsException("残高不足です")`と書いたときに自動的に呼ばれ、渡された文字列を`super(message)`で親（`Exception`）に渡す。親が内部でその文字列を保持してくれるので、後から`e.getMessage()`で取り出せるようになる

## try-catch-finally（投げられた例外を受け止める側）

```java
try {
    double newBalance = withdraw(1000, 1500); // ここで例外が投げられる可能性がある
    System.out.println(newBalance); // 例外が起きたらこの行は実行されずcatchに飛ぶ
} catch (InsufficientFundsException e) {
    System.out.println("エラー: " + e.getMessage()); // 投げられた例外を受け止めて処理
} finally {
    System.out.println("finallyは必ず実行される"); // 例外の有無にかかわらず必ず実行
}
```

`withdraw`に`throws`が付いているので、呼び出す側は「例外が飛んでくるかもしれない」と分かる → だから`try-catch`で受け止める必要がある、という繋がりを意識する。

## Checked例外とUnchecked例外

| | Checked例外 | Unchecked例外 |
|---|---|---|
| 継承元 | `Exception`（`RuntimeException`以外） | `RuntimeException` |
| コンパイラのチェック | `try-catch`または`throws`が必須 | 任意 |
| 具体例 | `IOException`, `SQLException` | `NullPointerException`, `ArithmeticException` |
| 意味合い | 予測できる外部要因のエラー | プログラムのバグによるエラー |

JSの例外にはこの区別がない。

## try-with-resources

```java
try (FileReader reader = new FileReader("data.txt")) {
    // ...
} // 自動的にreader.close()が呼ばれる
```

## 覚えておくべきルール・規約

- `throws`はメソッドの宣言部分に書く「予告」、`throw`は実際に例外を投げる「実行文」。この2つは役割が別（綴りが似ているだけ）
- 独自の例外クラスは`Exception`を継承する。継承することで「投げられる資格」と「`getMessage()`などの機能」を同時に手に入れる
- 独自例外のコンストラクタは`super(message)`で親にメッセージを渡すのが基本形（`05-class`のコンストラクタと構造は同じ）
- Checked例外（`Exception`継承）は`throws`宣言または`try-catch`が必須。書かないとコンパイルエラーになる
- Unchecked例外（`RuntimeException`継承）はコンパイラのチェック対象外
- `finally`は例外の有無にかかわらず必ず実行される
- 「データ（フィールド）を持たず、引数を受け取って計算するだけの処理」は独立したクラスにせず`static`メソッドで十分（`11-encapsulation`のような状態を持つクラスとの使い分け）

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
- 分散学習の復習チェックポイント（`review/05-exception`）で、コードを見ずに再実装しようとした際につまずいた。「`Exception`を継承する役割」「コンストラクタが何をしているか」を言葉で説明できず、丸暗記に近い理解だったことが判明
  - **教訓**: `extends Exception`は「投げられる資格を得る」＋「`getMessage()`などの機能を継承で手に入れる」の2つの意味を持つ、とセットで覚える。コンストラクタは`05-class`と同じ「`new`時に自動で呼ばれ、値を初期化するもの」という基本に立ち返ると理解しやすい
- 境界値の条件ミス（`age == 0`と書いて`age < 0`のつもりになっていた）も発生。`10-stream`の`>`/`>=`取り違えと同じ系統のミスで、境界値ちょうどのテストケースがないと気づきにくい

演習コードは `15-exception/InsufficientFundsException.java`, `Main.java`。コンパイル・実行して動作確認済み（異常系: 残高不足です→処理が終了しました、正常系: 新しい残高500.0→処理が終了しました）。

### 復習（`review/23-exception`）：「Checked例外は呼び出し側で必ず対応する」ルールを忘れていた

**何が起きたか**: `throws`付きのメソッド（Checked例外）を`try-catch`も`throws`宣言もせずに呼び出すコードを提示し、「コンパイルが通るか」を聞いたところ、覚えていなかった。実際に`javac`でコンパイルし、`エラー: 例外〇〇は報告されません。スローするには、捕捉または宣言する必要があります`というメッセージを見て、初めて「呼び出し側は`try-catch`か`throws`のどちらかが必須」というルールを再確認した。

**なぜ**: `throws`が「予告」であることは理解していても、その予告に対して呼び出し側に何が義務付けられるか（無視は許されない、という強制力の部分）が記憶から抜けていた。

**教訓**: ルール自体を思い出せないときは、実際に`javac`でコンパイルしてエラーメッセージを読むと、コンパイラが何を要求しているかがそのまま言葉で示されるので理解の助けになる。

### 復習：`throws`で`main`まで回すとどうなるか（`try-catch`との比較）

**何が起きたか**: 「呼び出し側で`throws`を付けて回す」という選択肢を試しに`main`まで実践してみたところ、`try-catch`なしで最後まで例外が伝播し、JVMがプログラムを異常終了させ、スタックトレースを出力して以降の処理（`"ここは実行されるか？"`）が実行されないことを確認した。最初は「`try-catch`の方が良い理由」を「どこでエラーになったか分かるから」と回答したが、実際には`throws`で回したバージョンの方がスタックトレースで発生箇所を詳しく表示しており、この理由づけは誤りだった。

**なぜ**: 「エラーの原因が分かること」と「プログラムが動き続けられること」を区別できていなかった。両方とも発生箇所は分かるが、`try-catch`はその場で処理してプログラムの実行を継続できるのに対し、`main`まで回すと受け止める者がおらずプログラム全体が異常終了する、という違いが本質。

**教訓**: 「Checked例外は`try-catch`で受け止めるべき」という結論だけでなく、「受け止めないとどうなるか」を実際にコード実行結果（正常終了 vs 異常終了）で比較すると、理由づけの誤りに気づきやすい。
