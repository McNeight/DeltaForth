( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@usa.net> )

\ BEGIN-WHILE-REPEAT structure

: MAIN \ Program Entry Point
  ." This program will loop until you enter 100" CR
  BEGIN
  ." To leave, enter 100..."
  QUERY INTERPRET
  100 <>
  WHILE
  ." You still don't want to leave..." CR
  REPEAT
  ." You wanted to leave..." CR
;