( DELTA Forth Example File  - Copyright 1999 Valer BOCAN <vbocan@usa.net> )

\ Demonstrates the use of string conversion primitives

-2234 CONSTANT CONST		\ The value we operate with

: MAIN
  ." Full conversion: "
  CONST <# #S SIGN #> TYPE CR

  ." Full conversion (no sign): "
  CONST <# #S #> TYPE CR

  ." Conversion and division by 100: "
  CONST <# # # 46 HOLD #S SIGN #> TYPE CR	\ Division by 100 is not actually performed
						\ but a dot is placed in the output string
  ." Last two digits: "
  CONST <# # # #> TYPE CR
;