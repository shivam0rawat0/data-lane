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
-DdbUrl=jdbc:mysql://localhost:3306/mydb ^
-DdbUser=USER ^
-DdbPass=PASS ^
-Dtopic=data-pipeline ^
DataLane