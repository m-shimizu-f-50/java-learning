# 20. Spring Boot基礎（DI・IoC）

## 用語集

- **DI（依存性注入 / Dependency Injection）**: オブジェクトが必要とする部品を自分で`new`せず、外部（フレームワーク）から渡してもらう設計
- **IoC（制御の反転 / Inversion of Control）**: オブジェクトを組み立てる主導権が、プログラマ自身からフレームワークに移ること
- **IoCコンテナ**: Springがアプリ起動時に部品（Bean）を生成・保管し、必要な場所に自動的に注入する仕組み
- **Bean**: SpringのIoCコンテナが管理しているオブジェクトのこと（`@Component`系のアノテーションが付いたクラスから生成される）
- **フレームワーク**: アプリの土台となる仕組みを提供し、開発者が本来のロジックに集中できるようにするソフトウェア（Spring Bootはその1つ）

## 使い方

### プロジェクト構成（Maven）

```
20-spring-boot/
  pom.xml
  src/main/java/com/example/demo/
    DemoApplication.java   # エントリーポイント
    GreetingService.java   # @Serviceを付けたビジネスロジック
    GreetingController.java # @RestControllerを付けたHTTP受付
  src/main/resources/
```

単一の`.java`ファイルではなく、`pom.xml`（依存関係・ビルド設定）を持つMavenプロジェクトとして構成する。

### DI（依存性注入）

```java
// 従来のやり方: 自分でnewする
class Car {
    private Engine engine = new Engine();
}

// DI: 外から受け取る（自分ではnewしない）
class Car {
    private final Engine engine;
    Car(Engine engine) { this.engine = engine; }
}
```

「部品を自分で`new`せず、コンストラクタで外から受け取る」のがDI。渡しているのが`String`や`double`ではなく「別のオブジェクト」という点だけがこれまでのコンストラクタと違う。

### IoC（制御の反転）とSpringのアノテーション

```java
@Service // 「このクラスはSpringに管理してもらう部品」→ 起動時に自動で1つ生成され、コンテナに保管される
public class GreetingService {
    public String greet(String name) {
        return "こんにちは、" + name + "さん";
    }
}
```

```java
@RestController // 「これはHTTPリクエストを受け付けるクラス」
public class GreetingController {
    private final GreetingService greetingService;

    // コンストラクタが1つだけなら@Autowiredは省略可能
    // Springがコンテナ内のGreetingServiceを自動的に見つけて渡してくれる
    public GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        return greetingService.greet(name);
    }
}
```

- `@Component`/`@Service`/`@RestController`: 「このクラスをSpringのIoCコンテナに登録してほしい」という目印
- コンストラクタでの受け取り: IoCコンテナが該当する型のインスタンスを自動的に探して渡す（＝DI、これが`@Autowired`の実体。コンストラクタが1つだけなら`@Autowired`アノテーション自体は省略可能）

### イメージで理解する：Springは何を2段階でやっているか

Springがやっていることは、大きく分けて2つのステップ。

1. **「これは部品です」と目印を付ける（`@Service`/`@RestController`など）**: アプリ起動時、Springはこれらの目印が付いたクラスを**全部探し出し**、それぞれ1個ずつインスタンスを自動生成して、**部品倉庫（IoCコンテナ）**に保管しておく
2. **倉庫から取り出して自動で渡す（コンストラクタでの受け取り＝`@Autowired`の実体）**: `GreetingController`が`GreetingService`を必要としている（コンストラクタで受け取ろうとしている）とき、Springは「倉庫の中に`GreetingService`型のものはあるか？」と探し、見つけたら自動的に渡す

この目印（`@Service`など）がないクラスは、Springがそもそも存在に気づかないため倉庫に入らず、DIで受け取ろうとしてもエラーになる。

### 実行・動作確認

```bash
mvn spring-boot:run
curl -G "http://localhost:8080/greet" --data-urlencode "name=太郎"
# こんにちは、太郎さん
```

## 覚えておくべきルール・規約

- `@Component`系のアノテーションが付いていないクラスはSpringに認識されず、DIの対象にならない
- コンストラクタが1つだけのクラスは`@Autowired`を省略できる（Springが自動的に注入ポイントと判断する）
- 日本語などマルチバイト文字を含むURLを`curl`で叩く場合は`--data-urlencode`でURLエンコードする必要がある（生の文字列だとTomcatが`400 Bad Request`を返す）

## 演習

`20-spring-boot/src/main/java/com/example/demo/`にて以下を実装。

1. `GreetingService`: `@Service`を付け、`greet(String name)`で挨拶文を返す
2. `GreetingController`: `@RestController`を付け、コンストラクタで`GreetingService`をDIし、`GET /greet?name=xxx`で呼び出す

## つまずきの分析

- Mavenが未インストールだったため、Homebrewでインストール。`mvn`がデフォルトでHomebrewのJDK 26を参照してしまい、プロジェクトが使うJDK 21と食い違っていたため、`JAVA_HOME=$(/usr/libexec/java_home -v 21)`を明示して実行する必要があった
- `GreetingService.java`のファイル名の先頭に誤って半角スペースが入っていた（` GreetingService.java`）。ファイル名とpublicクラス名の一致というJavaのルール（[01. 変数と型](01-variables-types.md)で学習済み）に反するため、リネームして解決
- DI/IoCの概念説明だけでは理解しにくかったため、`Car`/`Engine`という身近な例で「自分でnewする」vs「外から受け取る」の対比を先に示し、その後で`@Service`/`@RestController`の実コードに繋げる説明順にしたところ理解できた

演習コードは `20-spring-boot/src/main/java/com/example/demo/`。`mvn spring-boot:run`で起動し、`curl`で`GET /greet?name=太郎`が`"こんにちは、太郎さん"`を返すことを確認済み。
