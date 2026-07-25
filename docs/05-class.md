# 05. クラスとオブジェクト

## 要点

- `new クラス名(...)` でインスタンス生成（JSの`new`と同じ感覚）
- コンストラクタ: クラス名と同名、戻り値の型は書かない。インスタンス生成時に自動で呼ばれる
- `this`: インスタンス自身を指す（JSの`this`と近い、ただし挙動は単純）
- **1ファイルにpublicクラスは1つまで**、かつファイル名とpublicクラス名が一致する必要がある
  - データを表すクラス（`Book`）と実行の入口（`Main`）は別ファイルに分けるのが一般的
- 複数ファイルのコンパイルは `javac Book.java Main.java` のようにまとめて指定できる

## つまずいた点・フィードバック

- 最初、`Book.java`に`public class Book`と`public class Main`の両方を書いてしまい、
  「The public type Main must be defined in its own file」エラーが発生 → `Main.java`に分離して解決
- 分離後、VSCode上で「Book cannot be resolved to a type」という表示エラーが出たが、
  実際に`javac`でコンパイルすると成功。言語サーバーが新規ファイルをまだ認識していないだけの表示問題だった
  （`Java: Clean Java Language Server Workspace`やウィンドウリロードで解消可能）
- 演習コードは `05-class/Book.java`, `05-class/Main.java`。コンパイル・実行して動作確認済み。
