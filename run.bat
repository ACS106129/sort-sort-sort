@echo off
rem set solution dir variable
set sln=%cd%
IF NOT EXIST %sln%\build (
    echo Cannot run because build no exist, need to rebuild!
    pause
) ELSE (
    rem run the jar file
    cd %sln% && %sln%/build/bin/javaw -jar Main.jar
)