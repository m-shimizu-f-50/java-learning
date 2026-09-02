# Java Learning

JavaScript経験者がJavaの基礎からSpring Bootの実務レベルまで学習するためのリポジトリ。

## 学習ロードマップ

- [x] Javaの基礎文法（変数・型、制御構文、配列、メソッド）
- [x] オブジェクト指向とJavaの型システム（クラス、継承、インターフェース、ポリモーフィズム）
- [x] Java応用（Stream API、ラムダ式、Optional、コレクション）
- [x] 開発環境構築（JDK, IntelliJ IDEA, Git）
- [x] Spring Boot基礎（DI/IoC、REST API、レイヤードアーキテクチャ）
- [x] Spring Boot × DB連携（Spring Data JPA）
- [x] 実務レベルの実践（テスト、認証認可、DTO・バリデーション・例外ハンドリング一元化・ページネーションをTaskアプリに実装）

## 環境

- JDK 21
- IntelliJ IDEA Community Edition

## ディレクトリ構成

演習コードはトピックごとにディレクトリを分けて管理する想定。復習チェックポイントの演習は`review/`配下に`review/01-static`のようにまとめる。

## 実行方法

各トピックのディレクトリに移動し、コンパイル・実行する。

```bash
cd 03-array
javac Main.java   # コンパイル（Main.classを生成）
java Main         # 実行（拡張子.classは付けない）
```

複数ファイルで構成されるトピックは、ワイルドカードで一括コンパイルできる。

```bash
cd 07-interface
javac *.java      # ディレクトリ内の全.javaファイルをまとめてコンパイル
java Main
```

`20-spring-boot`以降のSpring Bootプロジェクトは、Mavenで管理している。

```bash
cd 20-spring-boot
export JAVA_HOME=$(/usr/libexec/java_home -v 21)  # HomebrewのJDKと混同しないよう明示する
mvn spring-boot:run
```

デフォルトはポート8080だが、他プロジェクトと衝突する場合は`src/main/resources/application.properties`の`server.port`で変更できる（このリポジトリでは`8090`に設定済み）。動作確認は`curl`で行う。

```bash
curl -s http://localhost:8090/tasks; echo
```
