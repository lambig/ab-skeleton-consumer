# 独立consumerの受け入れ

公開されたSkeletonを空のプロジェクトへ取り込み、公開Libraryの利用とアクセス境界をCIで確認する例です。
本番のサービス・設定・データは含みません。

## 取得元と利用側の変更

取得元は [ab-architecture-rulesのマージ版](https://github.com/lambig/ab-architecture-rules/tree/60cb1e10dcd660fd48b42bfd405e7936564fb8f0) です。
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
異なる公開テンプレート版間の更新実証は、この初回取り込みとartifact依存更新だけでは完了しません。
