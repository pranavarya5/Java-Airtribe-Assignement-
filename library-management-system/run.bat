@echo off
setlocal enabledelayedexpansion

echo ==========================================
echo  Building Library Management System...
echo ==========================================

if exist bin (
    rd /s /q bin
)
mkdir bin

echo Compiling Java source files...
dir /s /b src\*.java > sources.txt
javac -d bin -sourcepath src @sources.txt
del sources.txt

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo.
    echo Running Main Application...
    echo ------------------------------------------
    java -cp bin com.library.Main
) else (
    echo Compilation failed!
)
endlocal
