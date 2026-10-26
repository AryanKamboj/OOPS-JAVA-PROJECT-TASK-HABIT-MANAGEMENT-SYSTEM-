@echo off
setlocal

echo ===================================================
echo   Task ^& Habit Management System — Build ^& Run
echo   CSC2104 OOP Project — Manipal University Jaipur
echo ===================================================
echo.

:: Change to the directory containing this script
cd /d "%~dp0"

:: Check that Java is available
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not on PATH.
    echo         Please install JDK and add it to your PATH.
    pause
    exit /b 1
)

:: Compile all source files
echo [1/2] Compiling source files...
javac -d . src\Task.java src\Habit.java src\TaskManager.java src\HabitManager.java src\DataStorage.java src\Main.java
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed. Fix the errors shown above and try again.
    pause
    exit /b 1
)
echo       Compilation successful.
echo.

:: Run the application
echo [2/2] Starting application...
echo.
java -cp . Main

echo.
echo Application exited.
pause
