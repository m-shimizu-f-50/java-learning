# 20. Spring Boot基礎（REST API / CRUD）

## 用語集

- **REST API**: HTTPメソッド（GET/POST/PUT/DELETE）を使って、リソースに対する操作を表現するAPI設計の考え方
- **CRUD**: Create（作成）・Read（読み取り）・Update（更新）・Delete（削除）の頭文字。データ操作の基本4パターン
- **JSON（JavaScript Object Notation）**: サーバーとクライアントがデータをやり取りするための軽量なテキスト形式。`{"key": "value"}`のような見た目
- **エンドポイント**: APIがリクエストを受け付ける、URLとHTTPメソッドの組み合わせ（例: `GET /tasks`）
- **@PathVariable**: URLパスの一部（`/tasks/{id}`の`{id}`部分）を変数として受け取るアノテーション
- **@RequestParam**: URLのクエリパラメータ（`?name=太郎`の部分）を受け取るアノテーション
- **@RequestBody**: リクエストボディ全体を、指定した型のオブジェクトに変換して受け取るアノテーション

## 使い方

REST APIのCRUDとHTTPメソッドの対応。

| CRUD操作 | HTTPメソッド | 今回のエンドポイント |
|---|---|---|
| Create（作成） | `POST` | `POST /tasks` |
| Read（読み取り） | `GET` | `GET /tasks`（全件）, `GET /tasks/{id}`（1件） |
| Update（更新） | `PUT` | `PUT /tasks/{id}` |
| Delete（削除） | `DELETE` | `DELETE /tasks/{id}` |

### `@RequestParam` と `@PathVariable`

```java
@GetMapping("/greet")
public String greet(@RequestParam String name) { ... }
// GET /greet?name=太郎 のようにクエリパラメータを受け取る

@GetMapping("/tasks/{id}")
public Task getTaskById(@PathVariable int id) { ... }
// GET /tasks/3 のようにURLパスの一部を受け取る
```

「リソースを特定するID」は`@PathVariable`、「検索条件やオプション」は`@RequestParam`で使い分けるのが慣習。

### クラスをそのまま返すとJSONになる

```java
public class Task {
    private int id;
    private String title;
    private boolean done;
    // コンストラクタ・getter・(更新用の)setter
}
```

`@RestController`のメソッドが`Task`や`List<Task>`をそのまま`return`すると、Jacksonというライブラリがgetterを見て自動的にJSONへ変換する。

### `@RequestBody`はボディ全体を型に変換する

```java
@PostMapping("/tasks")
public Task createTask(@RequestBody Task task) {
    return taskService.create(task.getTitle());
}

@PutMapping("/tasks/{id}")
public Task updateTask(@PathVariable int id, @RequestBody Task task) {
    return taskService.update(id, task.getTitle());
}
```

`@RequestBody`は「リクエストボディ全体を、指定した型に変換して受け取る」というアノテーション。「JSONの中から特定のキーだけ取り出す」という意味ではない。ボディがJSONオブジェクト（`{"title": "...", ...}`）なら、対応するクラス（`Task`）で丸ごと受け取り、必要なフィールドをgetterで取り出す。

### 更新（Update）にはsetterが必要

`Task`はこれまで`private`フィールド＋getterだけだったが、値を変更する手段がないと更新できないため、`setTitle(String title)`を追加する必要がある（`11-encapsulation`のgetter/setterの実践例そのもの）。

## 覚えておくべきルール・規約

- `.java`ファイルには必ず`package`宣言を書く。抜けていると物理的に同じフォルダでも別パッケージ扱いになり、他クラスから見えなくなる
- 自作クラスの名前が、使用中のライブラリの既存クラス名と衝突すると、エディタの自動importが誤ったクラスを選ぶことがある（例: 自作`Task`と`org.springframework.scheduling.config.Task`）
- 日本語などマルチバイト文字を含むリクエストを`curl`で送る場合、URLに直接書くと`400 Bad Request`になる。`-G --data-urlencode`でクエリパラメータをエンコードするか、`-d`でJSONボディとして送る
- `mvn spring-boot:run`はコードを保存しただけでは反映されない。修正後は一度停止（`Ctrl+C`）して再起動する必要がある
- ポートが使用中で起動できない場合、`lsof -i :ポート番号`で何が使っているか確認する。無関係な別プロジェクトが使っている場合は、そちらを止めずに`application.properties`の`server.port`を変更する方が安全
- 存在しないリソースへの`PUT`/`DELETE`は、今の実装では`200 OK`を返してしまう。本来は`404 Not Found`を返すのが望ましく、`ResponseEntity`を使うと実現できる（発展課題）

## 演習

`20-spring-boot/src/main/java/com/example/demo/`に以下を実装。

1. `Task.java`: `id`, `title`, `done`を持つクラス（`setTitle`を含む）
2. `TaskService.java`（`@Service`）: `getAll()`, `getById(id)`, `create(title)`, `update(id, title)`, `delete(id)`
3. `TaskController.java`（`@RestController`）: `GET /tasks`, `GET /tasks/{id}`, `POST /tasks`, `PUT /tasks/{id}`, `DELETE /tasks/{id}`

## つまずきの分析

### package宣言漏れと誤import

**何が起きたか**: `Task.java`に`package com.example.demo;`がなく、さらに`TaskService.java`が`org.springframework.scheduling.config.Task`という無関係な同名クラスを誤ってimportしていた。

**教訓**: 汎用的な名前（`Task`, `User`など）のクラスを作る際は、既存ライブラリの同名クラスと衝突しないか、import文が自分のパッケージを指しているか確認する。

### ポート競合

**何が起きたか**: `mvn spring-boot:run`が「Port 8080 was already in use」で失敗。`lsof -i :8080`で調べたところ、無関係な別プロジェクト（`prospi-a-app/backend`）が使用中だった。

**教訓**: 無関係なプロセスをむやみに停止させず、`application.properties`に`server.port=8081`を設定してポートを変更する方が安全。

### curlでの日本語URLエンコード

**何が起きたか**: `curl -X POST "...?title=牛乳を買う"`のように日本語を直接URLに書いて`400 Bad Request`になった。

**教訓**: `-G --data-urlencode "title=..."`で正しくURLエンコードする。

### `@RequestBody`の型の誤解

**何が起きたか**: `@RequestBody String title`と書き、JSONオブジェクトのボディを直接`String`型で受け取ろうとして`400 Bad Request`になった。

**なぜ**: `@RequestBody`は「ボディ全体を指定した型に変換する」ものであり、「JSON内の特定のキーだけ取り出す」ものではない。

**教訓**: `@RequestBody`の型は、送られてくるJSONの構造と一致させる。特定のフィールドだけ欲しい場合も、一度対応するクラスで受け取ってからgetterで取り出す。

### コード修正後のサーバー再起動忘れ

**何が起きたか**: `@RequestBody`の型を修正した後も同じ`400`エラーが再現し続けた。別インスタンスで検証したところコード自体は正常に動作したため、原因は「編集後にサーバーを再起動していなかったこと」と判明。

**教訓**: Spring Bootは保存しただけでは変更が反映されない（devtoolsなどのホットリロード設定をしていない限り）。コード修正後は必ずアプリを再起動してから動作確認する。

演習コードは `20-spring-boot/src/main/java/com/example/demo/`。`mvn spring-boot:run`で起動し、`GET`/`POST`/`PUT`/`DELETE`一通りのCRUD操作を`curl`で確認済み。
