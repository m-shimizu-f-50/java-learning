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

## 覚えておくべきルール・規約

- フィールドは基本`private`にし、`public`な`getter`/`setter`で外部に公開する
- `setter`に検証ロジックを入れることで、不正な値の代入を防げる
- JSにはネイティブな`private`/`protected`の概念がない（`#field`はES2022以降の比較的新しい機能）ため、Javaでは最初から明確に区別されている点を意識する

## 演習

`11-encapsulation/Student.java`, `Main.java`にて以下を実装。

1. `Student`クラス: `private String name`, `private int score`
2. コンストラクタで初期化
3. `getName()`, `getScore()`
4. `setScore(int score)`: 0〜100の範囲外は「不正な点数です」と出力して更新しない
5. `Main`で不正な値・正常な値をそれぞれ試し、最終的な`getScore()`で不正値が弾かれていることを確認

## つまずきの分析

- 初回、`Student`クラスの実装（private化・getter・setterのバリデーション）は完璧だったが、`Main`側で不正値セット後の最終`getScore()`出力が漏れており、「本当に弾かれているか」を出力から確認できない状態だった → 追加して解決
- **教訓**: バリデーションのロジック自体が正しくても、「意図通り弾かれたことを示す出力」がなければ動作確認として不十分。境界ケース・異常系のテストは「弾かれた後の状態」まで確認する

演習コードは `11-encapsulation/Student.java`, `Main.java`。コンパイル・実行して動作確認済み（更新されたスコア90、不正な点数です、最終スコア90）。
