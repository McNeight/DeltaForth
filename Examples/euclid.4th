( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@usa.net> )

\ Greatest common divider (Euclid's algorhythm)

LOAD 2STACK.4TH	\ Include file for double-wide stack operations

100 CONSTANT CONST1
220 CONSTANT CONST2

: 0<=		\ First, define a helping word
  DUP 0=
  SWAP 0<
  OR
;

: GCD		(n1 n2 - - -)
  OVER 0<=
  IF		(n1 is <= 0)
    2DROP
  ELSE
    DUP 0<=
    IF		(n2 is <= 0)
      2DROP
    ELSE
      BEGIN
      2DUP =
      IF	(We have found the GCD!)
        .
      ELSE
        2DUP >
        IF
          SWAP
        THEN
        OVER - 0
      THEN
      UNTIL
    THEN
  THEN
;

: MAIN		\ Program entry point
  CONST1 CONST2
  ." Greatest Common Divider is "
  GCD
  CR
;