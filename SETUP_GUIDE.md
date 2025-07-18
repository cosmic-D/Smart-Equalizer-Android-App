# Smart Equalizer - Setup Guide

This guide will help you set up the Smart Equalizer Android project on your system.

## 🚀 Quick Setup

### Step 1: Install Prerequisites

#### **Java Development Kit (JDK)**
1. Download and install **JDK 17** or later from:
   - [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
   - [OpenJDK](https://adoptium.net/) (Recommended)

2. **Set JAVA_HOME environment variable:**
   
   **Windows:**
   ```cmd
   setx JAVA_HOME "C:\Program Files\Java\jdk-17"
   setx PATH "%PATH%;%JAVA_HOME%\bin"
   ```
   
   **macOS/Linux:**
   ```bash
   export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
   export PATH=$PATH:$JAVA_HOME/bin
   ```

3. **Verify Java installation:**
   ```bash
   java -version
   javac -version
   ```

#### **Android Studio**
1. Download and install [Android Studio](https://developer.android.com/studio)
2. Install Android SDK (API 21+ and API 34)
3. Set ANDROID_HOME environment variable

### Step 2: Project Setup

#### **Option A: Using Android Studio (Recommended)**
1. Open Android Studio
2. Select "Open an existing Android Studio project"
3. Navigate to the project folder and select it
4. Wait for Gradle sync to complete
5. Click the "Run" button (green play icon)

#### **Option B: Using Command Line**

1. **Download Gradle Wrapper (if not already done):**
   
   **Windows:**
   ```cmd
   download-gradle-wrapper.bat
   ```
   
   **macOS/Linux:**
   ```bash
   ./download-gradle-wrapper.sh
   ```

2. **Build the project:**
   ```bash
   # Debug build
   ./gradlew assembleDebug
   
   # Or build and install on connected device
   ./gradlew installDebug
   ```

### Step 3: Run the App

#### **On Physical Device:**
1. Enable Developer Options on your Android device
2. Enable USB Debugging
3. Connect device via USB
4. Run the app from Android Studio or command line

#### **On Emulator:**
1. Create an Android Virtual Device (AVD) in Android Studio
2. Start the emulator
3. Run the app

## 🔧 Troubleshooting

### **Common Issues**

#### **1. "JAVA_HOME is not set"**
```bash
# Check if Java is installed
java -version

# Set JAVA_HOME (Windows)
setx JAVA_HOME "C:\Program Files\Java\jdk-17"

# Set JAVA_HOME (macOS/Linux)
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
```

#### **2. "Gradle wrapper not found"**
```bash
# Download the wrapper
./download-gradle-wrapper.sh

# Make executable
chmod +x gradlew
```

#### **3. "Android SDK not found"**
1. Open Android Studio
2. Go to File → Settings → Appearance & Behavior → System Settings → Android SDK
3. Note the SDK location
4. Set ANDROID_HOME environment variable:
   ```bash
   export ANDROID_HOME=/path/to/your/android/sdk
   ```

#### **4. "Permission denied" errors**
```bash
# Make scripts executable
chmod +x gradlew
chmod +x download-gradle-wrapper.sh
```

#### **5. "Build failed" errors**
```bash
# Clean and rebuild
./gradlew clean
./gradlew build

# Check for specific errors in the output
```

### **Environment Variables Checklist**

Make sure these are set correctly:

**Windows:**
```cmd
JAVA_HOME=C:\Program Files\Java\jdk-17
ANDROID_HOME=C:\Users\YourName\AppData\Local\Android\Sdk
PATH=%PATH%;%JAVA_HOME%\bin;%ANDROID_HOME%\platform-tools
```

**macOS/Linux:**
```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$JAVA_HOME/bin:$ANDROID_HOME/platform-tools
```

## 📱 Testing the App

### **First Launch:**
1. Grant microphone permission when prompted
2. Select a preset (Voice, Music, or Custom)
3. Enable auto detection
4. Start the audio processing service

### **Testing Features:**
- **Equalizer**: Adjust the 9 frequency bands
- **Presets**: Switch between Voice, Music, and Custom
- **Auto Detection**: Play different audio types to see detection
- **Background Processing**: Minimize app and check notification

## 🛠️ Development Commands

### **Build Commands:**
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug

# Run tests
./gradlew test

# Clean project
./gradlew clean
```

### **Debug Commands:**
```bash
# View app logs
adb logcat | grep "SmartEqualizer"

# View specific component logs
adb logcat -s "AudioProcessing"
adb logcat -s "YAMNetClassifier"
```

## 📋 System Requirements

- **Operating System**: Windows 10+, macOS 10.14+, or Linux
- **Java**: JDK 17 or later
- **Android Studio**: Arctic Fox (2020.3.1) or later
- **Android SDK**: API 21+ (Android 5.0)
- **Target SDK**: API 34 (Android 14)
- **RAM**: 8GB minimum, 16GB recommended
- **Storage**: 10GB free space

## 🆘 Getting Help

If you encounter issues:

1. **Check the logs** using `adb logcat`
2. **Verify environment variables** are set correctly
3. **Try cleaning and rebuilding** the project
4. **Check Android Studio's Event Log** for specific errors
5. **Ensure all prerequisites** are installed and configured

## ✅ Success Checklist

- [ ] Java JDK 17+ installed and JAVA_HOME set
- [ ] Android Studio installed with SDK
- [ ] Gradle wrapper downloaded and executable
- [ ] Project builds successfully (`./gradlew build`)
- [ ] App installs and runs on device/emulator
- [ ] Microphone permission granted
- [ ] Equalizer controls work
- [ ] Audio detection shows status
- [ ] Background service starts and shows notification

---

**Note**: This is a demonstration project. For production use, integrate the actual YAMNet TensorFlow Lite model and add comprehensive testing. 