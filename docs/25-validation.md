# 25. バリデーション（`@Valid`）

## 用語集

- **Bean Validation**: フィールドにアノテーションを付けるだけで値のチェックルールを宣言できる、Javaの標準規格
- **`@NotBlank`**: 値が`null`・空文字・空白のみでないことを検証するアノテーション
- **`@Size`**: 文字列の長さ（や配列・コレクションの要素数）の範囲を検証するアノテーション
- **`@Valid`**: DTOに付与されたBean Validationのアノテーションを、Controllerに届く前に実際にチェックさせる指示
- **400 Bad Request**: リクエストは正しいURLに届いているが、送られてきたデータの中身が不正な場合に返すステータスコード（`404 Not Found`＝リソースが存在しない、とは意味が異なる）

## 使い方

### DTOにルールを宣言する

```java
public class TaskRequestDto {
    @NotBlank(message = "titleは必須です")
    @Size(max = 100, message = "titleは100文字以内で入力してください")
    private String title;

    public String getTitle() { return title; }
}
```

`11-encapsulation`で学んだ「setterにif文でチェックを書く」のと目的は同じだが、Bean Validationはif文を書かずアノテーションで宣言できる。

### `@Valid`で発動させる

```java
@PostMapping("/tasks")
public TaskResponseDto createTask(@Valid @RequestBody TaskRequestDto request) {
    // ここに到達した時点でtitleは必ず「空でない、100文字以内」
    ...
}
```

`@Valid`を付けると、SpringはControllerのメソッドが実行される**前**にDTOの中身をチェックする。違反していればメソッドは実行されず、自動的に`400 Bad Request`が返る。

`23-security`の認証認可が「Controllerに入る前に、誰か・権限があるかを自動チェックする」構造だったのに対し、バリデーションは「Controllerに入る前に、**送られてきたデータの中身が正しいか**を自動チェックする」という違いがある。

## 覚えておくべきルール・規約

- バリデーションは**リクエスト用DTO**（クライアントから送られてくるデータ）に付ける。レスポンス用DTO（自分たちで作って返すデータ）に付けても意味がない
- `@Valid`は検証したいDTOパラメータ（`@RequestBody`）に付ける。`@PathVariable`のようなプリミティブ型に付けても何も検証されない
- バリデーションエラーは`400 Bad Request`。「リソースが存在しない」ことを表す`404 Not Found`とは意味が異なるので混同しない

## 演習

`20-spring-boot`に以下を実装。

1. `pom.xml`に`spring-boot-starter-validation`を追加
2. `TaskRequestDto.java`の`title`に`@NotBlank`と`@Size(max = 100)`を追加
3. `TaskController.java`の`createTask`・`updateTask`の`@RequestBody TaskRequestDto request`に`@Valid`を追加
4. 空の`title`で`POST`/`PUT`すると`400`、正常な`title`なら成功することを`curl`で確認

## つまずきの分析

### バリデーションを付ける場所の誤り

**何が起きたか**: `@NotBlank`/`@Size`を、検証すべき`TaskRequestDto`ではなく`TaskResponseDto`（レスポンス用）に付けてしまい、`Size`のimportも漏れてコンパイルエラーになった。

**なぜ**: バリデーションが「データの中身をチェックする」ものだという理解はあったが、「クライアントから**送られてくる**データ（リクエスト）」と「自分たちが**返す**データ（レスポンス）」のどちらをチェックすべきかの区別がついていなかった。

**教訓**: バリデーションは「信頼できない外部からの入力」を検証するためのもの。自分たちのサーバーが生成して返すレスポンスは検証対象にならない。DTOを見たら「これはリクエスト用かレスポンス用か」を先に確認する。

### `@Valid`の付け場所の誤り（複数回の修正を要した）

**何が起きたか**: `updateTask`メソッドで`@Valid`を`@PathVariable int id`に付けてしまい、`@RequestBody TaskRequestDto request`には付いていなかった。指摘後も複数回同じ箇所が修正されないままレビューに提出される場面があった。

**なぜ**: `@Valid`と`@RequestBody`が2つとも同じ引数の前に並ぶ書き方に慣れておらず、`createTask`で先に書いた`@Valid @RequestBody`という並びを、機械的に別の引数（`id`側）に当てはめてしまっていた可能性がある。

**教訓**: `@Valid`は「検証したいオブジェクトの直前」に置く。`int`のようなプリミティブ型にはBean Validationの制約を直接書けないため、`@Valid`を付ける意味がない。修正が反映されない場合は、保存されているか・正しいファイルを編集しているかを都度`Read`で確認する。

演習コードは `20-spring-boot/src/main/java/com/example/demo/TaskRequestDto.java`, `TaskController.java`。`mvn spring-boot:run`で起動し、空`title`での`POST`/`PUT`が`400`、正常な`title`なら成功することを`curl`で確認済み。
