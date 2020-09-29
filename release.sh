#set solution dir variable
export sln="$(PWD)"
#set module
export module=javafx.controls,javafx.media,javafx.swing
#if bin dir not exist
if [ ! -e "$sln/bin" ]; then
    mkdir "$sln/bin"
fi
if [ ! -e "$sln/build" ]; then
    #exit if build exist
    echo "Cannot release because build no exist, need to rebuild!"
else
    #compile java and move binary file
    cd "$sln/src" && javac -p ../lib/javafx-12 --add-modules=$module *.java -d ../bin
    #establish jar file
    cd "$sln/bin" && jar cvfe ../Main.jar Main *
    #give executable permission and run
    cd "$sln" && chmod 755 Main.jar && $sln/build/bin/java -jar Main.jar
fi