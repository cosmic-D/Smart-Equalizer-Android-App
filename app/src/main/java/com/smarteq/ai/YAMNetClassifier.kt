package com.smarteq.ai

import android.content.Context
import android.media.AudioRecord
import android.media.MediaRecorder
import com.smarteq.models.AudioLabel
import com.smarteq.models.AudioType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import kotlin.math.sqrt

class YAMNetClassifier(private val context: Context) {
    
    private var interpreter: Interpreter? = null
    private val labels = mutableListOf<String>()
    private var isInitialized = false
    
    companion object {
        private const val MODEL_FILE = "yamnet_model.tflite"
        private const val LABELS_FILE = "yamnet_labels.txt"
        private const val SAMPLE_RATE = 16000
        private const val AUDIO_LENGTH = 0.96f // 960ms audio segment
        private const val AUDIO_SAMPLES = (SAMPLE_RATE * AUDIO_LENGTH).toInt()
        private const val CONFIDENCE_THRESHOLD = 0.3f
    }
    
    init {
        initializeModel()
    }
    
    private fun initializeModel() {
        try {
            // Load model file
            val modelFile = File(context.getExternalFilesDir(null), MODEL_FILE)
            if (!modelFile.exists()) {
                // For demo purposes, we'll create a mock model
                // In a real app, you would include the actual YAMNet model file
                createMockModel()
            }
            
            // Load labels
            loadLabels()
            
            // Initialize TensorFlow Lite interpreter
            val options = Interpreter.Options()
            interpreter = Interpreter(modelFile, options)
            
            isInitialized = true
            
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to mock classification
            isInitialized = false
        }
    }
    
    private fun createMockModel() {
        // This is a placeholder for the actual YAMNet model
        // In a real implementation, you would include the YAMNet TFLite model
        // For demo purposes, we'll use mock classification
    }
    
    private fun loadLabels() {
        try {
            // Load YAMNet labels from assets
            val labelsFile = File(context.getExternalFilesDir(null), LABELS_FILE)
            if (labelsFile.exists()) {
                labels.addAll(labelsFile.readLines())
            } else {
                // Fallback to basic labels
                labels.addAll(listOf(
                    "Speech", "Music", "Silence", "Noise",
                    "Conversation", "Singing", "Instrumental music"
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback to basic labels
            labels.addAll(listOf(
                "Speech", "Music", "Silence", "Noise",
                "Conversation", "Singing", "Instrumental music"
            ))
        }
    }
    
    fun classifyAudio(audioData: ShortArray): AudioType {
        if (!isInitialized) {
            return mockClassification(audioData)
        }
        
        try {
            val outputScores = runInference(audioData)
            val topLabels = getTopLabels(outputScores)
            return classifyFromLabels(topLabels)
            
        } catch (e: Exception) {
            e.printStackTrace()
            return mockClassification(audioData)
        }
    }
    
    private fun runInference(audioData: ShortArray): FloatArray {
        // Prepare input data
        val inputBuffer = ByteBuffer.allocateDirect(audioData.size * 2)
        inputBuffer.order(ByteOrder.nativeOrder())
        
        for (sample in audioData) {
            inputBuffer.putShort(sample)
        }
        
        // Prepare output buffer
        val outputBuffer = ByteBuffer.allocateDirect(labels.size * 4)
        outputBuffer.order(ByteOrder.nativeOrder())
        
        // Run inference
        interpreter?.run(inputBuffer, outputBuffer)
        
        // Convert output to float array
        val outputScores = FloatArray(labels.size)
        outputBuffer.rewind()
        for (i in outputScores.indices) {
            outputScores[i] = outputBuffer.float
        }
        
        return outputScores
    }
    
    private fun getTopLabels(outputScores: FloatArray, topK: Int = 5): List<AudioLabel> {
        val indexedScores = outputScores.mapIndexed { index, score ->
            AudioLabel(labels.getOrElse(index) { "Unknown" }, score)
        }
        
        return indexedScores
            .filter { it.score > CONFIDENCE_THRESHOLD }
            .sortedByDescending { it.score }
            .take(topK)
    }
    
    private fun classifyFromLabels(topLabels: List<AudioLabel>): AudioType {
        var musicScore = 0.0f
        var voiceScore = 0.0f
        var advertisementScore = 0.0f
        
        for (label in topLabels) {
            when {
                AudioLabel.MUSIC_LABELS.contains(label.name) -> {
                    musicScore += label.score
                }
                AudioLabel.VOICE_LABELS.contains(label.name) -> {
                    voiceScore += label.score
                }
                AudioLabel.ADVERTISEMENT_LABELS.contains(label.name) -> {
                    advertisementScore += label.score
                }
            }
        }
        
        return when {
            advertisementScore > musicScore && advertisementScore > voiceScore -> AudioType.ADVERTISEMENT
            musicScore > voiceScore -> AudioType.MUSIC
            voiceScore > musicScore -> AudioType.VOICE
            else -> AudioType.UNKNOWN
        }
    }
    
    private fun mockClassification(audioData: ShortArray): AudioType {
        // Simple mock classification based on audio characteristics
        val rms = calculateRMS(audioData)
        val zeroCrossings = calculateZeroCrossings(audioData)
        
        return when {
            rms < 100 -> AudioType.UNKNOWN // Very quiet
            zeroCrossings > 1000 -> AudioType.MUSIC // High frequency content
            rms > 500 -> AudioType.VOICE // Loud speech-like content
            else -> AudioType.UNKNOWN
        }
    }
    
    private fun calculateRMS(audioData: ShortArray): Double {
        var sum = 0.0
        for (sample in audioData) {
            sum += sample * sample
        }
        return sqrt(sum / audioData.size)
    }
    
    private fun calculateZeroCrossings(audioData: ShortArray): Int {
        var crossings = 0
        for (i in 1 until audioData.size) {
            if ((audioData[i] >= 0) != (audioData[i - 1] >= 0)) {
                crossings++
            }
        }
        return crossings
    }
    
    fun release() {
        interpreter?.close()
        interpreter = null
    }
} 