# 独立consumerの受け入れ

このリポジトリは、公開されたSkeletonを空のプロジェクトへ取り込み、公開Libraryの利用とアクセス境界をCIで確認する検証専用のリポジトリです。
コードは受け入れ試験用のサンプルです。

## 取得元と利用側の変更

現在の取得元は [ab-architecture-rulesのマージ版](https://github.com/lambig/ab-architecture-rules/tree/0db29f424b07a274bafbeb565fe94470d25bc6b0) です。
この40桁SHAをcheckoutし、`skeleton/create.py --source <SHA> --output <空のディレクトリ>` で生成しました。
`skeleton-source.json` のハッシュは取得直後の生成物を記録しています。利用側で変更した後の全ファイルのハッシュではありません。

利用側packageを `example.shipping` から `demo.delivery` へ移し、Mainの指定とHarnessのimport範囲を合わせています。
注釈・業務型・操作・適用範囲はこのconsumerが所有します。実行時に元checkoutを参照せず、Maven Centralの公開依存だけを使います。
元ソースのMIT Licenseを保持しています。

## 実行とCI履歴

JDK 17以上で `sh gradlew clean check run` を実行します。Java 11での利用確認には
`sh gradlew "-PruntimeJavaHome=<Java 11の場所>" clean check run` を使います。
生成されたGitHub ActionsもJDK 17でbuildし、Java 11でテスト・通常起動を実行します。

受け入れでは次のコミットを順に検証します。成功を装う `continue-on-error` やテストの無効化は使用しません。

1. 許容された操作と公開toolbox 0.4.0の実行。`H1 boundary passed` と `dispatch: ready` を確認します。
2. 操作外のClientに直接呼出しとメソッド参照を置き、対象規則の診断でCIが失敗することを確認します。
3. 同じClientの機能を操作経由へ戻し、CIが成功することを確認します。Clientを削除して修正を代用しません。
4. toolbox 0.3.1を使う更新前の版を確認します。
5. toolbox 0.4.0へ更新し、同じ検査と通常実行を再確認します。Harnessは公開版0.1.0を維持します。

操作外の直接呼出し・メソッド参照は検査しますが、注釈漏れ、import範囲外、reflection、業務順序などは保証しません。
生成例の成功と独立consumerのコミットごとのCI履歴を区別し、各コミットに紐づくActionsの実結果で判断します。

## テンプレートの更新

新旧の固定SHAをそれぞれ別の空ディレクトリへ生成し、差分を利用側の変更と照合して取り込みます。
特にpackage移動・Main指定・import範囲を維持し、上書き生成で変更を失わないようにします。
初回取り込みとartifact依存更新とは別に、異なる公開テンプレート版間の更新を検証します。

### 公開版の更新とテスト成果物

初版 `60cb1e10dcd660fd48b42bfd405e7936564fb8f0` から、
[次のマージ版](https://github.com/lambig/ab-architecture-rules/commit/0db29f424b07a274bafbeb565fe94470d25bc6b0)
`0db29f424b07a274bafbeb565fe94470d25bc6b0` へ更新しました。
新旧を別々に生成して各manifestのハッシュを照合し、生成物の変更であるworkflowとREADMEの差分を取り込みました。
取得元manifest・READMEリンクも新しいSHAへ揃え、package、Main指定、Harnessのimport範囲、Client、検証専用という目的を維持しています。
依存版はtoolbox 0.4.0とHarness 0.1.0のままです。

この更新の受け入れでは、次の3段階を別々のコミットとCI実行で確認します。

1. 更新後の許容例でCIが成功し、JUnit XMLとHTMLレポートが保存されること。
2. Clientの直接呼出し・メソッド参照を操作外へ移し、両方の対象診断でCIが失敗すること。失敗時もレポートが保存されること。
3. Clientの両方の機能を操作経由へ戻し、CIとJUnitが再び成功すること。

成果物名は `boundary-test-results-<実行SHA>-<再実行番号>` で、保存期間は14日です。
Actionsで各実行の成果物を取得し、JUnitのtests・failures・errors・skippedと対象診断を、CIの成否に照合します。
失敗したCIを成功扱いにする設定は使いません。XML生成前の失敗とキャンセルは成果物の保存対象外です。

ローカル実行とGitHub Actionsでの保存・取得は別の証拠です。公開版間の更新というだけでは最終受け入れとせず、
この更新のPRに各コミット・CI・取得結果を記録して判定します。保存期限後の追試は、同じコミットをcheckoutして
記載のJava条件で実行するか、対象コミットのCIを再実行して新しい成果物を取得してください。
