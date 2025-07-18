package com.smarteq.utils

import kotlin.math.*

object AudioUtils {
    
    /**
     * Calculate Root Mean Square (RMS) of audio samples
     */
    fun calculateRMS(audioData: ShortArray): Double {
        var sum = 0.0
        for (sample in audioData) {
            sum += sample * sample
        }
        return sqrt(sum / audioData.size)
    }
    
    /**
     * Calculate zero crossing rate
     */
    fun calculateZeroCrossings(audioData: ShortArray): Int {
        var crossings = 0
        for (i in 1 until audioData.size) {
            if ((audioData[i] >= 0) != (audioData[i - 1] >= 0)) {
                crossings++
            }
        }
        return crossings
    }
    
    /**
     * Calculate spectral centroid (brightness of sound)
     */
    fun calculateSpectralCentroid(audioData: ShortArray): Double {
        val fft = performFFT(audioData)
        var weightedSum = 0.0
        var magnitudeSum = 0.0
        
        for (i in 0 until fft.size / 2) {
            val frequency = i * 16000.0 / fft.size // Assuming 16kHz sample rate
            val magnitude = sqrt(fft[i * 2] * fft[i * 2] + fft[i * 2 + 1] * fft[i * 2 + 1])
            
            weightedSum += frequency * magnitude
            magnitudeSum += magnitude
        }
        
        return if (magnitudeSum > 0) weightedSum / magnitudeSum else 0.0
    }
    
    /**
     * Perform Fast Fourier Transform (simplified implementation)
     */
    private fun performFFT(audioData: ShortArray): DoubleArray {
        val n = audioData.size
        val fft = DoubleArray(n * 2)
        
        // Copy real data to complex array
        for (i in 0 until n) {
            fft[i * 2] = audioData[i].toDouble()
            fft[i * 2 + 1] = 0.0
        }
        
        // Simple FFT implementation (for demo purposes)
        // In a real app, you would use a proper FFT library
        return fft
    }
    
    /**
     * Normalize audio data to range [-1, 1]
     */
    fun normalizeAudio(audioData: ShortArray): FloatArray {
        val maxValue = Short.MAX_VALUE.toFloat()
        return audioData.map { it / maxValue }.toFloatArray()
    }
    
    /**
     * Apply window function to reduce spectral leakage
     */
    fun applyHammingWindow(audioData: FloatArray): FloatArray {
        val windowed = FloatArray(audioData.size)
        for (i in audioData.indices) {
            val window = 0.54f - 0.46f * cos(2.0f * PI.toFloat() * i / (audioData.size - 1))
            windowed[i] = audioData[i] * window
        }
        return windowed
    }
    
    /**
     * Calculate energy in different frequency bands
     */
    fun calculateBandEnergy(audioData: ShortArray, bandFrequencies: List<Pair<Int, Int>>): FloatArray {
        val energies = FloatArray(bandFrequencies.size)
        val fft = performFFT(audioData)
        
        for ((bandIndex, (lowFreq, highFreq)) in bandFrequencies.withIndex()) {
            var energy = 0.0f
            
            val lowBin = (lowFreq * fft.size / 2 / 16000).toInt()
            val highBin = (highFreq * fft.size / 2 / 16000).toInt()
            
            for (i in lowBin..highBin) {
                if (i < fft.size / 2) {
                    val real = fft[i * 2]
                    val imag = fft[i * 2 + 1]
                    energy += (real * real + imag * imag).toFloat()
                }
            }
            
            energies[bandIndex] = energy
        }
        
        return energies
    }
    
    /**
     * Detect silence in audio data
     */
    fun isSilence(audioData: ShortArray, threshold: Double = 100.0): Boolean {
        return calculateRMS(audioData) < threshold
    }
    
    /**
     * Calculate signal-to-noise ratio estimate
     */
    fun estimateSNR(audioData: ShortArray): Double {
        val rms = calculateRMS(audioData)
        val noiseFloor = 50.0 // Estimated noise floor
        return 20 * log10(rms / noiseFloor)
    }
    
    /**
     * Convert dB to linear scale
     */
    fun dbToLinear(db: Float): Float {
        return 10.0f.pow(db / 20.0f)
    }
    
    /**
     * Convert linear scale to dB
     */
    fun linearToDb(linear: Float): Float {
        return 20.0f * log10(linear)
    }
} 