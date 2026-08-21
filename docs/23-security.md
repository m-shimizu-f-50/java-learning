# 23. 認証認可（Spring Security）

## 用語集

- **認証（Authentication）**: 「あなたは誰ですか？」を確認するプロセス（ログイン）
- **認可（Authorization）**: 認証された上で「あなたは何をしてよいですか？」を確認するプロセス（権限チェック）
- **401 Unauthorized**: 認証されていない（誰か分からない）ときに返るステータスコード
- **403 Forbidden**: 認証はされているが、その操作をする権限がないときに返るステータスコード
- **`@Bean`**: メソッドの戻り値をSpringのIoCコンテナに登録する目印。`@Component`系がクラスに付けるのに対し、`@Bean`はメソッドに付ける
- **HTTP Basic認証**: ユーザー名とパスワードをHTTPヘッダーに載せて送る、最もシンプルな認証方式
- **CSRF（クロスサイトリクエストフォージェリ）**: ブラウザが自動でCookieを送ってしまう性質を悪用し、ユーザーの意図しない操作を第三者のサイトから実行させる攻撃。Spring Securityはデフォルトでこれへの対策を有効にしている

## 使い方

### 認証と認可の違い

会社に例えると、入館証で「誰であるか」を確認するのが認証、部署ごとに「入れる部屋が違う」のが認可。

### Spring Securityがリクエストの流れに割り込む

```
リクエスト → [Spring Securityのフィルター] → Controller → Service → Repository
```

`spring-boot-starter-security`を追加すると、何も設定しなくても**全エンドポイントが自動的にログイン必須**になる（Spring Bootの「まず安全側に倒す」という思想）。デフォルトのユーザー名は`user`、パスワードは起動時にランダム生成されコンソールに表示される。

### SecurityFilterChainでカスタマイズ

```java
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // セッションを使わないREST APIではCSRF保護は不要
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/tasks/**").permitAll() // GETは誰でもOK
                .anyRequest().authenticated() // それ以外（POST/PUT/DELETE）は認証必須
            )
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
```

### 動作確認

**1. アプリを起動し、起動ログからパスワードを控える**

```bash
cd 20-spring-boot
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
mvn spring-boot:run
```

起動ログに以下のような行が出る（起動のたびにパスワードは変わる）。

```
Using generated security password: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
```

**2. 別のターミナルタブで確認する**

```bash
# 認証なしでGET → 200（誰でも見られる）
curl -s -o /dev/null -w "status: %{http_code}\n" http://localhost:8090/tasks

# 認証なしでPOST → 401（未認証は拒否される）
curl -s -o /dev/null -w "status: %{http_code}\n" -X POST http://localhost:8090/tasks \
  -H "Content-Type: application/json" -d '{"title":"テスト","id":0,"done":false}'

# 認証ありでPOST → 成功する（さっき控えたパスワードに置き換える）
curl -s -u "user:控えたパスワード" -X POST http://localhost:8090/tasks \
  -H "Content-Type: application/json" -d '{"title":"テスト","id":0,"done":false}'
echo

# 全件取得して、作成したタスクが反映されているか確認
curl -s http://localhost:8090/tasks; echo
```

**3. 終わったら起動したターミナルで`Ctrl + C`で停止する**

## 覚えておくべきルール・規約

- `SecurityConfig`のような設定クラスも、`@SpringBootApplication`と同じパッケージ以下に置く必要がある（コンポーネントスキャンの対象になるため）
- `@Bean`はメソッドに付け、そのメソッドの戻り値をIoCコンテナに登録する（`@Component`系はクラスに付ける点との違い）
- **状態を持たない（セッションを使わない）REST APIでは、CSRF保護は無効化するのが定石**。CSRFはブラウザの自動Cookie送信を悪用する攻撃で、Basic認証やトークン認証のようにブラウザが自動送信しない認証方式には当てはまらない。無効化しないと、認証情報が正しくても`POST`/`PUT`/`DELETE`が拒否される
- 認証エラーは`401`、認可エラー（権限不足）は`403`

## 演習

`20-spring-boot/src/main/java/com/example/demo/`に以下を実装。

1. `pom.xml`に`spring-boot-starter-security`を追加
2. `SecurityConfig.java`（`@Configuration`）を作成し、`SecurityFilterChain`の`@Bean`で`GET /tasks/**`は`permitAll()`、それ以外は`authenticated()`、HTTP Basic認証を設定
3. `GET`は認証なしで通り、`POST`は認証情報なしだと`401`、認証情報ありだと成功することを`curl`で確認

## つまずきの分析

### CSRF保護がPOSTリクエストを拒否する

**何が起きたか**: 正しいユーザー名・パスワードでBasic認証を行っても、`POST /tasks`が`401`で拒否され続けた。デバッグログ（`logging.level.org.springframework.security=DEBUG`）を確認したところ、実際には`CsrfFilter`が「Invalid CSRF token」として`403`を返しており、その後の内部的なエラー処理の過程で最終的に`401`（`WWW-Authenticate`付き）として返っていたことが判明した。

**なぜ**: Spring Securityはデフォルトで、状態を変更するリクエスト（`POST`/`PUT`/`DELETE`）にCSRFトークンを要求する。これはブラウザのセッション・Cookieを使ったログインを想定した保護だが、今回のようにBasic認証でAPIを直接叩く構成には不要で、むしろ邪魔になる。

**教訓**: セッションを使わないステートレスなREST APIを構築する場合、`http.csrf(csrf -> csrf.disable())`でCSRF保護を無効化するのが定石。認証情報が正しいのに`401`/`403`が返る場合は、まずCSRFを疑う。原因調査には`logging.level.org.springframework.security=DEBUG`でリクエストがフィルターチェーンのどこで止まっているかを確認するのが有効。

### ポート競合（無関係なプロセスとの衝突、2回目）

**何が起きたか**: `mvn spring-boot:run`が「Port 8081 was already in use」で失敗。`lsof -i :8081`で調べたところ、Dockerの関連プロセスが使用中だった（`20-spring-boot-rest`のつまずきで発生した`prospi-a-app`との衝突に続き2回目）。

**教訓**: 無関係なプロセスは停止させず、`application.properties`の`server.port`を空いているポートに変更する。ローカル環境で複数のアプリ・ツールを併用していると、こうしたポート競合はしばしば起きるので、都度`lsof -i :ポート番号`で確認する習慣をつける。

演習コードは `20-spring-boot/src/main/java/com/example/demo/SecurityConfig.java`。`mvn spring-boot:run`で起動し、`GET`は認証なしで成功、`POST`は未認証で`401`・認証ありで成功することを`curl`で確認済み（ポートは`8090`）。
