@echo off
rem set solution dir variable
set sln=%cd%
rem set javafx lib dir
set javafxLib=%sln%\lib\javafx-12\
rem set module
set module=javafx.controls,javafx.media,javafx.swing
rem if bin dir not exist
IF NOT EXIST %sln%\bin (
    mkdir %sln%\bin
)
IF NOT EXIST %sln%\build (
    rem exit if build exist
    echo Cannot release because build no exist, need to rebuild!
) ELSE (
    rem compile java and move binary file
    cd %sln%\src && javac -p %javafxLib% --add-modules=%module% *.java -d ..\bin
    rem establish jar file
    cd %sln%\bin && jar cvfe ..\Main.jar Main *
    rem run the jar file
    cd %sln% && %sln%/build/bin/java -jar Main.jar
)
pause