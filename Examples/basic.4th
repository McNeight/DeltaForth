( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@dataman.ro> )

\ The Basics Of DELTA Forth

10 CONSTANT TEN
20 CONSTANT TWENTY
VARIABLE RESULT

: MAIN	\ Program entry point
  TEN TWENTY +
  RESULT !
  TEN . ." +" TWENTY . ." =" RESULT ?
  CR
;