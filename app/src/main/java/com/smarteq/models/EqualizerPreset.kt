package com.smarteq.models

enum class EqualizerPreset(val displayName: String) {
    VOICE("Voice"),
    MUSIC("Music"),
    CUSTOM("Custom");
    
    companion object {
        // Predefined frequency levels for 9-band equalizer (in dB)
        // Frequencies: 60Hz, 170Hz, 310Hz, 600Hz, 1kHz, 3kHz, 6kHz, 12kHz, 14kHz
        val VOICE_LEVELS = floatArrayOf(
            -2.0f,  // 60Hz - Reduce bass
            -1.0f,  // 170Hz - Slight reduction
            0.0f,   // 310Hz - Neutral
            2.0f,   // 600Hz - Boost lower mids
            4.0f,   // 1kHz - Boost mids for clarity
            6.0f,   // 3kHz - Boost upper mids
            4.0f,   // 6kHz - Moderate high boost
            2.0f,   // 12kHz - Slight high boost
            1.0f    // 14kHz - Minimal high boost
        )
        
        val MUSIC_LEVELS = floatArrayOf(
            3.0f,   // 60Hz - Boost bass
            2.0f,   // 170Hz - Boost lower bass
            1.0f,   // 310Hz - Slight boost
            0.0f,   // 600Hz - Neutral
            1.0f,   // 1kHz - Slight boost
            2.0f,   // 3kHz - Boost mids
            3.0f,   // 6kHz - Boost highs
            2.0f,   // 12kHz - Moderate high boost
            1.0f    // 14kHz - Slight high boost
        )
    }
} 