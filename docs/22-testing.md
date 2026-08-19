# 22. テスト（JUnit 5 + Mockito）

## 用語集

- **JUnit**: Javaでテストコードを書いて実行するための標準的なフレームワーク
- **Mockito**: 「偽物（モック）」のオブジェクトを作り、依存先を差し替えてテストするためのライブラリ
- **モック（mock）**: 本物の代わりに使う偽物のオブジェクト。呼ばれたときの返り値を自由に設定できる
- **単体テスト（Unit Test）**: 1つのクラス・メソッドだけを、他への依存をモックにして検証するテスト
- **統合テスト（Integration Test）**: 複数の層をまたいで、実際に近い形で動作するかを検証するテスト
- **アサーション（assert）**: 「期待した結果になっているか」を検証する文（`assertEquals`など）

## 使い方

### 基本構文

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test // 「これはテストメソッドです」という目印
    void addsTwoNumbers() {
        int result = 2 + 3;
        assertEquals(5, result); // 期待値と実際の値が一致するか検証
    }
}
```

### なぜモックが必要か

`TaskService`をテストする際、本物の`TaskRepository`（本物のDB）を使うとテストが遅く不安定になる。`TaskService`がコンストラクタで`TaskRepository`を受け取る設計（DI）になっているからこそ、本物の代わりに**偽物を差し込める**。

```java
TaskRepository fakeRepository = mock(TaskRepository.class); // ①偽物を作る
when(fakeRepository.findAll()).thenReturn(List.of(...));    // ②「呼ばれたらこれを返す」という台本を設定

TaskService taskService = new TaskService(fakeRepository);  // ③偽物をDIで注入
List<Task> tasks = taskService.getAll();                    // ④実行

assertEquals(2, tasks.size());                               // ⑤結果を検証
```

「偽物を用意して台本を設定 → DIで注入 → 実行 → 結果を検証」という4ステップが単体テストの基本の型。

### 戻り値がない処理は`verify`で検証

```java
taskService.delete(1);
verify(fakeRepository).deleteById(1); // 「deleteById(1)が呼ばれたか」を確認
```

| | 何を確認するか |
|---|---|
| `assertEquals`/`assertTrue`など | メソッドの**戻り値**が期待通りか |
| `verify(...)` | 偽物の**特定のメソッドが呼ばれたか**（`void`など戻り値がない処理の確認に向く） |

## 覚えておくべきルール・規約

- テストコードはMavenの規約で`src/test/java`配下に置く（`src/main/java`とはディレクトリが分かれる）
- `mock(型.class)`で偽物を作り、`when(...).thenReturn(...)`で「呼ばれたら何を返すか」を設定する
- DIでコンストラクタから依存を受け取る設計になっているクラスは、テスト時に本物の代わりに偽物を注入できる（`20-spring-boot-di`で説明したDIのメリットが実際に活きる場面）
- 戻り値の検証は`assertEquals`系、「メソッドが呼ばれたか」の検証は`verify`を使う

## 演習

`20-spring-boot/src/test/java/com/example/demo/TaskServiceTest.java`にて以下を実装。

1. `getAll_returnsAllTasksFromRepository`: モックの`findAll()`が返すリストを`getAll()`経由で取得できるか検証
2. `create_savesNewTaskWithGivenTitle`: `create()`実行後、`save()`が呼ばれたか・作成内容が正しいかを検証
3. `delete_callsRepositoryDeleteById`: `delete()`実行後、`deleteById()`が正しい引数で呼ばれたかを`verify`で検証

## つまずきの分析

### 存在しないメソッド名を呼んでしまうコンパイルエラー

**何が起きたか**: `Task`クラスの実際のgetter（`getTitle()`, `isDone()`）ではなく、`getName()`, `isCompleted()`という存在しないメソッド名でアサーションを書いてしまい、コンパイルエラーになった。

**なぜ**: `Task`クラスの実際のフィールド名・メソッド名を確認せず、一般的にありそうな名前を仮定して書いてしまった。

**教訓**: モックのテストデータを作る際は、対象クラス（`Task`）の実際のgetter名を確認してから使う。

### 初見の概念（mock/when/verify）は一つずつ役割を分解して理解する

**何が起きたか**: `mock`, `when().thenReturn()`, `verify()`が一度に出てきて、1問目で「何をしているのか」が分からなくなった。

**教訓**: 「偽物を作る（mock）→ 台本を設定する（when/thenReturn）→ DIで注入する → 実行する → 結果 or 呼び出しを検証する（assert / verify）」と役割ごとに分解すると理解しやすい。`assert`系は戻り値の検証、`verify`は「呼ばれたかどうか」の検証、という使い分けも重要。

演習コードは `20-spring-boot/src/test/java/com/example/demo/TaskServiceTest.java`。`mvn test`で実行し、3件全て成功することを確認済み。
