# AB Skeleton 検証用リポジトリ

このリポジトリは、**AB Skeletonと公開ライブラリの受け入れ検証専用のリポジトリ**です。
独立した利用側として、許容例の成功・境界違反の検出・修正後の成功・依存更新前後の動作を、個別のコミットとGitHub Actionsの実行履歴で確認します。

`demo.delivery` のコードは受け入れ試験用のサンプルです。テンプレート本体は、取得元の [AB Skeleton](https://github.com/lambig/ab-architecture-rules/blob/0db29f424b07a274bafbeb565fe94470d25bc6b0/skeleton/README.md) を参照してください。

検証範囲・取得元・残る判定は [ACCEPTANCE.md](ACCEPTANCE.md) に記録しています。
初回受け入れのコミットごとのCI結果は [PR #1](https://github.com/lambig/ab-skeleton-consumer/pull/1)、
今回のテンプレート更新のCIは [更新ブランチのActions](https://github.com/lambig/ab-skeleton-consumer/actions?query=branch%3Aacceptance%2Ftemplate-update) を参照してください。

## 検証の実行

`sh gradlew clean check run` で公開ライブラリの利用とアクセス境界を確認します。JDK 17以上でbuildし、アプリケーションはJava 11以上を対象とします。
正常時の出力は `H1 boundary passed` と `dispatch: ready` です。
CIはJDK 17でbuildし、`-PruntimeJavaHome` でJava 11を指定してテストと通常起動を検査します。
ローカルでも同じ条件で確認する場合は、このプロパティにJava 11のインストール先を指定してください。

CIはテスト成功時・失敗時のJUnit XMLとHTMLレポートを、実行対象SHAと再実行番号を含む `boundary-test-results-*` 成果物として14日間保存します。
Actionsの実行画面から取得し、違反の診断を確認できます。レポート保存は失敗を成功へ変えません。
コンパイルや依存取得で失敗してXMLが生成されない場合、または実行をキャンセルした場合は保存しません。実行ログで原因を確認してください。

toolboxは通常依存、ab-architecture-rules・ArchUnit・JUnitはテスト依存です。版は `gradle.properties` に固定しています。
規則は専用遷移の直接呼出し・メソッド参照を操作内に限定します。注釈漏れ・import範囲外・reflection・業務順序は保証しません。
自分のpackageへ移す場合は `BoundaryCheck` のimport範囲を更新してください。

取得元とファイルのハッシュは `skeleton-source.json` にあります。その `sourceCommit` に固定した
`https://github.com/lambig/ab-architecture-rules/blob/<sourceCommit>/skeleton/README.md` が導入・違反の修正・更新手順です。
テンプレートの再生成は空の別ディレクトリへ行い、既存の変更と比較して取り込んでください。
