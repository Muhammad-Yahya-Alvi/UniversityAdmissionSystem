@echo off
javac -cp .;mysql-connector-j-8.4.0.jar *.java
if %errorlevel% neq 0 (
    echo Compilation failed!
    pause
    exit /b %errorlevel%
)
java -cp .;mysql-connector-j-8.4.0.jar LoginFrame
pause
