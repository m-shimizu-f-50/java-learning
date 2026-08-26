# 24. DTO（Data Transfer Object）

## 用語集

- **DTO（Data Transfer Object）**: API専用に用意する、データ受け渡し用のクラス。DBと直結した`Entity`とは別に用意する
- **コンストラクタ参照**: メソッド参照の一種。`ClassName::new`と書くと「引数を受け取ってそのクラスをnewする」処理を表せる（例: `TaskResponseDto::new`）

## 使い方

### なぜEntityをそのまま使わないのか

1. **DBの内部構造とAPIの形が結びついてしまう**: `Task`（Entity）にDB都合の列が増えると、意図せずAPIの形にも影響する
2. **クライアントが触ってはいけない項目まで送れてしまう**: `Task`をそのまま受け取ると、本来DBが決める`id`やサーバー側で管理すべき`done`もクライアントが指定できてしまう（リクエスト側の問題）
3. **返したくない情報まで返してしまう可能性がある**: 将来Entityに内部管理用の列が増えると、意図せずレスポンスに含まれてしまう（レスポンス側の問題）

「受け取る内容」と「返す内容」を、それぞれ意図的にコントロールするのがDTOの役割。

### リクエスト用DTOとレスポンス用DTO

```java
// リクエスト専用: クライアントが指定してよい項目だけ
public class TaskRequestDto {
    private String title;
    public String getTitle() { return title; }
}

// レスポンス専用: クライアントに返してよい項目だけ
public class TaskResponseDto {
    private final int id;
    private final String title;
    private final boolean done;

    public TaskResponseDto(Task task) { // Entity → DTOへの変換
        this.id = task.getId();
        this.title = task.getTitle();
        this.done = task.isDone();
    }
    // getter一式（Jacksonがgetterを見てJSONに変換するため必須）
}
```

### 一覧はStreamのmapでEntity→DTOに変換する

```java
@GetMapping("/tasks")
public List<TaskResponseDto> getAllTasks() {
    List<Task> tasks = taskService.getAll();
    return tasks.stream()
        .map(TaskResponseDto::new) // 1件ずつTask→TaskResponseDtoに変換
        .collect(Collectors.toList());
}
```

for文で書くなら「空のListを用意 → 1件ずつ変換して`add` → 返す」の3ステップに相当する処理を、`stream()`/`map()`/`collect()`の3ステップで宣言的に書いている（`10-stream`の応用）。

### nullチェックを忘れない

DTOへの変換を挟むと、変換元が`null`の場合に新たな注意点が生まれる。

```java
@GetMapping("/tasks/{id}")
public TaskResponseDto getTaskById(@PathVariable int id) {
    Task task = taskService.getById(id);
    if (task == null) {
        return null; // nullチェックせずnew TaskResponseDto(null)するとNullPointerException
    }
    return new TaskResponseDto(task);
}
```

Entityをそのまま返していた時は`null`を直接返すだけで問題なかったが、コンストラクタで`task.getId()`のようにフィールドへアクセスする以上、`null`を渡すとその場で例外になる。DTO化によって「存在しないケースの考慮」が新たに必要になる点は覚えておく。

## 覚えておくべきルール・規約

- DTOは「受け取っていい項目」「返していい項目」を明示的に絞り込むためのクラス
- レスポンス用DTOには必ずgetterを実装する（Jacksonがgetterを見てJSON化するため、getterがないと空のJSONになる）
- `ClassName::new`はコンストラクタ参照。「引数を受け取ってそのクラスをnewする」処理をラムダ式よりさらに簡潔に書ける
- Entityを直接返さずDTOに変換する場合、変換元が`null`になり得るケースを忘れずにチェックする（`new Dto(null)`は`NullPointerException`になる）

## 演習

`20-spring-boot/src/main/java/com/example/demo/`に以下を実装。

1. `TaskRequestDto.java`: `title`のみを持つリクエスト用DTO
2. `TaskResponseDto.java`: `id`/`title`/`done`とgetterを持つレスポンス用DTO、`Task`から変換するコンストラクタ
3. `TaskController.java`の`GET`/`POST`/`PUT`をDTO経由に書き換え

## つまずきの分析

### 変数名の重複と存在しない変数の参照

**何が起きたか**: `createTask`メソッドの引数名を`task`（`TaskRequestDto`型）にしたのに、メソッド内でも`Task task = ...`と同じ名前を再定義しようとし、さらに存在しない`request`という変数を参照してコンパイルエラーになった。

**教訓**: パラメータ名とメソッド内のローカル変数名が衝突しないよう、DTOの引数は`request`、Entityの変数は`task`のように役割ごとに名前を分けると事故が起きにくい。

### レスポンス用DTOにgetterがない

**何が起きたか**: `TaskResponseDto`にコメントで「getter省略」と書いたまま、実際にgetterを実装し忘れていた。動作確認すると空のJSON（`{}`）が返る状態だった。

**教訓**: JacksonはgetterまたはpublicフィールドをもとにJSONへ変換する。DTOを作る際は、フィールドを定義しただけで満足せず、対応するgetterまで実装したか必ず確認する。

### Entity→DTO変換時のnull考慮漏れ

**何が起きたか**: `getTaskById`/`updateTask`をDTO化した際、Entityが見つからない（`null`）ケースの考慮が漏れており、存在しないIDへのアクセスで`NullPointerException`（500エラー）が発生した。Entityをそのまま返していた時は問題にならなかった箇所。

**教訓**: `Task`のようなEntityから`TaskResponseDto`のようなDTOへ変換する処理を挟むと、「変換元が存在しない場合」への対応が新たに必要になる。DTO化のような設計変更を行う際は、正常系だけでなく異常系（存在しないIDなど）も必ず動作確認する。

演習コードは `20-spring-boot/src/main/java/com/example/demo/TaskRequestDto.java`, `TaskResponseDto.java`, `TaskController.java`。`mvn spring-boot:run`で起動し、CRUD一式と存在しないIDのケースを`curl`で確認済み。
