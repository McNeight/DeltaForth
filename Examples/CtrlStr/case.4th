( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@usa.net> )

\ CASE-ENDCASE structure

: MAIN \ Program Entry Point
  ." Enter 1, 2 or 3:"
  QUERY INTERPRET
  ." The number you have entered is "
  CASE
    1 OF ." one" ENDOF
    2 OF ." two" ENDOF
    3 OF ." three" ENDOF
    ." unknown to me"
  ENDCASE
  ." ." CR
;