( DELTA Forth Example File  - Copyright 1998 Valer BOCAN <vbocan@dataman.ro> )

\ Leap years

1953 CONSTANT STARTYEAR
2014 CONSTANT ENDYEAR
VARIABLE LEAPYEARS

: <=
  > 0=
;

: CHKYEAR
  STARTYEAR ENDYEAR <=
;

: LEAP
  0 LEAPYEARS !
  ENDYEAR STARTYEAR
  DO
    I 4 MOD 0=
    IF 1 LEAPYEARS +!
    THEN
  LOOP
;

: MAIN	\ Program entry point
  CHKYEAR
  IF LEAP
     ." There are " LEAPYEARS C . ."  leap years between "
     STARTYEAR . ."  and " ENDYEAR . ." ."
  ELSE
     ." Error"
  THEN
;