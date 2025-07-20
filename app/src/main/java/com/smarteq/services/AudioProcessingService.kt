package com.smarteq.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.Equalizer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.smarteq.R
import com.smarteq.ai.YAMNetClassifier
import com.smarteq.models.AudioType
import com.smarteq.models.EqualizerPreset
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean

class AudioProcessingService : Service() {
    
    private val binder = LocalBinder()
    private var audioRecord: AudioRecord? = null
    private var equalizer: Equalizer? = null
    private var classifier: YAMNetClassifier? = null
    private var processingJob: Job? = null
    private val isProcessing = AtomicBoolean(false)
    
    private var currentAudioType = AudioType.UNKNOWN
    private var currentPreset = EqualizerPreset.VOICE
    private var autoDetection = true
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "smart_equalizer_service"
        private const val SAMPLE_RATE = 16000
        private val BUFFER_SIZE = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )
        private const val AUDIO_SEGMENT_SIZE = 15360 // 960ms at 16kHz
    }
    
    inner class LocalBinder : Binder() {
        fun getService(): AudioProcessingService = this@AudioProcessingService
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initializeAudioProcessing()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())
        startAudioProcessing()
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
    
    override fun onDestroy() {
        stopAudioProcessing()
        releaseResources()
        super.onDestroy()
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.service_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Smart Equalizer background service"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.service_notification_title))
            .setContentText(getString(R.string.service_notification_text))
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }
    
    private fun initializeAudioProcessing() {
        try {
            // Initialize audio recorder
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                BUFFER_SIZE
            )
            
            // Initialize equalizer
            equalizer = Equalizer(0, 0)
            equalizer?.enabled = true
            
            // Initialize classifier
            classifier = YAMNetClassifier(this)
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    private fun startAudioProcessing() {
        if (isProcessing.get()) return
        
        isProcessing.set(true)
        processingJob = CoroutineScope(Dispatchers.IO).launch {
            processAudioLoop()
        }
    }
    
    private fun stopAudioProcessing() {
        isProcessing.set(false)
        processingJob?.cancel()
        processingJob = null
    }
    
    private suspend fun processAudioLoop() {
        val audioBuffer = ShortArray(AUDIO_SEGMENT_SIZE)
        
        audioRecord?.startRecording()
        
        while (isProcessing.get()) {
            try {
                val bytesRead = audioRecord?.read(audioBuffer, 0, audioBuffer.size) ?: 0
                
                if (bytesRead > 0) {
                    val audioType = classifier?.classifyAudio(audioBuffer) ?: AudioType.UNKNOWN
                    
                    if (audioType != currentAudioType) {
                        currentAudioType = audioType
                        if (autoDetection) {
                            val newPreset = getPresetForAudioType(audioType)
                            if (newPreset != currentPreset) {
                                currentPreset = newPreset
                                applyPreset(newPreset)
                            }
                        }
                    }
                }
                
                delay(1000) // Process every second
                
            } catch (e: Exception) {
                e.printStackTrace()
                delay(1000) // Wait before retrying
            }
        }
        
        audioRecord?.stop()
    }
    
    private fun getPresetForAudioType(audioType: AudioType): EqualizerPreset {
        return when (audioType) {
            AudioType.VOICE -> EqualizerPreset.VOICE
            AudioType.MUSIC -> EqualizerPreset.MUSIC
            AudioType.ADVERTISEMENT -> EqualizerPreset.VOICE
            AudioType.UNKNOWN -> currentPreset
        }
    }
    
    private fun applyPreset(preset: EqualizerPreset) {
        val levels = when (preset) {
            EqualizerPreset.VOICE -> EqualizerPreset.VOICE_LEVELS
            EqualizerPreset.MUSIC -> EqualizerPreset.MUSIC_LEVELS
            EqualizerPreset.CUSTOM -> FloatArray(9) { 0.0f } // Custom preset would be loaded from settings
        }
        
        for (i in levels.indices) {
            equalizer?.setBandLevel(i.toInt().toShort(), (levels[i] * 100).toInt().toShort())
        }
    }
    
    private fun releaseResources() {
        audioRecord?.release()
        audioRecord = null
        
        equalizer?.release()
        equalizer = null
        
        classifier?.release()
        classifier = null
    }
    
    // Public methods for activity communication
    fun setAutoDetection(enabled: Boolean) {
        autoDetection = enabled
    }
    
    fun setPreset(preset: EqualizerPreset) {
        currentPreset = preset
        applyPreset(preset)
    }
    
    fun getCurrentAudioType(): AudioType {
        return currentAudioType
    }
    
    fun getCurrentPreset(): EqualizerPreset {
        return currentPreset
    }
} 