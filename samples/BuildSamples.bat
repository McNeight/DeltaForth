@ECHO OFF
ECHO Building DeltaForth Samples

SET CONFIGURATION=%1
IF "%1"=="" (
    SET CONFIGURATION=Debug
)

SET FRAMEWORK=%2
IF "%2"=="" (
    SET FRAMEWORK=net461
)

call :treeProcess ..\artifacts\bin\%CONFIGURATION%\%FRAMEWORK%\DeltaForth.exe
goto :eof

:treeProcess
for %%f in (*.deltaforth *.4th *.fth *.fr *.fs) do (
    REM Compile a DeltaForth file
    REM %~dpnx1 %%f /map /clock
    %~dpnx1 %%f /nologo /quiet
)
for /D %%d in (*) do (
    cd %%d
    call :treeProcess %~dpnx1
    cd ..
)
exit /b

ECHO Done.
