(
SELECT
    *
FROM
    tbl0
    INNER JOIN
        tbl1
    ON
        tbl0.id   = tbl1.id
    AND tbl0.name = tbl1.name
    JOIN
        tbl3
    USING
    (
        column1
    )
    JOIN
        tbl4
    USING
    (
        column1
      , column2
      , column3
    )
    CROSS JOIN
        tbl5
    NATURAL JOIN
        tbl6
    NATURAL INNER JOIN
        tbl7
WHERE
    a = 0
AND b = '1'
UNION
SELECT /*+ hint */
    query_name.*
  , schema1 . table1 . *
  , schema2 . view2  . *
  , schema3 . materialized_view4 . *
  , expr5
  , expr6 c_alias6
  , expr7 AS c_alias7
FROM
    tbl
  , (
        select /*+ hint2 */
            *
        from
            tbl2
    )
WHERE
    a=0
AND b=0
AND c=0
AND (
        d = 0
    OR  e = 0
    )
)
ORDER SIBLINGS BY
    column1
  , column2 ASC
  , column3 NULLS FIRST
  , column4 DESC NULLS LAST
FOR UPDATE OF 
    schema1 . table1 . column1
  , table2.column2
  , column3
  WAIT 10
;