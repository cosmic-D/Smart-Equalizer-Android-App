package com.smarteq.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.audiofx.Equalizer
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.smarteq.R
import com.smarteq.databinding.ActivityMainBinding
import com.smarteq.models.AudioType
import com.smarteq.models.EqualizerPreset
import com.smarteq.services.AudioProcessingService
import com.smarteq.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var equalizer: Equalizer? = null
    private val seekBars = mutableListOf<SeekBar>()
    
    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        private const val EQUALIZER_BANDS = 9
        private val FREQUENCIES = intArrayOf(60, 170, 310, 600, 1000, 3000, 6000, 12000, 14000)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        
        setupUI()
        setupObservers()
        checkPermissions()
    }
    
    private fun setupUI() {
        // Setup equalizer bands
        setupEqualizerBands()
        
        // Setup preset chips
        binding.chipVoice.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) applyPreset(EqualizerPreset.VOICE)
        }
        
        binding.chipMusic.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) applyPreset(EqualizerPreset.MUSIC)
        }
        
        binding.chipCustom.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) applyPreset(EqualizerPreset.CUSTOM)
        }
        
        // Setup auto detection switch
        binding.switchAutoDetection.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setAutoDetection(isChecked)
        }
        
        // Setup service buttons
        binding.btnStartService.setOnClickListener {
            startAudioService()
        }
        
        binding.btnStopService.setOnClickListener {
            stopAudioService()
        }
    }
    
    private fun setupObservers() {
        viewModel.audioType.observe(this) { audioType ->
            updateAudioDetectionUI(audioType)
        }
        
        viewModel.currentPreset.observe(this) { preset ->
            updatePresetUI(preset)
        }
        
        viewModel.isServiceRunning.observe(this) { isRunning ->
            updateServiceButtons(isRunning)
        }
    }
    
    private fun setupEqualizerBands() {
        val container = binding.equalizerContainer
        
        for (i in 0 until EQUALIZER_BANDS) {
            val seekBar = SeekBar(this).apply {
                max = 1500 // -15dB to +15dB
                progress = 750 // 0dB (center)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    marginEnd = 8
                }
                
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            val band = i
                            val level = (progress - 750) / 100f // Convert to dB
                            equalizer?.setBandLevel(band.toInt().toShort(), (level * 100).toInt().toShort())
                            viewModel.updateCustomPreset(band, level)
                        }
                    }
                    
                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                })
            }
            
            seekBars.add(seekBar)
            container.addView(seekBar)
        }
    }
    
    private fun applyPreset(preset: EqualizerPreset) {
        viewModel.setCurrentPreset(preset)
        
        val levels = when (preset) {
            EqualizerPreset.VOICE -> EqualizerPreset.VOICE_LEVELS
            EqualizerPreset.MUSIC -> EqualizerPreset.MUSIC_LEVELS
            EqualizerPreset.CUSTOM -> viewModel.getCustomPreset()
        }
        
        for (i in levels.indices) {
            val level = levels[i]
            val progress = ((level + 15) * 50).toInt() // Convert dB to progress
            seekBars[i].progress = progress.coerceIn(0, 1500)
            equalizer?.setBandLevel(i.toInt().toShort(), (level * 100).toInt().toShort())
        }
        
        Snackbar.make(binding.root, "Preset applied: ${preset.name}", Snackbar.LENGTH_SHORT).show()
    }
    
    private fun updateAudioDetectionUI(audioType: AudioType) {
        val textView = binding.tvAudioDetection
        val (text, color) = when (audioType) {
            AudioType.VOICE -> getString(R.string.detection_voice) to getColor(R.color.detection_voice)
            AudioType.MUSIC -> getString(R.string.detection_music) to getColor(R.color.detection_music)
            AudioType.ADVERTISEMENT -> getString(R.string.detection_advertisement) to getColor(R.color.detection_advertisement)
            AudioType.UNKNOWN -> getString(R.string.detection_unknown) to getColor(R.color.detection_unknown)
        }
        
        textView.text = text
        textView.setTextColor(color)
    }
    
    private fun updatePresetUI(preset: EqualizerPreset) {
        when (preset) {
            EqualizerPreset.VOICE -> binding.chipVoice.isChecked = true
            EqualizerPreset.MUSIC -> binding.chipMusic.isChecked = true
            EqualizerPreset.CUSTOM -> binding.chipCustom.isChecked = true
        }
    }
    
    private fun updateServiceButtons(isRunning: Boolean) {
        binding.btnStartService.isEnabled = !isRunning
        binding.btnStopService.isEnabled = isRunning
    }
    
    private fun startAudioService() {
        if (checkPermissions()) {
            val intent = Intent(this, AudioProcessingService::class.java)
            startForegroundService(intent)
            viewModel.setServiceRunning(true)
            Snackbar.make(binding.root, getString(R.string.success_service_started), Snackbar.LENGTH_SHORT).show()
        }
    }
    
    private fun stopAudioService() {
        val intent = Intent(this, AudioProcessingService::class.java)
        stopService(intent)
        viewModel.setServiceRunning(false)
        Snackbar.make(binding.root, getString(R.string.success_service_stopped), Snackbar.LENGTH_SHORT).show()
    }
    
    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) 
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.root, "Audio permission granted", Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(binding.root, getString(R.string.error_audio_permission), Snackbar.LENGTH_LONG).show()
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        setupEqualizer()
    }
    
    override fun onPause() {
        super.onPause()
        equalizer?.release()
        equalizer = null
    }
    
    private fun setupEqualizer() {
        try {
            equalizer = Equalizer(0, 0)
            equalizer?.enabled = true
            
            // Set frequency bands
            for (i in 0 until EQUALIZER_BANDS) {
                equalizer?.setCenterFreq(i.toInt().toShort(), FREQUENCIES[i].toInt().toShort())
            }
            
        } catch (e: Exception) {
            Snackbar.make(binding.root, "Failed to setup equalizer: ${e.message}", Snackbar.LENGTH_LONG).show()
        }
    }
} 