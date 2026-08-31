# 26. 例外ハンドリングの一元化（`@ControllerAdvice`）

## 用語集

- **`@ControllerAdvice`**: アプリ全体の、全Controllerに対する例外処理を1箇所にまとめるクラスに付ける目印
- **`@RestControllerAdvice`**: `@ControllerAdvice`と`@ResponseBody`（戻り値をJSONとして返す指定）をまとめたアノテーション
- **`@ExceptionHandler(型)`**: `@ControllerAdvice`クラス内で、「この型の例外が飛んできたら、このメソッドで処理する」と宣言するアノテーション
- **`ResponseEntity<T>`**: ステータスコードとレスポンスボディの両方を明示的に指定できるクラス
- **Unchecked例外**: `RuntimeException`を継承した例外。呼び出し元に`throws`宣言や`try-catch`を強制しない（`15-exception`参照）

## 使い方

### 問題: 個々のControllerに同じチェックが散らばる

```java
// Before: 各メソッドにif (task == null)が重複
@GetMapping("/tasks/{id}")
public TaskResponseDto getTaskById(@PathVariable int id) {
    Task task = taskService.getById(id);
    if (task == null) {
        return null; // 実際には200 OK（空ボディ）が返ってしまう
    }
    return new TaskResponseDto(task);
}
```

### 独自例外を投げるようにする

```java
public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(int id) {
        super("Taskが見つかりません: id=" + id);
    }
}
```

```java
// TaskService
public Task getById(int id) {
    return taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException(id)); // 見つからなければ例外を投げる
}
```

### Controllerはシンプルになる

```java
// After: if文が不要になる（見つからなければここで例外が飛ぶので、以降の行は実行されない）
@GetMapping("/tasks/{id}")
public TaskResponseDto getTaskById(@PathVariable int id) {
    Task task = taskService.getById(id);
    return new TaskResponseDto(task);
}
```

### 例外を1箇所で受け止める

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleNotFound(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
```

`TaskController`のどのメソッドから投げられても、この1箇所だけで`404`への変換を担当する。将来`PartController`のような別のControllerを追加しても、同じ例外を投げれば自動的にここで処理される。

### 誰が`GlobalExceptionHandler`を呼んでいるのか

`GlobalExceptionHandler.handleNotFound(...)`は、コード上のどこからも**直接呼び出していない**。呼んでいるのはSpring自身（DispatcherServlet、リクエストを各Controllerに振り分ける中心的な受付役）で、例外が投げられたタイミングで自動的に発動する。`GET /tasks/999`（存在しないID）を例に流れを追うと：

```
1. リクエストが届く
   → DispatcherServletが TaskController.getTaskById(999) を呼ぶ

2. getTaskById内で taskService.getById(999) を呼ぶ

3. getById内で findById(999).orElseThrow(...) が
   TaskNotFoundException を投げる

4. getById はこの例外をcatchしていない
   → getTaskById まで例外が伝播する

5. getTaskById もcatchしていない
   → さらに上（DispatcherServlet）まで例外が伝播する

6. DispatcherServletが「TaskNotFoundExceptionだから、
   GlobalExceptionHandlerの@ExceptionHandler(TaskNotFoundException.class)
   に処理を任せよう」と判断し、handleNotFound(e) を自動的に呼び出す

7. handleNotFoundが返したResponseEntity（404 + メッセージ）が
   そのままHTTPレスポンスとしてクライアントに返る
```

`@Service`/`@RestController`が「Springに管理してもらう部品」として倉庫（IoCコンテナ）に登録される（[20. DI・IoC](20-spring-boot-di.md)参照）のと同じ仕組みで、`@RestControllerAdvice`は「例外処理担当」として登録される。DispatcherServletは例外が発生するたびに「登録済みの例外処理担当の中に、この例外型を扱えるものはあるか」を自動で探して呼び出す。

### 認証認可（Security）との方向の違い

`23-security`の`SecurityFilterChain`は、リクエストが**Controllerに届く前**に割り込んでチェックする仕組みだった。一方`@ExceptionHandler`は、ControllerやServiceで**例外が投げられた後**、レスポンスを返す直前に割り込む仕組み。「入り口で止める」か「出口で受け止める」かの違い。

## 覚えておくべきルール・規約

- `null`を返す設計は、意図せず`200 OK`（空ボディ）になってしまう。「見つからない」という異常系は例外として表現し、正しいステータスコード（`404`など）を返す
- `@ControllerAdvice`で例外処理を一元化すると、個々のControllerから重複したチェックコードを排除できる
- Serviceが例外を投げるように変更すると、それ以降のController側のコードは実行されない（`return`文を書く必要がなくなる）
- `ResponseEntity`を使うと、正常時の`200`以外の任意のステータスコードを明示的に返せる

## 演習

`20-spring-boot/src/main/java/com/example/demo/`に以下を実装。

1. `TaskNotFoundException.java`: `RuntimeException`を継承した独自例外
2. `TaskService.java`: `getById`が`orElseThrow`で`TaskNotFoundException`を投げるように変更
3. `TaskController.java`: `if (task == null)`のチェックを削除
4. `GlobalExceptionHandler.java`（`@RestControllerAdvice`）: `TaskNotFoundException`を`404`に変換するハンドラを実装
5. 存在しないIDへの`GET`/`PUT`が`404`を返すことを`curl`で確認

## つまずきの分析

### 不要になったコードの消し忘れ

**何が起きたか**: `TaskService`側を例外を投げる実装に変更したが、`TaskController`側の`if (task == null) { return null; }`が削除されないまま残っていた。

**なぜ**: `getById`が例外を投げるようになった時点で、`task`が`null`になることは二度と起こらない（`null`になる前に例外で処理が中断される）ため、このチェックは**到達不可能なコード**になっていたが、見た目上は動いてしまうため気づきにくかった。

**教訓**: Serviceの挙動（`null`を返す→例外を投げる）を変えたら、呼び出し側で「その`null`を前提にしていたコード」が残っていないか確認する。動作確認で問題が出なくても、意味のなくなったコードは削除しておく。

### `GlobalExceptionHandler`がどこで呼ばれているか分からない

**何が起きたか**: `GlobalExceptionHandler`を実装して動作は確認できたが、コード上のどこからも明示的に呼び出していないため、「どこで使われているのか」がイメージできていなかった。

**なぜ**: これまでの`Service`/`Repository`は`Controller`から明示的にメソッド呼び出し（`taskService.getById(id)`など）をしていたが、`@ExceptionHandler`はそのような明示的な呼び出しが一切なく、Spring（DispatcherServlet）が例外の伝播を検知して裏側で自動的に呼び出す、という他とは違う仕組みだった。

**教訓**: `@RestControllerAdvice`は`@Service`などと同じ「IoCコンテナに登録される部品」だが、呼ばれ方が「明示的な呼び出し」ではなく「例外が投げられた時にフレームワークが自動的に探して呼ぶ」という点で特殊。`23-security`のフィルターチェーンと同様、「自分では呼んでいないのに動く」処理を見たら、それはSpringが裏側で仲介している合図だと考える。

演習コードは `20-spring-boot/src/main/java/com/example/demo/TaskNotFoundException.java`, `TaskService.java`, `TaskController.java`, `GlobalExceptionHandler.java`。`mvn spring-boot:run`で起動し、存在しないIDへの`GET`/`PUT`が`404`とメッセージを返すこと、正常系は引き続き問題なく動くことを`curl`で確認済み。
