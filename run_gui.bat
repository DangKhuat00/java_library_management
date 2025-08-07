@echo off
echo ========================================
echo    🚀 LAUNCHING LIBRARY MANAGEMENT GUI
echo ========================================

cd src

echo ⚙️  Compiling Java files...
javac -cp ".;../lib/*" gui/*.java model/*.java dao/*.java config/*.java

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo ✅ Compilation successful!
echo 🎯 Starting GUI application...

java -cp ".;../lib/*" gui.LibraryAppGUI

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Application failed to start!
    pause
    exit /b 1
)

echo 👋 Application closed.
pause
