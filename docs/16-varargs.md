# 16. 可変長引数（varargs）

## 用語集

- **可変長引数（varargs）**: 呼び出し時に個数を指定せず、複数の値を渡せる引数（`型... 変数名`）。メソッド内では配列として扱われる

## 使い方

### 基本構文

```java
static int sum(int... numbers) { // int[]の代わりに int... と書く
    int total = 0;
    for (int n : numbers) {
        total += n;
    }
    return total;
}
```

```java
sum();              // 0個 → 0
sum(1, 2, 3);        // 3個 → 6
```

メソッド内では`numbers`は実質`int[]`として扱われる。JSのレストパラメータ（`function sum(...numbers)`）に近い発想。

### 配列を直接渡すこともできる

```java
int[] arr = {1, 2, 3};
sum(arr);
```

### 固定引数との組み合わせ

```java
void printScores(String name, int... scores) { ... }
```

可変長引数は引数リストの**最後に1つだけ**書ける。

## 覚えておくべきルール・規約

- `型... 変数名`で宣言し、メソッド内では配列として扱われる
- 可変長引数は引数リストの最後に1つだけ許される
- 固定引数と可変長引数を組み合わせられる（固定引数が先、可変長引数が最後）
- `sum(int a, int b)`と`sum(int... numbers)`が両方あれば、より具体的にマッチする固定引数版が優先される

## 演習

`16-varargs/Main.java`にて以下を実装。

1. `average(double... numbers)`: 平均を返す（0個の場合は0）
2. `printAll(String label, String... items)`: labelと各itemを出力
3. `average(10, 20, 30)`と`average()`を呼び出し
4. `printAll("好きな果物", "りんご", "ばなな", "ぶどう")`を呼び出し

## つまずきの分析

今回は目立ったつまずきなし。`average()`（引数0個）で戻り値の`double`型への自動変換（`0` → `0.0`）も正しく理解できていた。

演習コードは `16-varargs/Main.java`。コンパイル・実行して動作確認済み（20.0、0.0、好きな果物の一覧）。
