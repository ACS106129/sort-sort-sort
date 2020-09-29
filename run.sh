#set solution dir variable
export sln="$(PWD)"
#if bin dir not exist
if [ ! -e "$sln/build" ]; then
    echo "Cannot run because build no exist, need to rebuild!"
else
    #give executable permission and run
    chmod 755 Main.jar
    #run the jar file
    "$sln"/build/bin/javaw -jar Main.jar
fi