# 18. String/StringBuilderの詳細

## 使い方

### Stringは不変（イミュータブル）

```java
String s = "Hello";
s.concat(" World"); // 戻り値を使っていない
System.out.println(s); // "Hello"のまま

String s2 = s.concat(" World");
System.out.println(s2); // "Hello World"
```

`String`は一度作られると中身を変更できない。変更したように見える操作は、実は新しいオブジェクトを作って返しているだけ。再代入して使う必要がある。

### 文字列プール（String Pool）

```java
String a = "test";
String b = "test";
System.out.println(a == b); // true（同じプール上のオブジェクトを指す）

String c = new String("test");
System.out.println(a == c); // false（newで明示的に作ると別オブジェクト）
System.out.println(a.equals(c)); // true（内容は同じ）
```

リテラル（`""`）で書いた文字列は文字列プールで使い回される。`new String(...)`は明示的に別オブジェクトを作る。**文字列の比較には`==`ではなく`.equals()`を使う**（`==`は参照の比較）。

### StringBuilder（可変な文字列）

```java
StringBuilder sb = new StringBuilder();
sb.append("Hello").append(" ").append("World"); // メソッドチェーン可能
sb.insert(5, ",");
sb.reverse();
sb.deleteCharAt(0);
```

`append`は自分自身を書き換えて自分自身を返すため、メソッドチェーンで書ける。

## 覚えておくべきルール・規約

- 文字列の比較は`==`ではなく`.equals()`を使う（`==`は参照比較、プールのおかげでたまたま一致することがあるだけで信頼できない）
- ループ内で文字列を繰り返し連結する場合は`String`の`+`ではなく`StringBuilder`を使う（`+`は毎回新しいオブジェクトを作り非効率）
- 単発の連結（数回程度）なら`String`の`+`で十分

## 演習

`18-string-builder/Main.java`にて以下を実装。

1. `String a = "test"`, `b = "test"`, `c = new String("test")`で`==`/`.equals()`の挙動を確認
2. `StringBuilder`で1〜5を連結した文字列を作成
3. `reverse()`で逆順にする

## つまずきの分析

今回は目立ったつまずきなし。文字列プールの挙動（`==`と`.equals()`の違い）、`StringBuilder`の`append`/`reverse()`ともに正しく理解できていた。

演習コードは `18-string-builder/Main.java`。コンパイル・実行して動作確認済み（true, false, true, 12345, 54321）。
