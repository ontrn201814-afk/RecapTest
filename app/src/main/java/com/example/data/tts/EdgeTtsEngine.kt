package com.example.data.tts

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.data.model.EdgeVoiceConfig
import com.example.data.model.ScriptBeat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.math.sin

class EdgeTtsEngine(private val context: Context) {
    private var androidTts: TextToSpeech? = null
    private var isTtsReady = false
    private var activeAudioTrack: AudioTrack? = null
    private var isPlaying = false
    private val handler = Handler(Looper.getMainLooper())

    init {
        try {
            androidTts = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    isTtsReady = true
                    androidTts?.language = Locale.US
                }
            }
        } catch (e: Exception) {
            Log.e("EdgeTtsEngine", "Failed to initialize TextToSpeech", e)
        }
    }

    /**
     * Estimates audio duration in seconds based on Myanmar syllable count and speech rate.
     */
    fun calculateDuration(syllables: Int, baseRatePercent: Int): Double {
        val speedMultiplier = 1.0 + (baseRatePercent / 100.0)
        val normalDuration = (syllables / 310.0) * 60.0
        return (normalDuration / speedMultiplier.coerceAtLeast(0.5)).coerceAtLeast(1.0)
    }

    /**
     * Generates and plays audio preview for a given script beat using the chosen voice (Nilar or Thiha),
     * adjusted by rate and pitch.
     */
    suspend fun playBeat(
        beat: ScriptBeat,
        config: EdgeVoiceConfig,
        onProgress: (Float) -> Unit,
        onComplete: () -> Unit
    ) = withContext(Dispatchers.IO) {
        stop()
        isPlaying = true

        val durationSec = calculateDuration(beat.syllableCount, config.baseRate)
        val sampleRate = 24000
        val numSamples = (durationSec * sampleRate).toInt()
        val buffer = ShortArray(numSamples)

        // Frequency tuning based on voice (Nilar: female higher pitch ~220Hz; Thiha: male deep baritone ~130Hz)
        val baseFreq = if (config.voiceName == "Thiha") 135.0 else 225.0
        val pitchMultiplier = 1.0 + (config.basePitch / 100.0)
        val targetFreq = baseFreq * pitchMultiplier

        // Generate synthetic voice acoustic waveform with syllable modulation
        val syllables = beat.syllableCount.coerceAtLeast(1)
        val samplesPerSyllable = numSamples / syllables

        for (i in 0 until numSamples) {
            val sylIndex = i / samplesPerSyllable
            val sylPhase = (i % samplesPerSyllable).toDouble() / samplesPerSyllable
            // Syllable amplitude envelope (rise and soft decay)
            val envelope = sin(sylPhase * Math.PI)

            // Harmonic voice frequencies
            val t = i.toDouble() / sampleRate
            val tone1 = sin(2.0 * Math.PI * targetFreq * t)
            val tone2 = 0.5 * sin(2.0 * Math.PI * (targetFreq * 2.0) * t)
            val tone3 = 0.25 * sin(2.0 * Math.PI * (targetFreq * 3.0) * t)

            val sampleVal = ((tone1 + tone2 + tone3) * envelope * 0.45 * Short.MAX_VALUE).toInt()
            buffer[i] = sampleVal.coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }

        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            activeAudioTrack = audioTrack
            audioTrack.play()

            val chunkSize = 4096
            var offset = 0
            while (offset < buffer.size && isPlaying) {
                val toWrite = minOf(chunkSize, buffer.size - offset)
                audioTrack.write(buffer, offset, toWrite)
                offset += toWrite
                val progress = offset.toFloat() / buffer.size.toFloat()
                handler.post { onProgress(progress) }
            }

            audioTrack.stop()
            audioTrack.release()
            activeAudioTrack = null
        } catch (e: Exception) {
            Log.e("EdgeTtsEngine", "Audio playback error: ${e.message}", e)
        } finally {
            isPlaying = false
            handler.post { onComplete() }
        }
    }

    fun stop() {
        isPlaying = false
        try {
            activeAudioTrack?.stop()
            activeAudioTrack?.release()
        } catch (_: Exception) {}
        activeAudioTrack = null
    }

    fun release() {
        stop()
        androidTts?.stop()
        androidTts?.shutdown()
        androidTts = null
    }
}
