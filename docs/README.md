# アーキテクチャ図とクラス図

このディレクトリには、プロジェクトの設計図が含まれています。

## ファイル一覧

- `class-diagram.puml` - PlantUML形式のクラス図
- `class-diagram.drawio` - draw.io形式のクラス図

## クラス図の表示方法

### PlantUML形式 (class-diagram.puml)

1. **オンラインで表示**
   - [PlantUML Online](https://www.plantuml.com/plantuml/uml/)にアクセス
   - ファイルの内容をコピー&ペースト

2. **VS Codeで表示**
   - PlantUML拡張機能をインストール
   - `.puml`ファイルを開いてプレビュー

3. **draw.ioで使用**
   - draw.ioの「Arrange」→「Insert」→「Advanced」→「PlantUML」
   - ファイルの内容を貼り付け

### draw.io形式 (class-diagram.drawio)

1. [draw.io](https://www.drawio.com/)にアクセス
2. 「Open Existing Diagram」を選択
3. `class-diagram.drawio`ファイルを開く

## クラス図の構造

### パッケージ構成

- **com.example** - ルートパッケージ
  - HelloServlet - サンプルServlet（独立）

- **com.example.controller** - コントローラー層
  - ControlServlet - フロントコントローラー

- **com.example.model.service** - サービス層
  - User - ユーザーエンティティ
  - UserService - ビジネスロジック

- **com.example.model.dao** - DAO層
  - DatabaseManager - DB接続管理
  - UserDao - ユーザーデータアクセス

### 主な関係

- **継承**: ControlServlet, HelloServlet → HttpServlet
- **依存**: ControlServlet → UserService → UserDao → DatabaseManager
- **生成**: UserDao → User
