(2-cell DELTA Forth stack operations)
(Copyright 1998 Valer BOCAN <vbocan@dataman.ro>)

: 2DUP
   OVER OVER
;

: 3DUP
   DUP >R ROT
   DUP >R ROT
   DUP >R ROT
   R> SWAP
   R>
;

: 2SWAP
   ROT R> ROT R>
;

: 2OVER
   >R >R 2DUP
   R> R> 2SWAP
;

: 2DROP
   DROP DROP
;

: 2ROT
   >R >R 2SWAP
   R> R> 2SWAP
;

: D2DUP
  2OVER 2OVER
;