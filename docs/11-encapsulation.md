# 11. アクセス修飾子・カプセル化

## 用語集

- **カプセル化**: フィールドを外部から直接触れないようにし、決まった手段（getter/setter）経由でのみ操作させる設計
- **アクセス修飾子**: クラス・フィールド・メソッドへのアクセス範囲を制限するキーワード（`private`, `protected`, `public`など）
- **getter/setter**: `private`なフィールドを外部から読み書きするために公開するメソッド。`get〇〇()`で取得、`set〇〇(値)`で設定するのが慣習

## 使い方

### アクセス修飾子の種類

| 修飾子 | アクセス可能な範囲 |
|---|---|
| `private` | 同じクラス内のみ |
| （なし、package-private） | 同じパッケージ内のみ |
| `protected` | 同じパッケージ ＋ 継承したサブクラス |
| `public` | どこからでも |

### カプセル化の実践

```java
public class Student {
    private String name;
    private int score;

    public Student(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public int getScore() { return score; }

    public void setScore(int score) {
        if (score < 0 || score > 100) {
            System.out.println("不正な点数です");
        } else {
            this.score = score;
        }
    }
}
```

フィールドを`private`にし、外部からは`getter`/`setter`経由でのみアクセスさせる。`setter`にバリデーションを入れることで「不正な値を代入させない」というルールを強制できる。

### setterは必ず用意しなければいけないものではない

```java
public class BankAccount {
    private int balance;

    public void withdraw(int amount) {
        if (amount > balance) {
            System.out.println("残高不足です");
        } else {
            balance -= amount;
        }
    }
    // setBalance(int balance) は用意しない
}
```

このクラスには`setBalance(int balance)`のような無制限setterがなく、残高を変更する手段は`withdraw()`だけ。もし`setBalance`を公開してしまうと、外部から`account.setBalance(-500)`のように直接書き換えられ、`withdraw()`が持つ「残高不足なら弾く」というルールを完全にすり抜けられてしまう。

**カプセル化の目的は「private化してgetter/setterを付けること」自体ではなく、「オブジェクトが常に正しい状態（不変条件）を保てるように、外部からの変更経路をコントロールすること」**。フィールドに対して意味を持つ操作（`withdraw()`/`deposit()`など、業務的な名前のメソッド）だけを公開する方が、何でも書き換えられる無制限setterより安全な場合が多い。getter/setterは「デフォルトの選択肢」であり「必ず付けるべきもの」ではない。

### getterがコレクション（参照型）を返すときの罠：防御的コピー

```java
public class Wallet {
    private List<String> transactions = new ArrayList<>();

    public void addTransaction(String t) { transactions.add(t); }
    public List<String> getTransactions() { return transactions; } // 実体をそのまま返している
}
```

```java
List<String> externalList = wallet.getTransactions();
externalList.add("勝手に追加された取引"); // addTransaction()を一切通していない
// wallet内部のtransactionsにも反映されてしまう！
```

`List`（[03. 配列](03-arrays.md)の配列と同じ）は参照型。`return transactions;`は中身をコピーして返しているのではなく、**実体への参照そのもの**を渡している。`private`は「フィールド変数名への直接アクセス」を防ぐだけで、一度getter経由でその実体を渡してしまうと、受け取った側はその実体を自由に書き換えられる。`addTransaction()`のような正規の経路を完全にすり抜けられてしまう。

**対策：防御的コピー（defensive copy）**

```java
public List<String> getTransactions() {
    return new ArrayList<>(transactions); // コピーを返す
}
```

`new ArrayList<>(transactions)`（[03. 配列](03-arrays.md)の`Arrays.copyOf`と同じ考え方）で、フィールドとは別の実体を返す。外部でいくら書き換えても、内部のフィールドには一切影響しなくなる。**フィールドが`List`/`Set`/`Map`のような参照型の場合、getterはそのまま返さず防御的コピーを返す**、というのが安全な実装。

### 参照型フィールドの初期値はnull

```java
public class ShoppingList {
    private List<String> items; // 初期化していない → 初期値はnull

    public void addItem(String i) {
        items.add(i); // NullPointerException: itemsがnullのまま
    }
}
```

`int`フィールドの初期値が`0`（[05. クラス](05-class.md)）なのと同様に、`List`のような参照型フィールドは、何も代入しなければ初期値は**`null`**になる。フィールド宣言時にその場で初期化する（`private List<String> items = new ArrayList<>();`）か、コンストラクタで初期化する必要がある。

## 覚えておくべきルール・規約

- フィールドは基本`private`にし、`public`な`getter`/`setter`で外部に公開する
- `setter`に検証ロジックを入れることで、不正な値の代入を防げる
- setterは必須ではない。無制限に値を書き換えられるsetterより、`withdraw()`のような意味のある操作だけを公開する方が不変条件を守りやすい
- フィールドが`List`/`Set`/`Map`のような参照型の場合、getterはフィールドをそのまま返さず、防御的コピー（`new ArrayList<>(フィールド)`）を返す。そのまま返すと、外部で取得したものを書き換えられて内部状態が漏れてしまう
- 参照型フィールドは、初期化しないと初期値が`null`になる。宣言時に`= new ArrayList<>()`のように初期化するか、コンストラクタで初期化する
- JSにはネイティブな`private`/`protected`の概念がない（`#field`はES2022以降の比較的新しい機能）ため、Javaでは最初から明確に区別されている点を意識する

## 演習

`exercises/11-encapsulation/Student.java`, `Main.java`にて以下を実装。

1. `Student`クラス: `private String name`, `private int score`
2. コンストラクタで初期化
3. `getName()`, `getScore()`
4. `setScore(int score)`: 0〜100の範囲外は「不正な点数です」と出力して更新しない
5. `Main`で不正な値・正常な値をそれぞれ試し、最終的な`getScore()`で不正値が弾かれていることを確認

## つまずきの分析

- 初回、`Student`クラスの実装（private化・getter・setterのバリデーション）は完璧だったが、`Main`側で不正値セット後の最終`getScore()`出力が漏れており、「本当に弾かれているか」を出力から確認できない状態だった → 追加して解決
- **教訓**: バリデーションのロジック自体が正しくても、「意図通り弾かれたことを示す出力」がなければ動作確認として不十分。境界ケース・異常系のテストは「弾かれた後の状態」まで確認する

演習コードは `exercises/11-encapsulation/Student.java`, `Main.java`。コンパイル・実行して動作確認済み（更新されたスコア90、不正な点数です、最終スコア90）。

### 復習（`review/35-encapsulation`）：フィールド未初期化と型の取り違え

**何が起きたか**: `private List<String> items;`を初期化せず（`= new ArrayList<>()`が無い）、`addItem()`で`NullPointerException`が発生した。修正後、`getItems()`（戻り値`List<String>`）の結果を`ShoppingList`型の変数で受けようとして型不一致エラーが発生し、その修正過程でも`List<String> shoppingList2 = List<String>;`のように型と値を混同した誤った構文を書いてしまう場面があった。

**なぜ**: 1つ目は、`05-class`で学んだ「参照型フィールドの初期値はnull」というルールを、実装時に見落としていた。2つ目は、メソッドが実際に何を返すか（`ShoppingList`ではなく`List<String>`）を確認せず、変数の型をコピー元のまま流用してしまった。

**教訓**: フィールドは宣言するだけでなく初期化されているか確認する（特に参照型は`null`になりやすい）。変数の型を書くときは「そのメソッドが実際に何を返すか」をコンパイラのエラーメッセージ（`不適合な型: List<String>を〇〇に変換できません`）から逆算すると、正しい型に気づきやすい。

演習コードは `review/35-encapsulation/ShoppingList.java`, `Main.java`。コンパイル・実行して動作確認済み（元のショップリスト：[追加ショップ]、外部から追加したショップリスト：[追加ショップ, 追加ショップ2]）。
