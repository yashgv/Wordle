@echo off
cd /d%~dp0
cd ./src
javac -cp jaylib-4.2.0-1.jar -d ./classes Wordle.java 
java -cp jaylib-4.2.0-1.jar Wordle.java