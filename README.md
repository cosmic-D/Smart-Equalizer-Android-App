# Smart Equalizer Android App

A free Android app inspired by FxSound, featuring a 9-band equalizer with automatic preset switching powered by artificial intelligence. The app improves clarity, volume, and audio balance while running in the background, processing system audio.

## Features

### 🎵 9-Band Equalizer
- Professional-grade 9-band equalizer with frequencies: 60Hz, 170Hz, 310Hz, 600Hz, 1kHz, 3kHz, 6kHz, 12kHz, 14kHz
- Real-time audio processing and enhancement
- Smooth visual feedback with Material Design interface

### 🤖 AI-Powered Audio Classification
- **TensorFlow Lite + YAMNet** integration for real-time audio analysis
- Automatic detection of:
  - **Voice/Speech** - Optimized for conversations, calls, podcasts
  - **Music** - Enhanced for songs, instrumental music, singing
  - **Advertisements** - Special handling for commercial content
  - **Unknown** - Fallback for unclear audio content

### 🎛️ Smart Preset System
- **Voice Preset**: Boosts mid frequencies (1kHz-3kHz) for speech clarity
- **Music Preset**: Balanced enhancement across all frequency bands
- **Custom Preset**: User-defined equalizer settings
- **Auto Detection**: Automatically switches presets based on audio content

### 🔄 Background Processing
- Foreground service for continuous audio processing
- Minimal battery impact with optimized processing
- Seamless integration with system audio

### 🎨 Modern UI/UX
- Material Design 3 interface
- Intuitive equalizer visualization
- Real-time audio detection status
- Smooth animations and transitions

## Technical Architecture

### Core Components
- **MainActivity**: Main UI and user interactions
- **AudioProcessingService**: Background audio processing service
- **YAMNetClassifier**: TensorFlow Lite audio classification
- **MainViewModel**: MVVM architecture for state management
- **AudioUtils**: Audio processing utilities

### AI Integration
```kotlin
// Example audio classification logic
val topLabels = getTopLabelsFromYamnet(outputScores)
var musicScore = 0.0f
var voiceScore = 0.0f

for (label in topLabels) {
    when (label.name) {
        in listOf("Music", "Instrumental music", "Singing") -> musicScore += label.score
        in listOf("Speech", "Conversation", "Narration, monologue") -> voiceScore += label.score
    }
}

val result = when {
    musicScore > voiceScore -> "MUSIC"
    voiceScore > musicScore -> "VOICE"
    else -> "UNKNOWN"
}
```

## Build Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 21+ (Android 5.0)
- Target SDK API 34 (Android 14)
- Kotlin 1.9.0+

### Setup
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd Smart-Equalizer
   ```

2. Open the project in Android Studio

3. Sync Gradle dependencies:
   ```bash
   ./gradlew build
   ```

4. Build the APK:
   ```bash
   # Debug build
   ./gradlew assembleDebug
   
   # Release build
   ./gradlew assembleRelease
   ```

### Dependencies
- **TensorFlow Lite**: 2.14.0 - AI model inference
- **Material Design**: 1.11.0 - Modern UI components
- **AndroidX Lifecycle**: 2.7.0 - MVVM architecture
- **Kotlin Coroutines**: 1.7.3 - Asynchronous processing
- **Android Audio Effects**: Built-in - Equalizer functionality

## Usage

### First Launch
1. Grant microphone permission when prompted
2. Select your preferred preset (Voice, Music, or Custom)
3. Enable auto detection for automatic preset switching
4. Start the audio processing service

### Manual Control
- Use the equalizer sliders to adjust frequency bands manually
- Switch between presets using the chip buttons
- Toggle auto detection on/off
- Start/stop background processing

### Background Operation
- The app runs as a foreground service
- Audio classification happens every second
- Presets automatically switch based on detected content
- Service continues running when app is minimized

## Project Structure

```
app/
├── src/main/
│   ├── java/com/smarteq/
│   │   ├── activities/
│   │   │   └── MainActivity.kt
│   │   ├── services/
│   │   │   └── AudioProcessingService.kt
│   │   ├── models/
│   │   │   ├── AudioType.kt
│   │   │   ├── AudioLabel.kt
│   │   │   └── EqualizerPreset.kt
│   │   ├── viewmodels/
│   │   │   └── MainViewModel.kt
│   │   ├── ai/
│   │   │   └── YAMNetClassifier.kt
│   │   └── utils/
│   │       └── AudioUtils.kt
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_main.xml
│   │   ├── values/
│   │   │   ├── strings.xml
│   │   │   ├── colors.xml
│   │   │   └── themes.xml
│   │   └── xml/
│   │       ├── backup_rules.xml
│   │       └── data_extraction_rules.xml
│   └── AndroidManifest.xml
├── build.gradle
└── proguard-rules.pro
```

## Performance Considerations

### Optimization Features
- Efficient TensorFlow Lite model inference
- Optimized audio processing pipeline
- Minimal memory footprint
- Battery usage optimization
- Background processing efficiency

### Memory Management
- Proper resource cleanup in services
- Efficient audio buffer handling
- Optimized FFT calculations
- Smart caching of classification results

## Future Enhancements

### Planned Features
- **Windows/macOS Desktop Versions**: Cross-platform expansion
- **Cloud Processing**: Server-side audio analysis
- **Advanced Presets**: User-defined custom presets
- **Social Features**: Preset sharing and community
- **Enhanced AI**: More accurate audio classification
- **Audio Effects**: Reverb, bass boost, spatial audio

### Technical Improvements
- **Real YAMNet Model**: Integration with actual YAMNet TFLite model
- **Advanced FFT**: Professional FFT library integration
- **Audio Session Management**: Better system audio integration
- **Performance Profiling**: Detailed performance monitoring
- **Unit Testing**: Comprehensive test coverage

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- **FxSound**: Inspiration for the equalizer concept
- **YAMNet**: Audio classification model
- **TensorFlow Lite**: AI inference framework
- **Material Design**: UI/UX guidelines
- **Android Audio Effects**: System audio processing

## Support

For issues, questions, or contributions, please open an issue on the project repository.

---

**Note**: This is a demonstration project showcasing AI-powered audio processing capabilities. The YAMNet model integration is simplified for demo purposes. In a production environment, you would integrate the actual YAMNet TensorFlow Lite model. 