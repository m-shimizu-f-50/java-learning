# 19. java.time API

## 使い方

### 主なクラス

| クラス | 表すもの |
|---|---|
| `LocalDate` | 日付のみ |
| `LocalTime` | 時刻のみ |
| `LocalDateTime` | 日付＋時刻 |
| `Period` | 日付同士の期間（年・月・日） |
| `Duration` | 時刻同士の期間（時・分・秒） |

### LocalDateの基本

```java
LocalDate today = LocalDate.now();
LocalDate date = LocalDate.of(2026, 8, 10);

date.getYear();
date.getMonth();
date.getDayOfWeek();
```

### イミュータブル（不変）

```java
LocalDate date = LocalDate.of(2026, 8, 10);
date.plusDays(5); // 戻り値を使っていない → dateは変わらない
LocalDate newDate = date.plusDays(5); // 新しいオブジェクトを受け取る必要がある
```

`String`と同じく、変更操作は新しいオブジェクトを返すだけで元のオブジェクトは変わらない。

### 比較

```java
d1.isBefore(d2);
d1.isAfter(d2);
d1.isEqual(d2);
```

### Period（期間の計算）

```java
Period period = Period.between(start, end);
period.getYears() + "年" + period.getMonths() + "ヶ月" + period.getDays() + "日";
```

## 覚えておくべきルール・規約

- `java.time`のクラスは基本的にイミュータブル。`plusXxx`/`minusXxx`は新しいオブジェクトを返すだけで元は変わらない（戻り値を使わないと何も起きない）
- 日付の比較は`isBefore`/`isAfter`/`isEqual`を使う
- 期間（年月日単位の差）は`Period.between()`で計算する

## 演習

`19-datetime/Main.java`にて以下を実装。

1. `LocalDate.of(...)`で入社日を作成
2. `LocalDate.now()`で今日の日付を取得
3. `plusYears(1)`で1年後の日付を計算（元の変数が変わらないことも確認）
4. `Period.between()`で入社日から今日までの期間を「◯年◯ヶ月◯日」の形式で出力
5. `isBefore()`で入社日が今日より前かを判定

## つまずきの分析

今回は目立ったつまずきなし。`plusYears(1)`の結果を別変数で受け取り、元の`hireDate`が変わらないことを確認できており、不変性の理解ができていた。

演習コードは `19-datetime/Main.java`。コンパイル・実行して動作確認済み。

---

これでJava Silver試験対策の補完項目（アクセス修飾子・static・抽象クラス・キャスト/instanceof・例外処理・可変長引数・配列ユーティリティ・String/StringBuilder・java.time）が全て完了。
