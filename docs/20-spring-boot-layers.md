# 20. Spring Boot基礎（レイヤードアーキテクチャ）

## 用語集

- **レイヤードアーキテクチャ**: Controller/Service/Repositoryのように、役割ごとに層（レイヤー）を分けるアプリケーション設計
- **Repository**: データの保存・取得だけを担当する層。業務ルールやHTTPのことは知らない
- **upsert**: 既存なら更新、なければ新規追加する動作（"update" + "insert"の合成語）。実務のDB操作やO/Rマッパーでよく使われる考え方
- **O/Rマッパー（ORM）**: Javaのオブジェクトとデータベースのテーブルを自動的に対応付ける仕組み（次のトピックのJPAなど）

## 使い方

### 3層構造

| 層 | 役割 | 知っていいこと | 知ってはいけないこと |
|---|---|---|---|
| Controller | HTTPリクエストの受付・返却 | どのURL・HTTPメソッドに対応するか | データの保存方法、業務ルールの詳細 |
| Service | 業務ロジック（ルール・判断） | 「何をすべきか」の判断 | HTTPの詳細、データの保存方法の詳細 |
| Repository | データの保存・取得のみ | データの読み書き方法 | 業務ルール、HTTPの詳細 |

```
リクエスト → Controller → Service → Repository → (データ)
```

### 実装イメージ

```java
@Repository
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    public List<Task> findAll() { return tasks; }
    public Task findById(int id) { ... }

    public void save(Task task) {
        tasks.removeIf(t -> t.getId() == task.getId()); // 既存なら削除してから
        tasks.add(task); // 追加（結果的に新規でも更新でも同じ動きになる＝upsert）
    }

    public void deleteById(int id) { tasks.removeIf(task -> task.getId() == id); }
}
```

```java
@Service
public class TaskService {
    private final TaskRepository taskRepository; // Service → Repository もDIでつながる

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(String title) {
        int id = taskRepository.findAll().size() + 1; // 採番という「業務ロジック」はServiceの仕事
        Task task = new Task(id, title, false);
        taskRepository.save(task); // 保存はRepositoryに任せる
        return task;
    }
}
```

`Controller → Service → Repository`とDIの連鎖が続く。`@Service`も`@Repository`も`@Component`の仲間で、Springに管理してもらう部品という点は同じ。

### なぜ分けるのか

Repository層を差し替えるだけで（例: `List`管理 → 本物のデータベース）、ServiceやControllerを一切変更せずに済む。`07-interface`で学んだ「呼び出し側は実装の詳細を知らなくていい」という考え方が、ここでもController/Serviceが「データがどう保存されているか知らない」という形で活きている。

### Before / After比較（使う場合と使わない場合）

**外から見た挙動（`curl`で叩いた結果）はBefore/Afterで一切変わらない**。変わるのは内部の役割分担だけ。

```java
// Before: レイヤー分割前
// TaskServiceが「業務ロジック」と「データの保存方法」の両方を抱えている
@Service
public class TaskService {
    private final List<Task> tasks = new ArrayList<>(); // 保存方法の詳細までServiceが知っている

    public void delete(int id) {
        tasks.removeIf(task -> task.getId() == id); // Listの操作方法まで直接書く
    }
}
```

```java
// After: レイヤー分割後
// TaskServiceは「削除して」とRepositoryに依頼するだけ
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public void delete(int id) {
        taskRepository.deleteById(id); // Listがどう実装されているか知らなくていい
    }
}
```

**使わない場合（Beforeのまま）に起きる問題:**

- `TaskService`が肥大化しやすい: 業務ロジックとデータ保存の実装が1クラスに混在し、コードが追いにくくなる
- データの保存方法を変えたくなったとき（`List`→本物のDBなど）、`TaskService`自体を書き換える必要が出てくる
- `TaskService`をテストしたいとき、`List`の中身まで含めて丸ごとテストすることになり、業務ロジックだけを狙ってテストしづらい

**使う場合（Afterの構成）で得られるメリット:**

1. **`TaskService`が「何をすべきか」に集中できる**: `List`の操作方法（`removeIf`, `add`など）を気にせず、業務の流れだけを見れば良くなる
2. **Repositoryだけ差し替えれば、Service・Controllerは無傷で済む**: 将来「本物のデータベースに保存したい」となっても、`TaskRepository`の中身を書き換えるだけで済む。`taskRepository.deleteById(id)`という呼び出し方はServiceを変える必要がない
3. **テストしやすくなる**: `TaskService`だけをテストしたいとき、本物の`TaskRepository`の代わりに「偽のRepository（モック）」を差し込める（DIのメリットがそのまま活きる）

つまりこの変更は「結果を変えるための修正」ではなく、「**将来の変更に強くするための設計変更（リファクタリング）**」だった、と捉えると分かりやすい。

## 覚えておくべきルール・規約

- Repositoryの`save()`は「新規なら追加、既存なら上書き」の**upsert**にするのが実務の標準的な設計（JPAの`save()`などもこの動きをする）
- `findById`などで取得したオブジェクトは、リスト内の「実物」への参照であることが多い。フィールドを直接書き換えると、保存処理を呼ばなくても元のデータが変わってしまう点に注意する
- Serviceに業務ロジック（IDの採番、バリデーションなど）を置き、Repositoryは純粋なデータの出し入れに専念させる

## 演習

`20-spring-boot/src/main/java/com/example/demo/`にて以下を実装。

1. `TaskRepository.java`（`@Repository`）を新規作成: `findAll()`, `findById(id)`, `save(task)`, `deleteById(id)`
2. `TaskService.java`を修正: `List<Task>`を直接持たず、コンストラクタで`TaskRepository`をDIし、各メソッドをRepositoryへの委譲に置き換える
3. `TaskController.java`は変更不要

## つまずきの分析

### `save()`が常に追加してしまい、更新時に重複が発生

**何が起きたか**: `update()`で`getById`から取得した`Task`を`setTitle`で直接書き換えた後、さらに`taskRepository.save(task)`を呼んでいた。`getById`が返すのはリスト内の実物への参照なので、`setTitle`の時点で既にリスト内のデータは更新済みだった。それにもかかわらず`save`（＝`tasks.add(task)`）をもう一度呼んだため、同じタスクがリストに2件重複して入ってしまった。

**なぜ**: `save()`を「常に新規追加」として実装しており、「既に同じIDが存在する場合は上書きする」という考慮がなかった。

**教訓**: Repositoryの`save()`は「新規か更新かをRepository側が判断して吸収する」設計にすると、呼び出し側（Service）は常に同じ書き方（`save()`を呼ぶだけ）で済む。これはJPAなど実際のO/Rマッパーでも採用されている標準的なパターン。

演習コードは `20-spring-boot/src/main/java/com/example/demo/`。`mvn spring-boot:run`で起動し、create→update→get→deleteの一連の流れで重複が発生しないことを確認済み。

---

これでSpring Boot基礎（DI/IoC、REST API、レイヤードアーキテクチャ）が一通り完了。
