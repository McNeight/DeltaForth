( --------------------------------------------------------------------
  File:		Test.4th

  Summary:	Shows how to call an external word

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

\Declare and external word in TextLib.dll, based on class TextLib, method text1
extern display1 TextLib.dll TextLib.text1

\Declare and external word in TextLib.dll, based on class TextLib, method text2
extern display2 TextLib.dll TextLib.text2

: main
  display1
  display2
;
