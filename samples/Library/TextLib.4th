( --------------------------------------------------------------------
  File:		TextLib.4th

  Summary:	Example library to be called from an external program
		Should be compiled with the /DLL option

  Origin:	Valer BOCAN <vbocan@dataman.ro>

  Date:		November 10, 2001

  --------------------------------------------------------------------
  This file is part of the Delta Forth Code Samples.

  Copyright (C)2001 Valer BOCAN  All rights reserved.

  THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
  KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
  PARTICULAR PURPOSE.
  -------------------------------------------------------------------- )

library TextLib

: main
  ."We're in word MAIN in TextLib" cr
;

: text1
  ."We're in TEXT1" cr
;

: text2
  ."We're in TEXT2" cr
;