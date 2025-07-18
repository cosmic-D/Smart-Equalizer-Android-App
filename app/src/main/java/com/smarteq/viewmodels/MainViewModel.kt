package com.smarteq.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smarteq.models.AudioType
import com.smarteq.models.EqualizerPreset

class MainViewModel : ViewModel() {
    
    private val _audioType = MutableLiveData<AudioType>(AudioType.UNKNOWN)
    val audioType: LiveData<AudioType> = _audioType
    
    private val _currentPreset = MutableLiveData<EqualizerPreset>(EqualizerPreset.VOICE)
    val currentPreset: LiveData<EqualizerPreset> = _currentPreset
    
    private val _isServiceRunning = MutableLiveData<Boolean>(false)
    val isServiceRunning: LiveData<Boolean> = _isServiceRunning
    
    private val _autoDetection = MutableLiveData<Boolean>(true)
    val autoDetection: LiveData<Boolean> = _autoDetection
    
    private val customPreset = FloatArray(9) { 0.0f } // 9-band equalizer
    
    fun setAudioType(type: AudioType) {
        _audioType.value = type
    }
    
    fun setCurrentPreset(preset: EqualizerPreset) {
        _currentPreset.value = preset
    }
    
    fun setServiceRunning(running: Boolean) {
        _isServiceRunning.value = running
    }
    
    fun setAutoDetection(enabled: Boolean) {
        _autoDetection.value = enabled
    }
    
    fun updateCustomPreset(band: Int, level: Float) {
        if (band in customPreset.indices) {
            customPreset[band] = level
        }
    }
    
    fun getCustomPreset(): FloatArray {
        return customPreset.clone()
    }
    
    fun shouldAutoSwitch(): Boolean {
        return _autoDetection.value == true
    }
    
    fun getPresetForAudioType(audioType: AudioType): EqualizerPreset {
        return when (audioType) {
            AudioType.VOICE -> EqualizerPreset.VOICE
            AudioType.MUSIC -> EqualizerPreset.MUSIC
            AudioType.ADVERTISEMENT -> EqualizerPreset.VOICE // Use voice preset for ads
            AudioType.UNKNOWN -> _currentPreset.value ?: EqualizerPreset.VOICE
        }
    }
} 