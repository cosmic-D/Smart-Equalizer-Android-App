# Smart Equalizer Android App

A free Android app inspired by FxSound, featuring a 9-band equalizer with automatic preset switching powered by artificial intelligence.

## 🛠️ Tech Stack

### **Core Technologies**
- **Language**: Kotlin 1.9.0
- **Platform**: Android (API 21+ / Android 5.0+)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel)

### **AI & Machine Learning**
- **TensorFlow Lite**: 2.14.0 - AI model inference
- **YAMNet Model**: Audio classification for voice/music detection
- **Audio Processing**: Real-time audio analysis and classification

### **UI/UX Framework**
- **Material Design**: 1.11.0 - Modern UI components
- **AndroidX**: Core Android libraries
- **ConstraintLayout**: 2.1.4 - Flexible layouts
- **ViewBinding**: Type-safe view access

### **Background Processing**
- **Kotlin Coroutines**: 1.7.3 - Asynchronous operations
- **Foreground Service**: Continuous audio processing
- **Android Audio Effects**: Built-in equalizer functionality

### **State Management**
- **LiveData**: Reactive data streams
- **ViewModel**: UI state management
- **Lifecycle Components**: 2.7.0 - Lifecycle-aware components

### **Build Tools**
- **Gradle**: 8.1.0 - Build automation
- **Android Gradle Plugin**: 8.1.0
- **ProGuard**: Code obfuscation and optimization

## 🚀 Quick Start

### **Prerequisites**
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK API 21+ (Android 5.0)
- Target SDK API 34 (Android 14)
- Kotlin 1.9.0+
- Java JDK 17+
- Git

### **Installation**

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Smart-Equalizer
   ```

2. **Setup Gradle Wrapper** (if not already done)
   ```bash
   # Linux/macOS
   ./download-gradle-wrapper.sh
   
   # Windows
   download-gradle-wrapper.bat
   ```

3. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the project folder and select it

4. **Sync Gradle dependencies**
   ```bash
   ./gradlew build
   ```
   Or use Android Studio: File → Sync Project with Gradle Files

5. **Build and Run**
   ```bash
   # Debug build
   ./gradlew assembleDebug
   
   # Install on connected device
   ./gradlew installDebug
   ```

### **Running the App**

1. **Connect Android Device**
   - Enable Developer Options on your Android device
   - Enable USB Debugging
   - Connect device via USB

2. **Install and Run**
   ```bash
   # Install debug APK
   ./gradlew installDebug
   
   # Or build and install
   ./gradlew assembleDebug installDebug
   ```

3. **Alternative: Use Android Studio**
   - Click the "Run" button (green play icon)
   - Select your device/emulator
   - App will install and launch automatically

## 📱 App Features

### **Core Functionality**
- **9-Band Equalizer**: 60Hz, 170Hz, 310Hz, 600Hz, 1kHz, 3kHz, 6kHz, 12kHz, 14kHz
- **AI Audio Classification**: Real-time detection of Voice/Music/Advertisement
- **Smart Presets**: Voice, Music, and Custom presets
- **Auto Detection**: Automatic preset switching based on audio content
- **Background Processing**: Continuous audio analysis service

### **User Interface**
- Material Design 3 interface
- Real-time equalizer visualization
- Audio detection status display
- Preset selection with chip buttons
- Service control buttons

## 🔧 Development Setup

### **Project Structure**
```
app/
├── src/main/
│   ├── java/com/smarteq/
│   │   ├── activities/          # UI Activities
│   │   ├── services/           # Background Services
│   │   ├── models/             # Data Models
│   │   ├── viewmodels/         # MVVM ViewModels
│   │   ├── ai/                 # AI/ML Components
│   │   └── utils/              # Utility Classes
│   ├── res/                    # Resources
│   └── AndroidManifest.xml     # App Configuration
├── build.gradle                # App Dependencies
└── proguard-rules.pro          # Code Obfuscation
```

### **Key Dependencies**
```gradle
// Core Android
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

// Material Design
implementation 'com.google.android.material:material:1.11.0'

// Lifecycle & MVVM
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'

// Coroutines
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

// TensorFlow Lite
implementation 'org.tensorflow:tensorflow-lite:2.14.0'
implementation 'org.tensorflow:tensorflow-lite-support:0.4.4'
```

## 🧪 Testing

### **Unit Tests**
```bash
# Run unit tests
./gradlew test

# Run with coverage
./gradlew testDebugUnitTestCoverage
```

### **Instrumented Tests**
```bash
# Run on connected device
./gradlew connectedAndroidTest
```

## 📦 Building for Production

### **Release Build**
```bash
# Generate signed APK
./gradlew assembleRelease

# Generate signed AAB (for Play Store)
./gradlew bundleRelease
```

## 🔍 Debugging

### **Logs**
```bash
# View app logs
adb logcat | grep "SmartEqualizer"

# View specific tag
adb logcat -s "AudioProcessing"
```

### **Common Issues**

1. **Permission Denied**
   - Ensure microphone permission is granted
   - Check AndroidManifest.xml permissions

2. **TensorFlow Lite Model Not Found**
   - The app includes fallback classification
   - For production, add actual YAMNet model file

3. **Audio Service Not Starting**
   - Check foreground service permissions
   - Verify audio session is available

## 📚 API Reference

### **Main Components**

#### **MainActivity**
```kotlin
class MainActivity : AppCompatActivity {
    // Main UI and user interactions
    // Equalizer controls and preset management
}
```

#### **AudioProcessingService**
```kotlin
class AudioProcessingService : Service {
    // Background audio processing
    // AI classification and preset switching
}
```

#### **YAMNetClassifier**
```kotlin
class YAMNetClassifier(private val context: Context) {
    // TensorFlow Lite audio classification
    // Real-time audio analysis
}
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

- **Issues**: [GitHub Issues](https://github.com/your-repo/issues)
- **Documentation**: [Wiki](https://github.com/your-repo/wiki)
- **Discussions**: [GitHub Discussions](https://github.com/your-repo/discussions)

## 📖 Additional Documentation

- **[SETUP_GUIDE.md](SETUP_GUIDE.md)** - Detailed setup instructions
- **[PROJECT_BUILD_FLOW.md](PROJECT_BUILD_FLOW.md)** - Development tracking
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - Project overview

---

**Note**: This is a demonstration project. For production use, integrate the actual YAMNet TensorFlow Lite model and add comprehensive testing. 