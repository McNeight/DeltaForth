( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@usa.net> )

\ Test for prime numbers

100 CONSTANT PRIMELIMIT

: PRIME		\ Replaces the number on the top of stack with 1 if it is prime
  2
  BEGIN
    OVER OVER MOD 0= 0=
    ROT ROT DUP >R
    OVER 2 / > 0=
    ROT AND R> SWAP
  WHILE
    1+
  REPEAT
  OVER 2 / >
;

: MAIN		\ Program entry point
  ." Prime numbers up to " PRIMELIMIT . CR
  PRIMELIMIT 1 DO
		 I PRIME
		 IF I . ."  is a prime number." CR THEN
	       LOOP
;