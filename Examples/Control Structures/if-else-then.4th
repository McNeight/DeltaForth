( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@dataman.ro> )

\ IF-ELSE-THEN structure

: MAIN \ Program Entry Point
  ." Enter a number:"
  QUERY INTERPRET
  ." The number you have entered is "
  DUP 0=
  IF ." zero" ELSE
     DUP 0 >
     IF ." greater than zero" ELSE ." less than zero" THEN
  THEN
  ." ." CR
;