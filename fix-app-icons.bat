@echo off
echo Fixing Smart Equalizer app icon issue...
echo.

echo Step 1: Checking if icon directories exist...
if not exist "app\src\main\res\mipmap-hdpi" (
    echo Creating mipmap directories...
    mkdir "app\src\main\res\mipmap-hdpi"
    mkdir "app\src\main\res\mipmap-mdpi"
    mkdir "app\src\main\res\mipmap-xhdpi"
    mkdir "app\src\main\res\mipmap-xxhdpi"
    mkdir "app\src\main\res\mipmap-xxxhdpi"
    mkdir "app\src\main\res\drawable"
) else (
    echo Icon directories already exist.
)

echo.
echo Step 2: Icon files have been created with:
echo - Adaptive icons for all density levels
echo - Equalizer-themed foreground design
echo - Purple background matching app theme

echo.
echo Step 3: Next steps:
echo 1. Clean and rebuild the project in Android Studio
echo 2. If you still see errors, try:
echo    - Build → Clean Project
echo    - Build → Rebuild Project
echo    - File → Invalidate Caches and Restart

echo.
echo Step 4: Alternative solution - Use Android Studio's icon generator:
echo 1. Right-click on app/src/main/res
echo 2. Select "New → Image Asset"
echo 3. Choose "Launcher Icons (Adaptive and Legacy)"
echo 4. Customize the icon design
echo 5. Click "Next" and "Finish"

echo.
echo Fix completed! Try building the project again.
pause 