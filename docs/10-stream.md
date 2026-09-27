# 10. Stream API

## 用語集

- **Stream**: コレクションに対してフィルタ・変換・集計を連鎖的に行うためのオブジェクト。元のコレクションは変更しない
- **中間操作**: `filter`や`map`など、Streamを別のStreamに変換する操作。複数つなげられる
- **終端操作**: `collect`や`sum`など、Streamの処理を締めくくり最終結果を取り出す操作。1回しか呼べない

## いつ使うか

配列に限らず、**リストなどのコレクションに対して複数の要素をまとめて処理する場面**で使う。典型的には以下のようなケース。

1. **条件で絞り込みたい（フィルタリング）**: 例）「アクティブなユーザーだけ取り出す」
2. **各要素を別の形に変換したい（マッピング）**: 例）「`User`のリストから名前だけのリストを作る」
3. **集計したい**: 例）「注文リストの合計金額を計算する」「件数を数える」「最大値を求める」
4. **上記を連続したパイプラインとして書きたい**: 例）「アクティブなユーザーだけ抽出して、名前のリストにする」を1行の連鎖で書ける

### for文で書いた場合との比較

同じ処理（15より大きい数を2倍にする）を比べると、Streamの狙いが分かりやすい。

```java
// for文の場合: 一時変数(result)を用意して手動でループ・追加する
List<Integer> result = new ArrayList<>();
for (int n : numbers) {
    if (n > 15) {
        result.add(n * 2);
    }
}

// Streamの場合: 「何をしたいか」を宣言的に1行で書ける
List<Integer> result = numbers.stream()
    .filter(n -> n > 15)
    .map(n -> n * 2)
    .collect(Collectors.toList());
```

for文は「どう繰り返すか（一時変数を用意して、条件分岐して、追加する）」という**手順**を書く。Streamは「フィルタする→変換する→集める」という**やりたいこと**を宣言的に書く。処理が2〜3段階連なるほどStreamの読みやすさが際立つ。

### 逆にStreamを無理に使わなくていい場面

- ただ1回ループして`println`するだけなど、変換・集計を伴わない単純な繰り返し（普通の`for`文で十分）
- ループの途中で`break`したい、インデックスそのものが必要、など手続き的な制御が必要な場合

## 使い方

### 基本の流れ

```java
List<Integer> result = numbers.stream()      // ①ストリーム生成
    .filter(n -> n % 2 == 0)                 // ②中間操作: 条件で絞る
    .map(n -> n * n)                         // ②中間操作: 変換する
    .collect(Collectors.toList());           // ③終端操作: 結果を集める
```

Streamは「生成 → 中間操作（複数つなげられる） → 終端操作」の3段階。`filter`/`map`にはラムダ式をそのまま渡せる。

### JSとの対応

| Java Stream | JS配列メソッド |
|---|---|
| `.filter(predicate)` | `.filter(fn)` |
| `.map(fn)` | `.map(fn)` |
| `.collect(Collectors.toList())` | （そのまま配列） |
| `.mapToInt(fn).sum()` | `.reduce((a,b)=>a+b, 0)` |
| `.count()` | `.length` |

### 元のコレクションは変更されない

Streamの操作は元のリストを変更せず、新しい結果を作る（JSの`map`/`filter`が新配列を返すのと同じ）。

### Streamは使い捨て：一度終端操作を呼んだら再利用できない

```java
Stream<Integer> stream = numbers.stream().filter(n -> n > 10);
long count = stream.count();          // 終端操作①
long sum = stream.mapToInt(n -> n).sum(); // 終端操作②：同じstream変数を再利用
// IllegalStateException: stream has already been operated upon or closed
```

`List`は「値を保存しておく箱」なので何度でも中身を見られるが、`Stream`は名前の通り「流れ」——ベルトコンベアのように要素が流れながら加工され、終端操作で流れきって処理が完了する**使い捨てのパイプライン**。一度終端操作（`count()`, `collect()`など）を呼ぶと、コンベアの上には何も残っていない。もう一度使いたい場合は`numbers.stream()`から**新しく作り直す**必要がある。

### 終端操作の戻り値の型に注意する

終端操作は「Streamの流れを終わらせて、最終結果を返す」操作。戻ってくるのは`Stream`ではなく、それぞれの操作に応じた具体的な型になる。

```java
List<Double> result = salaries.stream()...collect(Collectors.toList()); // collect()はList
long count = salaries.stream()...count();                               // count()はlong
```

変数の型を`Stream`のままにしてしまうと型不一致でコンパイルエラーになる。`collect`なら`List`（や`Set`）、`count`なら`long`など、それぞれの終端操作が何を返すかに合わせて変数の型を宣言する。

### mapの中での型変換ルール（04-methods、08-collectionsとの接続）

```java
List<Integer> salaries = ...;
salaries.stream()
    .map(n -> n * 1.1)   // nはInteger、1.1はdouble → 04-methodsのwidening規則でdoubleになる
    .collect(Collectors.toList()); // → List<double>ではなくList<Double>（08-collectionsの規則）
```

`map`の中の計算結果の型がそのままStreamの要素の型になる。`int * double`は`04-methods`のオーバーロード解決と同じ理屈で`double`に自動変換される。その結果を`collect`する変数の型も、コレクションはプリミティブ型を直接扱えない（`08-collections`）という理由から`List<Double>`（ラッパークラス）にする必要がある。

金額計算で誤差を避けたい場合は、`map`の中で`BigDecimal`を使うこともできる。

```java
.map(n -> new BigDecimal(n).multiply(new BigDecimal("1.1")))
```

## 覚えておくべきルール・規約

- Streamの中間操作（`filter`/`map`など）は元のコレクションを変更しない
- 「以上」は`>=`、「より大きい」は`>`。境界値がある条件では取り違えに注意する
- `mapToInt`は`Stream<Integer>`を`IntStream`に変換し、`.sum()`などの数値集計メソッドが使えるようになる
- Streamは使い捨て。一度終端操作を呼ぶと再利用できず、`IllegalStateException`になる。再度使いたい場合は`.stream()`から作り直す
- 終端操作の戻り値は`Stream`ではなく、操作に応じた具体的な型（`collect`なら`List`、`count`なら`long`など）。変数の型もそれに合わせる

## 演習

`10-stream/Main.java`にて以下を実装。

1. 数値リストから15より大きい値をフィルタし、2倍にしたリストを作成
2. 文字列リストから5文字以上の単語を抽出
3. 文字列リストの合計文字数を`mapToInt`と`.sum()`で計算

## つまずきの分析

### `>=`と`>`の取り違え（off-by-one）

**何が起きたか**: 「5文字以上」の条件を`word.length() > 5`と書いてしまい、ちょうど5文字の単語（`apple`, `grape`）が結果から漏れていた（`banana`のみが該当）。

**なぜ**: 「以上」と「より大きい」の境界値の扱いを混同した。

**教訓**: 「以上/以下」は`>=`/`<=`、「より大きい/小さい」は`>`/`<`。境界値ちょうどのテストデータ（今回で言う5文字の単語）があるケースでは、結果を見て境界がずれていないか必ず確認する。

**修正後の結果**: `[apple, banana, grape]`で正しく5文字以上の単語がすべて抽出された。

演習コードは `10-stream/Main.java`。コンパイル・実行して動作確認済み（結果[44, 60]、長い単語[apple, banana, grape]、合計文字数23）。

### 復習（`review/34-stream`）：終端操作の戻り値をStream型で受けてしまう

**何が起きたか**: `collect(Collectors.toList())`と`count()`（どちらも終端操作）の結果を受け取る変数を、両方とも`Stream`型で宣言してしまい、型不一致でコンパイルエラーになった。修正後も、`map(n -> n * 1.1)`の結果を`List<double>`（プリミティブ型のまま）で受けようとして再度エラーになった。

**なぜ**: 「終端操作は最終結果を返す」ことは理解していたが、「その最終結果の具体的な型」（`collect`なら`List`、`count`なら`long`）まで意識できていなかった。また、`int * double`が`double`になる（`04-methods`）ことと、コレクションはプリミティブ型を直接扱えない（`08-collections`）という2つの既習ルールを、Streamの`map`の文脈で組み合わせて適用することができていなかった。

**教訓**: Streamの型は「入り口（`stream()`で何のStreamか）」「中間操作でどう変わるか（`map`の計算結果の型）」「終端操作で何が返るか」の3点を順に追うと、変数の型が自然に決まる。既に学んだ型変換ルールは、Streamの中でもそのまま適用される。

演習コードは `review/34-stream/Main.java`。コンパイル・実行して動作確認済み（`[352000.0, 451000.0]`（`BigDecimal`使用で誤差なし）、件数2）。
