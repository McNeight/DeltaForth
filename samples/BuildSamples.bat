@ECHO OFF
ECHO Building Delta Forth .NET Samples

..\bin\Release\DeltaForth.exe "Control Structures\Begin-Again\BeginAgain.4th" /clock
..\bin\Release\DeltaForth.exe "Control Structures\Begin-Until\BeginUntil.4th" /clock
..\bin\Release\DeltaForth.exe "Control Structures\Begin-While-Repeat\BeginWhileRepeat.4th" /clock
..\bin\Release\DeltaForth.exe "Control Structures\Case\CaseEndCase.4th" /clock
..\bin\Release\DeltaForth.exe "Control Structures\DoLoop\DoLoop.4th" /clock
..\bin\Release\DeltaForth.exe "Control Structures\IfElseThen\IfElseThen.4th" /clock
..\bin\Release\DeltaForth.exe "General\Constants\Constants.4th" /clock
..\bin\Release\DeltaForth.exe "General\Debugging\dots.4th" /clock
..\bin\Release\DeltaForth.exe "General\Euclid\Euclid.4th" /clock
..\bin\Release\DeltaForth.exe "General\GuessTheNumber\GuessTheNumber.4th" /clock
..\bin\Release\DeltaForth.exe "General\Hanoi\Hanoi.4th" /clock
..\bin\Release\DeltaForth.exe "General\HelloWorld\HelloWorld.4th" /clock
..\bin\Release\DeltaForth.exe "General\LeapYears\LeapYears.4th" /clock
..\bin\Release\DeltaForth.exe "General\Permutations\Permutations.4th" /clock
..\bin\Release\DeltaForth.exe "General\PrimeNumbers\PrimeNumbers.4th" /clock
..\bin\Release\DeltaForth.exe "General\Variables\Variables.4th" /clock
..\bin\Release\DeltaForth.exe "Library\Library1\Forth\TestLibrary1.4th" /clock
..\bin\Release\DeltaForth.exe "Library\Library2\Forth\ForthLibrary.4th" /dll /clock

ECHO Done.