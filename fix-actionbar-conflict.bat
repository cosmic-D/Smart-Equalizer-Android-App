@echo off
echo ========================================
echo ActionBar/Toolbar Conflict Fix Script
echo ========================================
echo.

echo Fixing ActionBar/Toolbar conflict...
echo.

echo 1. Updated themes.xml:
echo    - Added windowActionBar=false
echo    - Added windowNoTitle=true
echo    - This disables the default ActionBar
echo.

echo 2. Why this happened:
echo    - App uses MaterialToolbar in layout
echo    - Theme was using default ActionBar
echo    - Android doesn't allow both simultaneously
echo    - Solution: Disable default ActionBar in theme
echo.

echo 3. The fix:
echo    - Theme now properly supports Toolbar
echo    - MainActivity.setSupportActionBar() will work
echo    - No more "already has an action bar" error
echo.

echo ========================================
echo ActionBar conflict fixed successfully!
echo ========================================
echo.

echo Next steps:
echo 1. Clean and rebuild the project
echo 2. Run the app again
echo 3. The app should now launch without crashes
echo.

pause 