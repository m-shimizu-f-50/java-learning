# 08. コレクション（ArrayList）

## 用語集

- **コレクション**: 複数の要素をまとめて扱うオブジェクト（`List`, `Set`, `Map`など）の総称
- **ジェネリクス**: クラスを使うときに、扱うデータの型を`<>`で指定する仕組み（例: `List<String>`は「Stringだけを扱うList」）
- **オートボクシング**: プリミティブ型（`int`）とラッパークラス（`Integer`）を、Javaが自動的に相互変換してくれる仕組み

## 使い方

### なぜコレクションを使うのか（配列との比較）

配列（`int[]`など）には実務で困る制約がある。

1. **サイズが固定**：要素数を後から増減できない。DBから取得する件数のように「事前に何個あるか分からない」場面で使えない
2. **便利なメソッドがない**：「含まれているか（`contains`）」「削除する（`remove`）」を自前で書く必要がある
3. **用途に応じた種類がない**：配列は「順序ありで重複OK」しか表現できない。「重複を許さない（`Set`）」「キーと値のペア（`Map`）」も実務では頻繁に必要

`List`はこれらを解決してくれる。実務では「要素数が実行中に増減する（可変長）ならコレクションを使う」が基本判断基準（`TaskRepository.findAll()`が`List<Task>`を返すのはこのため。DBの件数は実行するまで分からない）。

### 3種類の使い分け

| 種類 | 特徴 | 使う場面の例 |
|---|---|---|
| `List`（`ArrayList`） | 順序あり、重複OK、インデックスでアクセス | タスク一覧、注文履歴など「並んだ複数のもの」 |
| `Set`（`HashSet`） | 重複NG、順序は保証されない | [28. Objectクラス](28-object-class.md)で確認済み。「同じものを二重に持たせたくない」場面 |
| `Map`（`HashMap`） | キーと値のペア | 「IDから対応するデータを高速に取り出したい」場面（例：`userId → User`）。実務での使用頻度は`List`の次に高い |

実務での使用頻度は`List` > `Map` > `Set`の順になることが多い。

### 宣言と基本操作

```java
import java.util.ArrayList;
import java.util.List;

List<String> names = new ArrayList<>();
names.add("Alice");
names.get(0);
names.remove(0);   // インデックス指定で削除
names.size();
names.contains("Bob");
```

配列は固定長だが、`ArrayList`は要素数を自由に増減できる。JSの`push`/`splice`に近い感覚。

### ジェネリクスとインターフェース

`List<String> names = new ArrayList<>();` の左辺は`List`（インターフェース）、右辺は`ArrayList`（実装クラス）。「`List`という契約を満たすものとして`ArrayList`を使う」というインターフェースの実践例。

プリミティブ型は直接入れられない（`List<int>`はエラー）。`List<Integer>`のようにラッパークラスを使う（オートボクシングで自動変換される）。

### remove()の2つのオーバーロード

```java
fruits.remove("バナナ"); // 値で削除（remove(Object)）
numbers.remove(2);       // インデックスで削除（remove(int)）— 値の2ではなく3番目の要素が消える
numbers.remove(Integer.valueOf(2)); // 値の2を削除したい場合はこう書く
```

`List<Integer>`に対して`remove()`を使うと、`int`（インデックス）と`Integer`（値）の両方に解釈できるため、コンパイラは常に`remove(int)`（インデックス扱い）を優先する。「値を消したいのにインデックスとして解釈されてしまう」事故が起きやすい。

**実例**: 商品ID`101, 202, 303`の`List<Integer>`から「商品ID 202を削除したい」つもりで`cart.remove(202)`と書くと、`202`番目の要素（存在しない）とみなされ`IndexOutOfBoundsException`になる。リストがもっと大きく偶然202番目が存在する場合は、エラーにもならず全く別の要素が静かに削除されてしまう、より危険なケースになる。

**注意**: 「`Integer.valueOf(...)`を使えば安全」ではない。本当にインデックス指定したい場面で`Integer.valueOf(...)`を使うと、今度は逆に「値のつもりが実は正しいインデックス指定だった」という逆方向の事故になる。**安全な方を選ぶのではなく、自分の意図（インデックスか値か）と一致する書き方を選ぶ**のが正しい判断基準。

### 拡張for文の中でremoveすると何が起きるか（ConcurrentModificationException）

```java
List<String> fruits = new ArrayList<>();
fruits.add("りんご");
fruits.add("バナナ");
fruits.add("ぶどう");

for (String fruit : fruits) {
    if (fruit.equals("りんご")) {
        fruits.remove(fruit); // 危険：実行時にConcurrentModificationExceptionの可能性
    }
}
```

拡張for文は裏側で`Iterator`（イテレータ、リストを1つずつ取り出していく専用の道具）を自動的に使っている。イテレータは「リストが何回変更されたか（`modCount`）」を最初に記憶しておき、次の要素を取り出す（`next()`）たびにその回数が変わっていないかチェックする。`fruits.remove(fruit)`のように**イテレータを介さずに直接**リストを変更すると、`modCount`がこっそり増え、次に`next()`が呼ばれたタイミングで初めて食い違いに気づき、`ConcurrentModificationException`を投げる。

**罠**: このエラーは**削除する要素によって発生したりしなかったりする**。削除した要素が最後から2番目で、ループがちょうどそこで終わる場合、次の`next()`が二度と呼ばれず、食い違いに気づく機会が来ないためエラーが出ない。「エラーが出なかったから安全」ではなく、**データの中身次第でエラーになる不安定なコード**であることに注意する。

### 安全に削除する方法：removeIfとIterator.remove()

```java
// 方法1: removeIf（ラムダ式で条件を渡す、簡潔で推奨）
fruits.removeIf(fruit -> fruit.equals("りんご"));

// 方法2: Iteratorを直接使う（removeIfの内部で行われていることと同じ）
Iterator<String> it = fruits.iterator();
while (it.hasNext()) {
    String fruit = it.next();
    if (fruit.equals("りんご")) {
        it.remove(); // イテレータ自身のremove()なので、modCountの整合性が保たれる
    }
}
```

`removeIf`は「条件に一致する要素をまとめて安全に削除する」専用メソッドで、内部では`Iterator.remove()`と同じ仕組みが使われている。ループの外で1回呼ぶだけでよく、拡張for文と組み合わせて使うものではない。**単発で1つの要素を消したいだけ（ループの外）なら`list.remove(...)`で問題ない**。危険なのは「ループしながら`list.remove(...)`を直接呼ぶ」場合だけ。

## 覚えておくべきルール・規約

- 配列は固定長、`ArrayList`は可変長
- `List<T>`はインターフェース、`ArrayList<T>`は実装クラス。宣言は`List<T> x = new ArrayList<>();`とするのが一般的
- プリミティブ型はリストに直接入れられない（ラッパークラスを使う）
- `List<Integer>`への`remove()`はインデックスと値の解釈が衝突する。値を消したい場合は`Integer.valueOf(...)`で明示する
- 拡張for文の中で直接`list.remove(...)`するとエラーが起きたり起きなかったりする不安定なコードになる（`ConcurrentModificationException`）。ループしながら削除したい場合は`removeIf`または`Iterator.remove()`を使う

## 演習

`08-collections/Main.java`にて以下を実装。

1. `List<String> fruits`に3要素を`add`
2. 拡張for文で全要素出力
3. `remove`で1要素削除し、`size()`を出力
4. `List<Integer> numbers`に1〜5を`add`し、合計値を計算

## つまずきの分析

今回は目立ったつまずきなし。`remove(Object)`と`remove(int)`のオーバーロードの違いを解説し、`List<Integer>`特有の罠（値のつもりがインデックス扱いされる）を確認した。

演習コードは `08-collections/Main.java`。コンパイル・実行して動作確認済み（りんご/バナナ/ぶどう出力、削除後size=2、合計15）。

### 復習（`review/24-collections`）：コメントと実際の動作の不一致

**何が起きたか**: `List<Integer> productIds`（`101, 202, 303, 404`）に対して`productIds.remove(2);`と書き、コメントに`// 202を削除`と記載した。実際に実行すると`303`が削除され（`202`は残る）、コメントと実際の動作が一致していなかった。

**なぜ**: `remove(2)`の`2`を「値の202」のつもりで書いてしまい、インデックス（0番目から数えて2番目）として解釈されることを踏まえていなかった。まさにこのトピックで学んだ罠そのものを、演習コード自身で再現してしまった。

**教訓**: `remove(int)`を書くときは、コメントで「消したい値」ではなく「何番目を消しているか」を書く（またはコメント自体を実行結果で検算する）と、この種の不一致に気づきやすい。

### 復習（`review/32-collections`）：removeIfの引数を誤解

**何が起きたか**: `tasks.removeIf(task)`のように、条件式（ラムダ式）ではなく1つの値（`String`の`task`）をそのまま渡してしまい、「`String`を`Predicate<? super String>`に変換できません」というコンパイルエラーになった。

**なぜ**: `removeIf`が「1つの要素を受け取ってtrue/falseを判定する関数」を引数に取ることを踏まえず、for文の中の変数`task`をそのまま渡せば「これを削除して」という意味になると考えてしまった。

**教訓**: `removeIf`は`list.remove(値)`とは引数の種類が全く違う（値ではなく判定ロジック）。`task -> task.equals("resume")`のようなラムダ式で「何を条件に削除するか」を渡す。また`removeIf`はリスト全体からまとめて削除するメソッドなので、`for`ループの中で1要素ずつ呼ぶものではなく、ループの外で1回だけ呼ぶ。

演習コードは `review/32-collections/Main.java`。コンパイル・実行して動作確認済み（`[buy milk, call mom]`）。
