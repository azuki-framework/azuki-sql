/*
 * 複数行コメント
 */
SELECT/* コメント */
    /*+ use_hash */
    ID     -- ID
,NAME-- 名前
  , AGE    -- 年齢
FROM
    TM_PROFILE    -- プロフィールテーブル
WHERE
    ID = 1
AND NAME LIKE 'K%'
