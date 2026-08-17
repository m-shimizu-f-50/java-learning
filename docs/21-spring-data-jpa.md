# 21. Spring Boot × DB連携（Spring Data JPA）

## 用語集

- **JPA（Java Persistence API）**: Javaのオブジェクトとデータベースのテーブルを自動的に対応付ける仕組み（ORM）の標準規格
- **ORM（Object-Relational Mapping）**: オブジェクト指向のプログラムとリレーショナルデータベースを対応付ける技術全般
- **エンティティ（Entity）**: `@Entity`が付いた、DBのテーブルに対応するクラス
- **主キー（Primary Key）**: テーブルの各行を一意に識別する列。`@Id`で指定する
- **H2**: インストール不要で使えるインメモリ（メモリ上で動く）データベース。学習・テスト用途の定番
- **Optional**: 「値があるかもしれないし、無いかもしれない」ことを表す入れ物クラス。`null`を直接扱う代わりに使う

## 使い方

### エンティティの定義

```java
@Entity // DBのテーブルに対応するクラスだと示す
public class Task {
    @Id // 主キー
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DBに自動採番させる
    private int id;

    private String title;
    private boolean done;

    public Task() { } // JPAが内部でインスタンス生成に使うため必須

    public Task(String title, boolean done) { // idは渡さない（DBが決める）
        this.title = title;
        this.done = done;
    }
    // getter/setterは通常のクラスと同じ
}
```

### Repositoryはインターフェースを1つ書くだけ

```java
public interface TaskRepository extends JpaRepository<Task, Integer> {
    // 中身は空でよい。findAll(), findById(), save(), deleteById()などが自動的に使えるようになる
}
```

`JpaRepository<Task, Integer>`の`Integer`は主キーの型。Springが実行時にこのインターフェースの実装を自動生成する。

### Optionalの扱い方

```java
public Task getById(int id) {
    return taskRepository.findById(id).orElse(null); // Optional<Task> → 中身があれば取り出し、なければnull
}
```

`findById`は`Task`ではなく`Optional<Task>`を返す。`.orElse(null)`を付けることで、これまでと同じ「見つからなければnull」という挙動に揃えられる。

### application.propertiesの設定

```properties
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update   # エンティティ定義からテーブルを自動生成
spring.jpa.show-sql=true               # 実行されるSQLをログに出す
spring.h2.console.enabled=true         # ブラウザでDBの中身を見られるH2コンソールを有効化
```

## 覚えておくべきルール・規約

- `@Entity`を付けたクラスには、JPAが使うための引数なしコンストラクタが必須
- `@GeneratedValue`で主キーの自動採番をDBに任せられる。自前でのID管理（`list.size() + 1`など）が不要になる
- `JpaRepository`の`save()`は最初からupsert（新規なら追加、既存のIDなら更新）の動きをする。手書きRepositoryで発生した「更新時の重複バグ」（[20. レイヤードアーキテクチャ](20-spring-boot-layers.md)参照）が起きない
- `findById`は`Optional<Task>`を返す。`.orElse(null)`で今まで通りの`null`前提のコードに揃えられる
- レイヤードアーキテクチャで層を分けていたおかげで、`TaskRepository`と`Task`のコンストラクタを変えるだけで済み、`TaskController`は無変更で本物のDBに切り替えられた

## 演習

`20-spring-boot/src/main/java/com/example/demo/`にて以下を実装。

1. `pom.xml`に`spring-boot-starter-data-jpa`と`h2`を追加（インフラ部分としてセットアップ済み）
2. `application.properties`にH2の接続設定を追加（セットアップ済み）
3. `Task.java`を`@Entity`にし、`@Id`/`@GeneratedValue`、引数なしコンストラクタを追加
4. `TaskRepository.java`を`class`から`interface extends JpaRepository<Task, Integer>`に書き換え、手書きの実装を削除
5. `TaskService.java`の`getById`を`Optional`対応にし、`create`のID採番処理を削除

## つまずきの分析

- `TaskRepository`をインターフェース化した際、コメントアウトした旧実装や`TaskService`の未使用importが残っていた。動作には影響しないが、コミット前に削除して整理した
- **教訓**: 大きな書き換え（クラス→インターフェースなど）をした後は、不要になった古いコード・importが残っていないか確認する習慣をつける

演習コードは `20-spring-boot/src/main/java/com/example/demo/`。`mvn spring-boot:run`で起動し、H2データベースに対してcreate→get→update→deleteの一連のCRUD操作を`curl`で確認済み（更新時の重複も発生しないことを確認）。

---

これでSpring Boot × DB連携（Spring Data JPA）が完了。
