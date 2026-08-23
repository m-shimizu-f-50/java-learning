# 12. static

## 用語集

- **static（クラスメンバー）**: インスタンスごとではなく、クラス全体に対して1つだけ存在するフィールド・メソッドを表すキーワード
- **クラス変数**: `static`が付いたフィールド。全インスタンスで共有される値
- **クラスメソッド**: `static`が付いたメソッド。インスタンス化せずに`クラス名.メソッド()`で直接呼べる
- **インスタンスメンバー**: `static`が付いていない、通常のフィールド・メソッド。インスタンスごとに別々の値・呼び出しになる
- **final**: 一度値をセットしたら変更できないことを表すキーワード。`static`とは別の役割（下記「staticとfinalは別の軸」参照）

## 使い方

### staticフィールド

```java
public class Counter {
    static int count = 0; // クラス全体で1つだけ共有される

    Counter() {
        count++; // インスタンスが作られるたびに増える
    }
}
```

```java
new Counter();
new Counter();
new Counter();
System.out.println(Counter.count); // 3
```

`static`フィールドはインスタンスごとではなく、クラス全体で1つだけ共有される。`this.name`のようなインスタンスフィールドとは対照的。

### staticメソッド

```java
static int add(int a, int b) { return a + b; }
```

インスタンスを作らずに`クラス名.メソッド()`で直接呼べる。`public static void main(String[] args)`もその一例。

### static final（クラス定数）

```java
public static final double PI = 3.14159;
```

`static final`は「変更されないクラス共通の定数」を表す、実務で頻出のパターン。

### static初期化ブロック

```java
static {
    // クラスロード時に1度だけ実行される
}
```

### staticとfinalは別の軸

`static`＝「変わらない値をまとめておくもの」だと誤解しやすいが、それは正しくない。`static`は「**誰が持つか**（クラス全体で共有 vs インスタンスごとに個別）」を決め、`final`は「**変更できるかどうか**」を決める、独立した2つの機能。組み合わせは自由。

| | `static`あり | `static`なし |
|---|---|---|
| `final`あり | クラス定数（例: `Ticket.PREFIX`）。全体で共有、かつ変更不可 | インスタンスごとの値だが一度セットしたら変わらない（例: コンストラクタでセットする`final`フィールド） |
| `final`なし | 共有カウンター（例: `Counter.count`）。全体で共有、かつ変更される | 普通のフィールド。インスタンスごとに個別で、変更もされる |

`Counter.count`（`static`のみ、`final`ではない）が`new`のたびに増えていくのがまさにこの「`static`かつ変更される」パターン。

### 「他ファイルから使えるかどうか」とは別の話

`static`は「他のクラス（別ファイル）から使えるかどうか」を決めているのではない。`public`な通常のフィールド・メソッドも、`インスタンス変数.メソッド()`の形で普通に他ファイルから使える。

```java
// 別ファイルからでも、staticでなくても普通に呼べる
Student student = new Student("田中");
student.getName();
```

`static`が決めているのは「**クラス名から直接呼ぶか（`Counter.count`）、インスタンスを経由して呼ぶか（`student.getName()`）**」という**アクセスの経路**。他ファイルから見えるかどうかは`public`/`private`が決める、別の軸。

### 見分け方：インスタンスに紐づくかどうかで判断する

「この値・メソッドは、**“どのインスタンスか”によって結果や意味が変わるか？**」と自問する。

- 変わる（例: `student.getName()`は生徒ごとに違う）→ インスタンスに紐づく（`static`なし）
- 変わらない、そもそも「どのインスタンスか」を問うこと自体がおかしい（例: `Math.max(3, 5)`、`Counter.CATEGORY`）→ インスタンスに紐づかない（`static`）

### 実験：staticを外すとどうなるか

`static`を外すと、同じ「全体で共有するカウンター」は再現できなくなる。

```java
class TicketNoStatic {
    int ticketNumber = 0; // staticを外した場合
    TicketNoStatic() { ticketNumber++; }
}
```

```java
TicketNoStatic t1 = new TicketNoStatic();
TicketNoStatic t2 = new TicketNoStatic();
TicketNoStatic t3 = new TicketNoStatic();
TicketNoStatic t4 = new TicketNoStatic();
// t1.ticketNumber, t2.ticketNumber, t3.ticketNumber, t4.ticketNumber
// → 1, 1, 1, 1（それぞれが自分専用のticketNumberを持ち、他に影響しない）
```

`static`ありなら`4`になるところが、外すと全員`1`のまま。各インスタンスが**別々の**`ticketNumber`を持つため、「全体で1つを共有し続ける」という動作は`static`なしでは実現できない。書き方の好みの問題ではなく、明確に異なる挙動になる。

### 実務でstaticを使う代表的な場面

1. **IDの自動採番**: 会員番号・注文番号・チケット番号など、新規作成のたびに被らない連番を振りたい場面（今回の`Counter`/`Ticket`そのもの。実際のシステムでは`20-spring-boot`の`@GeneratedValue`のようにDB側に任せることが多い）
2. **全体の集計・カウント**: 「これまでに何回リクエストが来たか」のような、個々のインスタンスではなくアプリ全体としての数字
3. **ログ出力用のLogger**: `private static final Logger logger = LoggerFactory.getLogger(...)`のように、インスタンスごとに別々に持つ必要がないものはクラスに1つだけ用意する
4. **全インスタンス共通の設定・キャッシュ**: 一度読み込んだ設定を、全インスタンスで使い回したい場面

## 覚えておくべきルール・規約

- `static`フィールドは全インスタンスで共有される（インスタンスごとに別の値を持つ通常のフィールドとは異なる）
- `static`メンバーは「個々のインスタンスに紐づくか、クラス全体に紐づくか」で使い分ける
- `static`と`final`は別の軸。「共有するか」は`static`、「変更できるか」は`final`が決める。`static`単体は「値が変わらない」ことを意味しない（`Counter.count`のように変化するstaticもある）
- 「他ファイルから使えるか」は`public`/`private`が決める話であり、`static`とは無関係。`static`が決めるのは「クラス名から呼ぶか、インスタンス経由で呼ぶか」というアクセス経路
- `static final`はクラス定数の定義によく使う

## 演習

`12-static/Counter.java`, `Main.java`にて以下を実装。

1. `Counter`クラス: `static int count = 0;`
2. コンストラクタで`count++`（インスタンス生成のたびに増加）
3. `static int getCount()`
4. `static final String CATEGORY = "サンプル";`
5. `Main`で`Counter`のインスタンスを3つ作り、`Counter.getCount()`で`3`を確認、`Counter.CATEGORY`も出力

## つまずきの分析

### インスタンス生成を伴わない実装

**何が起きたか**: 初回、`Counter`のインスタンスを一度も作らず、代わりに手動の`static increment()`メソッドを3回呼んで`count`を`3`にしていた。出力結果自体は同じ`3`だったが、「インスタンス生成のたびに自動で増える」という`static`の核心（全インスタンスで共有される）を確認できていなかった。

**なぜ**: `static`メソッドを直接呼ぶ操作に気を取られ、コンストラクタでの自動カウントという設計意図を見落とした。

**教訓**: 出力結果が仕様と一致していても、実装が学習の狙い（今回は「コンストラクタ経由での共有フィールドの更新」）を満たしているかは別に確認する必要がある。`new Counter()`を3回呼ぶ形に修正し、狙い通りの実装で動作確認済み。

### 分散学習の復習チェックポイントでの誤解（`review/10-static`）

**何が起きたか**: 復習チェックポイント自体（`Ticket`クラス）は正解したが、事後の質疑応答で以下の誤解が連鎖的に判明した。

1. 「`static`＝共通で使う変わらない値をまとめておくもの」という理解（`static`と`final`の役割を混同していた）
2. `Ticket.ticketNumber`（`static`だが`final`ではない）が`new`のたびに変化する事実と、上記1の理解が矛盾することに気づけなかった
3. 「インスタンス」と「クラス（ファイル）」の用語を混同していた（「他のインスタンス(〇〇.java)」という表現）
4. 「他のファイルから使いたいから`static`にする」という理解（`static`ではなく`public`が決める話）

**なぜ**: `static`の「クラス全体で共有される」という性質と、`static final`でよく使う「定数」という用途が結びつきやすく、`static`単体の役割（誰が持つか）と`final`の役割（変更できるか）が1つの概念として混ざってしまっていた。

**教訓**: `static`と`final`は独立した別の軸（上記「staticとfinalは別の軸」参照）。矛盾する具体例（`static`だが変化する`ticketNumber`）を実際に突きつけられると誤解に気づきやすい。また「`static`を外すと実際にどう挙動が変わるか」を`javac`/`java`で実験して確認すると、「書き方の好みではなく明確に異なる挙動である」ことが実感しやすい。

演習コードは `12-static/Counter.java`, `Main.java`。コンパイル・実行して動作確認済み（現在のカウント3、カテゴリーサンプル）。
