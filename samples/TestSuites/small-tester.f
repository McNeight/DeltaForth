\ http://qualdan.com/forth/small-tester.f

\ A simple test utility.

\ Syntax: T{ test code => correct results }T

\ Gives better failure messages than the Hayes tester.

\ Requires only positive numbers (0, 1, and 32) and these 25 words:
    \ DUP DROP SWAP DEPTH
    \ + - <
    \ CELLS ALLOT @ !
    \ IF THEN BEGIN UNTIL
    \ : ; CREATE
    \ SOURCE >IN \
    \ S" TYPE . CR


\ Tests don't display anything when they succeed, so we provide a word
\ which skips and displays the source line to give an optional progress
\ indicator.

CREATE verbose 1 CELLS ALLOT  0 verbose !

: TESTING \ "..." --
    SOURCE >IN ! DROP
    verbose @ IF SOURCE TYPE CR THEN ;


\ Results are stored as a cell array, with the first cell holding the
\ number of values.

CREATE results 1 CELLS ALLOT
: #results results @ @ ;
: result CELLS results @ + ;

: .results  \ --
    DUP results ! @
    DUP 0 < IF . S" results." TYPE 0 THEN
    IF
        S" ( " TYPE 1 BEGIN
            DUP result @ .
        1 + DUP #results SWAP < UNTIL DROP  S" )" TYPE
    THEN ;

CREATE depth0 1 CELLS ALLOT
: tdepth DEPTH depth0 @ - ;
: fix-underflow BEGIN  0  depth0 @ DEPTH < UNTIL ;

: save-results  \ i*x addr --
    results !  tdepth results @ !
    tdepth 0 < IF fix-underflow THEN
    tdepth IF BEGIN  tdepth result !  tdepth 1 < UNTIL THEN ;

\ We store two sets of results: the ones we actually received, and the
\ ones we were expecting.

CREATE received 32 1 + CELLS ALLOT
CREATE expected 32 1 + CELLS ALLOT

\ When an error occurs, we show the most current line of source code and
\ the actual and expected stack results.

: test-failed \ --
    S" Test Failed: " TYPE SOURCE TYPE CR
    S"   Received: " TYPE received .results CR
    S"   Expected: " TYPE expected .results CR ;

\ Given an offset, check the received value against the expected value.
\ Return a -1 offset on failure.

: check-result \ n -- n'
    DUP CELLS  DUP received + @  SWAP expected + @
    - IF DROP 0 1 - THEN ;

\ Loop over all values (including the stack depths), making sure they
\ match the expected results.

CREATE should-fail 1 CELLS ALLOT  0 should-fail !
: FAIL 1 should-fail ! ;

: check-results \ --
    expected @ 1 + \ including depths
    BEGIN 1 -  check-result  DUP 1 < UNTIL
    should-fail @ + IF test-failed THEN  0 should-fail ! ;

\ Finally we define the actual test syntax.

: T{ DEPTH depth0 ! ;
: => received save-results ;
: }T expected save-results  check-results ;

\ Make sure it works:
1 2
    T{ 3 4 => 3 4 }T
T{ DEPTH => 2 }T
    FAIL T{ 5 6 => 5 7 }T
    FAIL T{ 7 8 => 9 8 }T
    FAIL T{ 10 11 => 10 11 12 }T
    FAIL T{ 13 14 15 => 13 14 }T
    FAIL T{ DROP => }T
    FAIL T{ => DROP }T
T{ DUP => 0 }T DROP
T{ DUP => 1 }T DROP
T{ DEPTH => 0 }T
