#set solution dir variable
export sln="$(PWD)"
#set module
export module=javafx.controls,javafx.media,javafx.swing
#if bin dir not exist
if [ ! -e "$sln/bin" ]; then
    mkdir "$sln/bin"
fi
#compile java and move binary file
cd "$sln/src" && javac -p ../lib/javafx-12 --add-modules=$module *.java -d ../bin
if [ -e "$sln/build" ]; then
    rm -rf "$sln/build"
fi
#link module if mods exist
cd "$sln" && jlink --no-header-files --no-man-pages --strip-debug -p "lib/javafx-12;bin" --add-modules=$module --compress=2 --output build
#establish jar file
cd "$sln/bin" && jar cvfe ../Main.jar Main *
#give executable permission and run
cd "$sln" && chmod 755 Main.jar && $sln/build/bin/java -jar Main.jar
