( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@usa.net> )

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