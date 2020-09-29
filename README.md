# Sort-Sort-Sort

A templated sort program in java

## Java Dev Kit

[Java SE 12](https://www.oracle.com/technetwork/java/javase/downloads/index.html#JDK12)(*Without using preview feature*)

### External Linked Lib

[JavaFx 12](https://gluonhq.com/products/javafx/)(*Static link library*)

## Compile and Execute

The preview feature compile is **disabled**, for [detail](https://openjdk.java.net/projects/jdk/12/)

### Build(JDK 12 above required)

Build an local [environment](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/master/build) for running and executing, [external library](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/master/lib) are included

> Will rebuild, compile and generate bin directory to release jar file for executing with local [environment](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/master/build)

* [Use bat to build](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/build.bat)(*Windows*)
* [Use bash to build](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/build.sh)(*Mac/Linux*)

#### Link Manually

For bat command

    jlink --no-header-files --no-man-pages --strip-debug -p "%ExternalLib%;bin" --add-modules=%module% --compress=2 --output build

For bash command

    jlink --no-header-files --no-man-pages --strip-debug -p "$ExternalLib;bin" --add-modules=$module --compress=2 --output build

### Release(JDK 12 above required)

Release an executable jar file, [external library](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/master/lib) are included

> Will compile and generate bin directory to release jar file for executing with local [environment](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/master/build)

* [Use bat to release](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/release.bat)(*Windows*)
* [Use bash to release](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/release.sh)(*Mac/Linux*)

#### Compile Manually

For bat command

    javac -p %ExternalLib% --add-modules=%module% *.java -d %binDirectory%

For bash command

    javac -p $ExternalLib --add-modules=$module *.java -d $binDirectory

### Run

Run jar file with local [environment](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/master/build) only without any compiling

> Local [environment](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/tree/master/build) need ready exist

* [Use bat to run](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/run.bat)(*Windows*)
* [Use bash to run](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/run.sh)(*Mac/Linux*)

## Test Mode

Change the bool value of [isTestMode](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/src/setting/Setting.java#L68)  
> [isTestMode](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/src/setting/Setting.java#L68) is true will skip the startup animation(*experimental*)

## Change Log

See [this](https://gitlab-se.ntcu.edu.tw/ACS106129/sort-sort-sort/blob/master/CHANGELOG.md) for detail