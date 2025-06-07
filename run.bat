@echo off
cd src
echo 🔧 Compiling MemoryGame.java...
javac -encoding UTF-8 game/MemoryGame.java

if %errorlevel% neq 0 (
    echo ❌ Compilation failed.
    pause
    exit /b
)

echo ✅ Compilation successful. Launching the game...
java game.MemoryGame
pause
