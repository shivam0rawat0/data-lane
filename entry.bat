@echo off

:: compilation
setlocal enabledelayedexpansion
set FILES=
for /R lib %%f in (*.java) do (
    set FILES=!FILES! %%f
)
javac -cp "lib\*" -d out DataLane.java %FILES%

:: execution
java -cp "out;lib\*" ^
-DclassDir=P:/System/lib/executor/stage ^
-DrepoDir=http://192.168.29.193:8000 ^
-DdbUrl=jdbc:mysql://localhost:3306/datalane ^
-DdbUser=root ^
-DdbPass=hg65gd#hsd%%hsdghjs ^
-Dtopic=data-pipeline ^
-Xss1m DataLane