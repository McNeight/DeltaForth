( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@dataman.ro> )

123 CONSTANT GUESS \ Change this!!!
VARIABLE NOTRIES

: MAIN \ Program Entry Point
  0 NOTRIES !  
  ." Guess the number!" CR
  BEGIN
  NOTRIES C 1+ NOTRIES ! \ Increment number of tries
  ." Give me a number:"
  QUERY INTERPRET
  DUP GUESS <>
  WHILE
  DUP
  GUESS <
  IF . ."  is too small." CR ELSE . ."  is too great." CR THEN
  REPEAT
  ." You have guessed in " NOTRIES ? ."  tries." CR
;