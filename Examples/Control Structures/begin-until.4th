( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@dataman.ro> )

\ BEGIN-UNTIL structure

: MAIN \ Program Entry Point
  ." This program will loop until you enter 100" CR
  BEGIN
  ." To leave, enter 100..."
  QUERY INTERPRET
  100 = 
  UNTIL
;