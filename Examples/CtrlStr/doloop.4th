( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@usa.net> )

\ DO-LOOP structure

: MAIN \ Program Entry Point
  ." Count limit:"
  QUERY INTERPRET
  0
  DO
  I . SPACE
  LOOP CR
;