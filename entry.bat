javac -cp "lib\*" -d out DataLane.java lib\consumer\*.java
java -cp "out;lib\*" DataLane %1