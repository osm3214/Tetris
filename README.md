# Tetris

## バージョン
- Java
```
openjdk 15.0.2 2021-01-19
OpenJDK Runtime Environment (build 15.0.2+7)
OpenJDK 64-Bit Server VM (build 15.0.2+7, mixed mode, sharing)
```

- maven
```
Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
Maven home: /usr/local/Cellar/maven/3.8.1/libexec
Java version: 15.0.2, vendor: N/A, runtime: /usr/local/Cellar/openjdk/15.0.2/libexec/openjdk.jdk/Contents/Home
Default locale: en_JP, platform encoding: UTF-8
OS name: "mac os x", version: "11.2.3", arch: "x86_64", family: "mac"
```

- その他外部パッケージ：pom.xmlを参照

## 実行方法

- Eclipse, VSCodeなどの実行ボタンから実行
- コンパイルして実行（？）
```
java @/var/folders/y_/gf7pn1j577g__d1_7yqz7bcw0000gn/T/cp_3dviyj6bpl7j0eqf78qmgyurc.argfile tetris.app.Main
```
- 外部リポジトリを含めたjarファイルから実行
```
mvn package # mavenでjarファイルにパッケージ
java -jar target/Tetris-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 操作方法
- スタート、一時停止、盤面のリセット：画面各ボタン
- 下移動：jもしくは↓
- 左移動：hもしくは←
- 右移動：lもしくは→
- ハードドロップ（ブロックを一気に下まで落とす）：kもしくは↑
- 回転（時計回り）：d
- 回転（反時計回り）：a


## フォルダ構成
```
Tetris
├── README.md
├── pom.xml # maven及び外部パッケージの情報
├── resources　#　リソースフォルダ
│   ├── golden_wind.wav # BGMファイル
│   └── models
│       └── tetrisnet.pt # AIの重みファイル（.pthではない）。dqn-tetris/test.pyを実行すると生成される。
├── src # ソースコード
│   ├── main
│   │   └── java
│   │       └── tetris
│   │           ├── app
│   │           │   ├── App.java # SwingのJFrameクラスを継承。アプリの一番外側
│   │           │   └── Main.java # mainを定義しているだけのクラス
│   │           ├── entity
│   │           │   ├── Action.java # Agentの選択するactionを定義
│   │           │   ├── Agent.java # 行動を選択するAI Agentの定義。GamePanelで生成され別スレッドのループ内で行動を選択している。
│   │           │   ├── AiPanel.java # プレイヤー（AI）の操作するパネル（画面左）。Board、NextPiecePanel、StatusPanelから構成。
│   │           │   ├── Board.java # 20x10の盤面を定義。各プレイヤーのパネルから生成（各画面左）。
│   │           │   ├── Brain.java # Agentの行動を選択するBrain。Agentから生成。
│   │           │   ├── GamePanel.java # PlayerPanelとAiPanelを持ち管理。Appから生成
│   │           │   ├── NextPiecePanel.java # 次落下するPieceを表示するパネル（各プレイヤーの画面右上）。PlayerPanel, AiPanelから生成。
│   │           │   ├── Piece.java # 基本となるテトリスのピース。Boardなどから生成
│   │           │   ├── PlayerPanel.java # プレイヤー（人間）の操作するパネル（画面左）。Board、NextPiecePanel、StatusPanelから構成。
│   │           │   └── StatusPanel.java # ステータスを表示するパネル（各プレイヤー画面右下）。PlayerPanel、AiPanelから生成。
│   │           └── util
│   │               └── SoundPlayer.java # bgm再生用のClipを生成
│   └── test # mvnによる自動生成。変更点なし
│       └── java
│           └── tetris
│               └── AppTest.java
└── target # mvnなどによる自動生成。変更点なし
    ├── Tetris-1.0-SNAPSHOT-jar-with-dependencies.jar
    ├── archive-tmp
    ├── classes
    │   └── tetris
    │       ├── app
    │       │   ├── App.class
    │       │   ├── Main$1$1.class
    │       │   ├── Main$1.class
    │       │   └── Main.class
    │       ├── entity
    │       │   ├── Action.class
    │       │   ├── Agent.class
    │       │   ├── AiPanel.class
    │       │   ├── Board.class
    │       │   ├── Brain$1.class
    │       │   ├── Brain.class
    │       │   ├── GamePanel$1.class
    │       │   ├── GamePanel$2.class
    │       │   ├── GamePanel$3.class
    │       │   ├── GamePanel$4.class
    │       │   ├── GamePanel$5.class
    │       │   ├── GamePanel$6.class
    │       │   ├── GamePanel.class
    │       │   ├── NextPiecePanel.class
    │       │   ├── Piece.class
    │       │   ├── PlayerPanel.class
    │       │   ├── StatusPanel$1.class
    │       │   └── StatusPanel.class
    │       └── util
    │           └── SoundPlayer.class
    ├── generated-sources
    │   └── annotations
    ├── maven-status
    │   └── maven-compiler-plugin
    │       └── compile
    │           └── default-compile
    │               ├── createdFiles.lst
    │               └── inputFiles.lst
    └── test-classes
        └── tetris
```
