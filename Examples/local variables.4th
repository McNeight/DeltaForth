( DELTA Forth Example File  - Copyright 1999 Valer BOCAN <vbocan@dataman.ro> )

\ Demonstrates the use of local variables

VARIABLE VAR		\ Declare global variable

: CHANGEVAR
  LOCAL VAR		\ Declare a local variable
  100 VAR !		\ Store 100 into local variable
  ." Local variable value: "
  VAR ? CR
;

: MAIN			\ Program entry point
  50 VAR !		\ Store 50 into global variable
  ." Global variable value: "
  VAR ? CR

  CHANGEVAR		\ Call word

  ." Global variable value: "
  VAR ? CR		\ Note that the local variable takes precedence over
			\ the global variable with the same name
;