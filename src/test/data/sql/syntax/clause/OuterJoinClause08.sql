PARTITION BY
    expr01
NATURAL
LEFT OUTER JOIN
    tbl01
PARTITION BY
    expr01
ON
    a = 'a'
OR  b = 'b'