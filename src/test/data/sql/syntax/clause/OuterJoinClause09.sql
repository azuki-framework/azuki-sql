PARTITION BY
    expr01
  , expr02
  , expr03
NATURAL
LEFT OUTER JOIN
    tbl01
PARTITION BY
    expr01
  , expr02
  , expr03
USING
(
    column01
  , column02
  , column03
)
