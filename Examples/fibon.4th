( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@usa.net> )

\ Fibonacci

VARIABLE FIBLIMIT

: FIBONACCI
  0 1 0 . SPACE 1 . SPACE
  FIBLIMIT C 0 DO
         DUP ROT +
         DUP . SPACE
       LOOP
  DROP DROP
;

: MAIN \ Program entry point
  20 FIBLIMIT !
  FIBONACCI
;