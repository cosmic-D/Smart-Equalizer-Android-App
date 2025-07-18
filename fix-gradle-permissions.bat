@echo off
echo Fixing Gradle permissions issue...

echo.
echo Step 1: Creating alternative Gradle cache directory...
if not exist "C:\gradle-cache" mkdir "C:\gradle-cache"

echo.
echo Step 2: Setting permissions on Gradle cache...
icacls "C:\gradle-cache" /grant "Users":(OI)(CI)F
icacls "C:\gradle-cache" /grant "Administrators":(OI)(CI)F

echo.
echo Step 3: Creating local.properties file...
echo sdk.dir=C\:\\Users\\%USERNAME%\\AppData\\Local\\Android\\Sdk > local.properties

echo.
echo Step 4: Setting GRADLE_USER_HOME environment variable...
setx GRADLE_USER_HOME "C:\gradle-cache"

echo.
echo Fix completed! Please restart Android Studio and try again.
echo.
echo If you still have issues, try running Android Studio as Administrator.
pause 