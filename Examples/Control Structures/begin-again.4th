( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@dataman.ro> )

\ BEGIN-AGAIN structure
\ WARNING! Running this program will cause the interpreter to loop indefinitely
\ To stop the execution, press CTRL+C at any time

: MAIN \ Program Entry Point
  BEGIN
  ." Infinite loop" CR
  AGAIN
;