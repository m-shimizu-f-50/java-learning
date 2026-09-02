# 27. ページネーション

## 用語集

- **ページネーション**: 大量のデータを一度に全件返さず、「1ページあたり何件」で区切って返す仕組み
- **`Pageable`**: 「何ページ目を、1ページ何件で、どう並び替えて欲しいか」というリクエスト情報を表すインターフェース
- **`Page<T>`**: 該当ページのデータに加えて、全件数・全ページ数などのメタ情報も持つレスポンス用の入れ物

## 使い方

### `JpaRepository`は最初からページネーションに対応している

`21-spring-data-jpa`で「空のインターフェースなのに`findAll()`が自動的に使える」と学んだが、`JpaRepository`にはページネーション対応版の`findAll`も最初から用意されている。`TaskRepository.java`自体は変更不要。

```java
Page<Task> findAll(Pageable pageable); // JpaRepositoryに元々ある
```

### Service・Controllerの実装

```java
// TaskService
public Page<Task> getAll(Pageable pageable) {
    return taskRepository.findAll(pageable); // Pageable IN → Page OUT
}
```

```java
// TaskController
@GetMapping("/tasks")
public Page<TaskResponseDto> getAllTasks(Pageable pageable) {
    return taskService.getAll(pageable).map(TaskResponseDto::new); // Page.map()でEntity→DTO変換
}
```

`Page<T>.map(...)`は`10-stream`のStream.mapと同じ「1件ずつ別の形に変換する」処理だが、変換後も`totalElements`（全件数）などのページ情報を保持し続ける点が異なる。

### `Pageable`はクエリパラメータから自動的に組み立てられる

`26-exception-handling`で「Spring（DispatcherServlet）が裏で自動的に何かをしてくれる」パターンを学んだが、ここでも同様に、`Pageable pageable`という引数を書くだけで、SpringがURLのクエリパラメータを読み取り自動的に組み立てて渡してくれる。

```bash
curl "http://localhost:8090/tasks?page=0&size=2&sort=title,asc"
```

- `page=0`: 0ページ目（0始まり）
- `size=2`: 1ページ2件
- `sort=title,asc`: `title`の昇順で並び替え

### Spring・Spring Data JPA・Spring Bootの関係

| | 役割 |
|---|---|
| Spring | DIコンテナなど、フレームワーク全体の基盤 |
| Spring Data JPA | `JpaRepository`など、DBアクセスを楽にする機能を提供するプロジェクト |
| Spring Security | 認証認可を提供するプロジェクト |
| Spring Boot | これらのプロジェクトを`spring-boot-starter-〇〇`という形で簡単に導入・自動設定してくれる仕組み |

`JpaRepository`はSpring Data JPAが提供する機能であり、Spring Bootそのものではない。「Spring Boot」は個別機能というより、Spring関連の様々な機能を簡単に束ねて使えるようにする仕組み、と捉えると理解しやすい。

## 覚えておくべきルール・規約

- ページネーションは`Repository`層に手を加える必要がなく、`Pageable`を引数で受け取り渡すだけで実現できる
- `Page`のデータの流れは「`Pageable`を引数として渡す → `Page`が戻り値として返る」の一方向
- `Pageable`はSpringがクエリパラメータ（`?page=`, `?size=`, `?sort=`）から自動的に組み立てる。自分でパースする必要はない
- 同名で紛らわしい別クラス（`org.hibernate.query.Page`など）が存在するため、importが正しいパッケージ（`org.springframework.data.domain`）を指しているか確認する

## 演習

`20-spring-boot/src/main/java/com/example/demo/`に以下を実装。

1. `TaskService.java`: `getAll()`を`getAll(Pageable pageable)`に変更し、`taskRepository.findAll(pageable)`を返す
2. `TaskController.java`: `getAllTasks()`を`getAllTasks(Pageable pageable)`に変更し、`Page<Task>`を`.map(TaskResponseDto::new)`で`Page<TaskResponseDto>`に変換して返す
3. `?page=0&size=2`のようなクエリでページ単位の取得ができることを`curl`で確認

## つまずきの分析

### 同名の別クラスを誤ってimportする（3回目）

**何が起きたか**: `Page`/`Pageable`のimportで、エディタの自動importが`org.hibernate.query.Page`（ジェネリクス非対応の別クラス）と`SpringDataWebProperties.Pageable`（Spring Bootの設定プロパティ用の別クラス）を選んでしまい、コンパイルエラーになった。

**なぜ**: `20-spring-boot-rest`（`Task`と`org.springframework.scheduling.config.Task`）、`20-spring-boot`のTaskService（今回）と、汎用的な名前のクラスを使う際に同じ種類のミスが繰り返し発生している。

**教訓**: 汎用的な名前（`Task`, `Page`, `Pageable`など）を使う際は、importが期待するパッケージ（今回であれば`org.springframework.data.domain`）を指しているか、追加のたびに確認する習慣をつける。

演習コードは `20-spring-boot/src/main/java/com/example/demo/TaskService.java`, `TaskController.java`。`mvn spring-boot:run`で起動し、`?page=0&size=2`と`?page=1&size=2`でそれぞれ異なるタスクが返り、`totalElements`/`totalPages`などのメタ情報も正しく含まれることを`curl`で確認済み。

---

これで「実務でよくある追加パターン」（DTO・バリデーション・例外ハンドリングの一元化・ページネーション）が全て完了。
