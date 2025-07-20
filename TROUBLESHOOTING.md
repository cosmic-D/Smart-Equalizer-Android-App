# Troubleshooting Guide

This guide addresses common issues when setting up and running the Smart Equalizer Android project.

## 🚨 Common Errors & Solutions

### **1. Gradle Distribution Download Error**

**Error Message:**
```
Could not install Gradle distribution from 'https://services.gradle.org/distributions/gradle-8.1-bin.zip'.
Reason: java.lang.RuntimeException: Could not create parent directory for lock file
```

**Solutions:**

#### **Option A: Run Android Studio as Administrator**
1. Close Android Studio completely
2. Right-click on Android Studio icon
3. Select "Run as administrator"
4. Open the project again

#### **Option B: Change Gradle Home Directory**
1. Open Android Studio
2. Go to **File → Settings** (or **Android Studio → Preferences** on macOS)
3. Navigate to **Build, Execution, Deployment → Gradle**
4. Change **Gradle user home** to a directory you have write access to:
   ```
   C:\gradle-cache
   ```
5. Click **Apply** and **OK**
6. Restart Android Studio

#### **Option C: Fix Permissions**
1. Open Command Prompt as Administrator
2. Run these commands:
   ```cmd
   icacls "C:\Users\Administrator\.gradle" /grant "Users":(OI)(CI)F
   icacls "C:\Users\Administrator\.gradle" /grant "Administrators":(OI)(CI)F
   ```

#### **Option D: Clear Gradle Cache**
1. Close Android Studio
2. Delete the Gradle cache directory:
   ```cmd
   rmdir /s /q "C:\Users\Administrator\.gradle"
   ```
3. Restart Android Studio

### **2. JAVA_HOME Not Set**

**Error Message:**
```
JAVA_HOME is not set and no 'java' command could be found in your PATH
```

**Solution:**
1. Download and install JDK 17 from [Eclipse Temurin](https://adoptium.net/)
2. Set JAVA_HOME environment variable:
   ```cmd
   setx JAVA_HOME "C:\Program Files\Eclipse Adoptium\jdk-17.0.9.9-hotspot"
   setx PATH "%PATH%;%JAVA_HOME%\bin"
   ```
3. Restart Command Prompt and verify:
   ```cmd
   java -version
   echo %JAVA_HOME%
   ```

### **3. Android SDK Not Found**

**Error Message:**
```
SDK location not found. Define location with sdk.dir in the local.properties file
```

**Solution:**
1. Open Android Studio
2. Go to **File → Settings → Appearance & Behavior → System Settings → Android SDK**
3. Note the **Android SDK Location**
4. Create `local.properties` file in project root:
   ```properties
   sdk.dir=C\:\\Users\\Administrator\\AppData\\Local\\Android\\Sdk
   ```

### **4. Permission Denied Errors**

**Error Message:**
```
Permission denied: ./gradlew
```

**Solution:**
```bash
# Make gradlew executable
chmod +x gradlew

# Or on Windows, ensure the file is not blocked
# Right-click gradlew.bat → Properties → Unblock
```

### **5. Build Failed - Missing Dependencies**

**Error Message:**
```
Could not resolve all dependencies for configuration ':app:debugCompileClasspath'
```

**Solution:**
1. Check internet connection
2. Sync project with Gradle files:
   - **File → Sync Project with Gradle Files**
3. Clean and rebuild:
   ```bash
   ./gradlew clean
   ./gradlew build
   ```

### **6. Device Not Recognized**

**Error Message:**
```
No target device found
```

**Solution:**
1. Enable Developer Options on your Android device:
   - Go to **Settings → About Phone**
   - Tap **Build Number** 7 times
2. Enable USB Debugging:
   - Go to **Settings → Developer Options**
   - Enable **USB Debugging**
3. Install USB drivers for your device
4. Connect device via USB
5. Allow USB debugging when prompted on device

### **7. Missing App Icons**

**Error Message:**
```
AAPT: error: resource mipmap/ic_launcher not found
```

**Solution:**
1. **Quick Fix**: Run the provided fix script:
   ```cmd
   fix-app-icons.bat
   ```

2. **Manual Fix**: Create icon directories and files:
   ```cmd
   mkdir app\src\main\res\mipmap-hdpi
   mkdir app\src\main\res\mipmap-mdpi
   mkdir app\src\main\res\mipmap-xhdpi
   mkdir app\src\main\res\mipmap-xxhdpi
   mkdir app\src\main\res\mipmap-xxxhdpi
   mkdir app\src\main\res\drawable
   ```

3. **Use Android Studio Icon Generator**:
   - Right-click on `app/src/main/res`
   - Select **New → Image Asset**
   - Choose **Launcher Icons (Adaptive and Legacy)**
   - Customize and generate icons

4. **Clean and Rebuild**:
   - **Build → Clean Project**
   - **Build → Rebuild Project**
   - **File → Invalidate Caches and Restart**

### **8. Adaptive Icon API Level Error**

**Error Message:**
```
<adaptive-icon> elements require a sdk version of at least 26
```

**Solution:**
1. **Quick Fix**: Run the provided fix script:
   ```cmd
   update-icons.bat
   ```

2. **Manual Fix**: Replace adaptive icons with traditional vector icons:
   - Replace `<adaptive-icon>` with `<vector>` in all icon files
   - Use traditional vector drawable format
   - Ensure compatibility with API 21+

3. **Alternative**: Increase minimum SDK to 26:
   ```gradle
   defaultConfig {
       minSdk 26  // Instead of 21
   }
   ```

4. **Use Android Studio Icon Generator**:
   - Right-click on `app/src/main/res`
   - Select **New → Image Asset**
   - Choose **Launcher Icons (Legacy Only)**
   - This generates compatible icons for older Android versions

### **9. Kotlin Type Conversion Errors**

**Error Message:**
```
Using 'toShort(): Short' is an error. Unclear conversion. To achieve the same result convert to Int explicitly and then to Short.
```

**Solution:**
1. **Quick Fix**: Run the provided fix script:
   ```cmd
   fix-kotlin-conversions.bat
   ```

2. **Manual Fix**: Replace direct conversions with explicit conversions:
   ```kotlin
   // ❌ Wrong - Unclear conversion
   value.toShort()
   
   // ✅ Correct - Explicit conversion
   value.toInt().toShort()
   ```

3. **Common Conversion Patterns to Fix**:
   - `i.toShort()` → `i.toInt().toShort()`
   - `(level * 100).toShort()` → `(level * 100).toInt().toShort()`
   - `FREQUENCIES[i].toShort()` → `FREQUENCIES[i].toInt().toShort()`

4. **Best Practices**:
   - Always use explicit conversions for numeric types
   - Check for null before casting: `value?.let { it.toInt() }`
   - Use safe casting: `value as? Type`
   - Avoid force unwrapping: `value!!`

5. **Files Fixed**:
   - `MainActivity.kt`: Lines 115, 143, 238
   - `AudioProcessingService.kt`: Line 188

### **10. Compilation Errors**

**Common Error Messages:**
```
Unresolved reference: setCenterFreq
'name' hides member of supertype 'Enum' and needs 'override' modifier
Const 'val' initializer should be a constant value
Unresolved reference: CHANNEL_IN_MONO
Expecting a name / Expecting ')' / Expecting 'in'
```

**Solutions:**

1. **Unresolved reference: setCenterFreq**
   ```kotlin
   // ❌ Wrong - Method doesn't exist
   equalizer?.setCenterFreq(band, freq)
   
   // ✅ Correct - Use setBandLevel instead
   equalizer?.setBandLevel(band, level)
   ```

2. **Enum name conflict**
   ```kotlin
   // ❌ Wrong - Conflicts with enum's built-in name
   enum class MyEnum(val name: String)
   
   // ✅ Correct - Use different property name
   enum class MyEnum(val displayName: String)
   ```

3. **Const val initializer error**
   ```kotlin
   // ❌ Wrong - Runtime calculation can't be const
   private const val BUFFER_SIZE = AudioRecord.getMinBufferSize(...)
   
   // ✅ Correct - Use val for runtime calculations
   private val BUFFER_SIZE = AudioRecord.getMinBufferSize(...)
   ```

4. **Destructuring assignment syntax**
   ```kotlin
   // ❌ Wrong - Complex destructuring in for loop
   for ((bandIndex, (lowFreq, highFreq)) in list.withIndex())
   
   // ✅ Correct - Simple iteration with destructuring
   for (i in list.indices) {
       val (lowFreq, highFreq) = list[i]
   }
   ```

5. **Quick Fix**: Run the provided fix script:
   ```cmd
   fix-compilation-errors.bat
   ```

### **11. AudioRecord Constants Error**

**Error Message:**
```
Unresolved reference: CHANNEL_IN_MONO
Unresolved reference: ENCODING_PCM_16BIT
```

**Solution:**
1. **Add missing import**:
   ```kotlin
   import android.media.AudioFormat
   ```

2. **Fix constant references**:
   ```kotlin
   // ❌ Wrong - Old API
   AudioRecord.CHANNEL_IN_MONO
   AudioRecord.ENCODING_PCM_16BIT
   
   // ✅ Correct - New API
   AudioFormat.CHANNEL_IN_MONO
   AudioFormat.ENCODING_PCM_16BIT
   ```

3. **Why this happens**:
   - AudioRecord constants were moved to AudioFormat class in newer Android APIs
   - AudioFormat provides the proper constants for audio configuration
   - This is the correct Android API usage

4. **Quick Fix**: Run the provided fix script:
   ```cmd
   fix-audio-constants.bat
   ```

5. **Files typically affected**:
   - `AudioProcessingService.kt`
   - Any file using AudioRecord configuration

### **12. ActionBar/Toolbar Conflict Error**

**Error Message:**
```
This Activity already has an action bar supplied by the window decor. 
Do not request Window.FEATURE_SUPPORT_ACTION_BAR and set windowActionBar to false 
in your theme to use a Toolbar instead.
```

**Solution:**
1. **Update theme configuration** in `themes.xml`:
   ```xml
   <style name="Theme.SmartEqualizer" parent="Theme.Material3.DayNight">
       <!-- Disable default ActionBar to use Toolbar instead -->
       <item name="windowActionBar">false</item>
       <item name="windowNoTitle">true</item>
       
       <!-- ... rest of theme attributes ... -->
   </style>
   ```

2. **Why this happens**:
   - App uses `MaterialToolbar` in layout
   - Theme was using default ActionBar
   - Android doesn't allow both simultaneously
   - `setSupportActionBar()` conflicts with default ActionBar

3. **Quick Fix**: Run the provided fix script:
   ```cmd
   fix-actionbar-conflict.bat
   ```

4. **Files typically affected**:
   - `app/src/main/res/values/themes.xml`
   - `app/src/main/res/values-night/themes.xml` (if exists)
   - Any Activity using `setSupportActionBar()`

## 🔧 Advanced Troubleshooting

### **Reset Android Studio Settings**
1. Close Android Studio
2. Delete configuration directory:
   ```cmd
   rmdir /s /q "%APPDATA%\Google\AndroidStudio4.1"
   ```
3. Restart Android Studio

### **Use Different Gradle Version**
If Gradle 8.1 causes issues, try a different version:

1. Edit `gradle/wrapper/gradle-wrapper.properties`:
   ```properties
   distributionUrl=https\://services.gradle.org/distributions/gradle-7.6.1-bin.zip
   ```

2. Sync project with Gradle files

### **Offline Mode**
If you have network issues:

1. Go to **File → Settings → Build, Execution, Deployment → Gradle**
2. Check **Offline work**
3. Click **Apply**

### **Increase Memory Allocation**
If you get out of memory errors:

1. Go to **Help → Edit Custom VM Options**
2. Add or modify these lines:
   ```
   -Xmx2048m
   -Xms512m
   ```
3. Restart Android Studio

## 📋 Environment Setup Checklist

### **Windows Setup**
- [ ] Install JDK 17 and set JAVA_HOME
- [ ] Install Android Studio
- [ ] Install Android SDK (API 21+ and API 34)
- [ ] Set ANDROID_HOME environment variable
- [ ] Add platform-tools to PATH
- [ ] Run Android Studio as Administrator (if needed)

### **macOS Setup**
- [ ] Install JDK 17 using Homebrew: `brew install openjdk@17`
- [ ] Set JAVA_HOME: `export JAVA_HOME=/opt/homebrew/opt/openjdk@17`
- [ ] Install Android Studio
- [ ] Install Android SDK
- [ ] Set ANDROID_HOME: `export ANDROID_HOME=$HOME/Library/Android/sdk`

### **Linux Setup**
- [ ] Install JDK 17: `sudo apt install openjdk-17-jdk`
- [ ] Set JAVA_HOME: `export JAVA_HOME=/usr/lib/jvm/java-17-openjdk`
- [ ] Install Android Studio
- [ ] Install Android SDK
- [ ] Set ANDROID_HOME: `export ANDROID_HOME=$HOME/Android/Sdk`

## 🆘 Getting Help

### **Check Logs**
1. **Android Studio Logs:**
   - Go to **Help → Show Log in Explorer**
   - Check `idea.log` for errors

2. **Gradle Logs:**
   ```bash
   ./gradlew build --info
   ./gradlew build --debug
   ```

3. **Device Logs:**
   ```bash
   adb logcat
   ```

### **Common Solutions Summary**

| Issue | Quick Fix |
|-------|-----------|
| Gradle download fails | Run as Administrator or change Gradle home |
| JAVA_HOME not set | Install JDK 17 and set environment variable |
| SDK not found | Create local.properties with sdk.dir |
| Permission denied | Make gradlew executable |
| Build fails | Clean and rebuild project |
| Device not found | Enable USB debugging |

### **Still Having Issues?**

1. **Check system requirements:**
   - Windows 10+ / macOS 10.14+ / Linux
   - 8GB RAM minimum, 16GB recommended
   - 10GB free disk space

2. **Verify installations:**
   ```bash
   java -version
   javac -version
   echo $JAVA_HOME
   echo $ANDROID_HOME
   ```

3. **Try alternative approaches:**
   - Use Android Studio's built-in Gradle sync
   - Use a different JDK version
   - Use a different Gradle version

---

**Note**: Most issues are related to environment setup. Once properly configured, the project should build and run without problems. 