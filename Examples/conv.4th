( DELTA Forth Example File  - Copyright 1999 Valer BOCAN <vbocan@dataman.ro> )

\ Conversion from integer to ASCII

-2234 CONSTANT VALUE

: MAIN
  ." Print the number using the stack dot operator: " VALUE . CR
  ." Print the number with no sign: "
  VALUE <# #S #> TYPE CR
  ." How about a decimal dot somewhere?: "
  VALUE <# # # 46 HOLD #S SIGN #> TYPE CR
;