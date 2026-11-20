package com.example.data.model

import androidx.annotation.DrawableRes
import com.example.R
import java.util.UUID

enum class RecapFlow(
    val id: String,
    val titleMy: String,
    val titleEn: String,
    val subtitle: String,
    val description: String,
    @DrawableRes val iconRes: Int,
    val promptRole: String
) {
    REAL_RECAP(
        id = "real_recap",
        titleMy = "Real Recap Flow",
        titleEn = "Story Recap",
        subtitle = "ဇာတ်လမ်း အပြည့်အစုံ",
        description = "SRT dialogue မှ ဇာတ်ကောင် အမည်များနှင့် အချိန်များကို အတိအကျ ထုတ်ယူပြီး သတ်မှတ်မိနစ်အတိုင်း ဆွဲဆောင်မှုရှိသော မြန်မာ စကားပြောဟန်ဖြင့် ပြန်လည်ရေးသားခြင်း",
        iconRes = R.drawable.flow_real,
        promptRole = "Professional Burmese movie recap narrator. You must preserve all character names and scene timings accurately from the provided SRT transcript."
    ),
    DIALOGUE_DUB(
        id = "dialogue_dub",
        titleMy = "စကားပြော Dub",
        titleEn = "Character Dub",
        subtitle = "ဇာတ်ကောင် အသံထွက်",
        description = "ဗီဒီယိုပါ ဇာတ်ကောင်များ အချင်းချင်း တိုက်ရိုက် စကားပြောဆိုသည့် အသံထွက်ဟန်ဖြင့် သဘာဝကျကျ အသံသွင်း Dubbing ပြုလုပ်ခြင်း",
        iconRes = R.drawable.flow_dub,
        promptRole = "Burmese dialogue dubbing director. Convert dialogue lines into natural spoken conversational Burmese (စကားပြောဟန်) matching the characters' emotions and scene timing."
    ),
    HINDI_CARTOON(
        id = "hindi_cartoon",
        titleMy = "Hindi Cartoon",
        titleEn = "Cartoon Myanmar",
        subtitle = "ကာတွန်း မြန်မာသံ",
        description = "ဟာသ၊ သရော်စာနှင့် အသံဆန်းကြယ်စွာ ဖန်တီးထားသော ပရိသတ်အကြိုက် မြန်မာ ကာတွန်း အသံထွက် စတိုင်",
        iconRes = R.drawable.flow_cartoon,
        promptRole = "Energetic Burmese cartoon dubber. Make the script funny, lively, comedic, and entertaining with slang and playful cartoon expressions."
    ),
    STORY_RECAP(
        id = "story_recap",
        titleMy = "Story Core",
        titleEn = "Short Story",
        subtitle = "အကျဉ်းချုပ် ဇာတ်လမ်း",
        description = "အဓိက ဇာတ်ကွက် အချက်အလက်များကို အဓိကထား၍ စိတ်လှုပ်ရှားဖွယ် အကျဉ်းချုပ် ရေးသားခြင်း",
        iconRes = R.drawable.flow_story,
        promptRole = "Fast-paced cinematic storyteller focusing on plot twists and exciting cliffhangers."
    ),
    AUDIO_BOOK(
        id = "audio_book",
        titleMy = "Audio Book",
        titleEn = "Audio Book",
        subtitle = "စာအုပ် ဖတ်ကြားခြင်း",
        description = "စာအုပ် အခန်းလိုက် စာသားများကို သာယာကြည်လင်သော မြန်မာ အသံထွက်ဖြင့် ဖတ်ပြခြင်း",
        iconRes = R.drawable.flow_book,
        promptRole = "Calm, articulate Burmese audiobook narrator reading chapter by chapter clearly."
    )
}

data class SrtSegment(
    val id: String = UUID.randomUUID().toString(),
    val index: Int,
    val startTimeMs: Long,
    val endTimeMs: Long,
    val character: String,
    val text: String,
    val originalText: String = text
) {
    val durationSec: Double get() = (endTimeMs - startTimeMs).coerceAtLeast(100) / 1000.0

    fun formatStartTime(): String = formatMs(startTimeMs)
    fun formatEndTime(): String = formatMs(endTimeMs)

    companion object {
        fun formatMs(ms: Long): String {
            val totalSec = ms / 1000
            val h = totalSec / 3600
            val m = (totalSec % 3600) / 60
            val s = totalSec % 60
            val millis = ms % 1000
            return String.format("%02d:%02d:%02d,%03d", h, m, s, millis)
        }

        fun formatDotTime(ms: Long): String {
            val totalSec = ms / 1000
            val h = totalSec / 3600
            val m = (totalSec % 3600) / 60
            val s = totalSec % 60
            return String.format("%02d.%02d.%02d", h, m, s)
        }
    }
}

data class ScriptBeat(
    val id: String = UUID.randomUUID().toString(),
    val beatNumber: Int,
    val timeRange: String, // e.g. "N00.01.12-00.01.24"
    val startSec: Double,
    val endSec: Double,
    val character: String,
    val narration: String,
    val syllableCount: Int = countMyanmarSyllables(narration),
    val audioDurationSec: Double = (countMyanmarSyllables(narration) / 310.0 * 60.0).coerceAtLeast(1.5),
    val voiceName: String = "my-MM-NilarNeural"
)

data class EdgeVoiceConfig(
    val voiceName: String = "Nilar", // "Nilar" or "Thiha"
    val voiceCode: String = if (voiceName == "Thiha") "my-MM-ThihaNeural" else "my-MM-NilarNeural",
    val baseRate: Int = 10,          // -50% to +50%
    val basePitch: Int = 0,          // -50Hz to +50Hz
    val maxConcurrent: Int = 3       // 1 to 5 threads
)

data class AntiCopyrightConfig(
    val flipHorizontal: Boolean = true,
    val zoomFactor: Float = 1.08f,
    val speedMultiplier: Float = 1.04f,
    val canvasRatio: String = "9:16", // "9:16", "16:9", "1:1", "4:5"
    val blurTopBottom: Boolean = true,
    val blurCornerLogo: Boolean = true,
    val colorFilter: String = "Vivid", // "Original", "Vivid", "Warm", "Cool", "Cinematic", "Drama"
    val addSubBarOverlay: Boolean = true
)

data class RecapProject(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val flow: RecapFlow = RecapFlow.REAL_RECAP,
    val targetMinutes: Int = 10,
    val videoFileName: String = "sample_movie_clip.mp4",
    val videoUri: String? = null,
    val videoDurationSec: Double = 900.0,
    val characterNames: List<String> = emptyList(),
    val srtList: List<SrtSegment> = emptyList(),
    val scriptBeats: List<ScriptBeat> = emptyList(),
    val voiceConfig: EdgeVoiceConfig = EdgeVoiceConfig(),
    val antiCopyright: AntiCopyrightConfig = AntiCopyrightConfig(),
    val status: ProjectStatus = ProjectStatus.DRAFT,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ProjectStatus(val labelMy: String, val colorHex: Long) {
    DRAFT("မူကြမ်း", 0xFF7D927F),
    SRT_EXTRACTED("SRT ရရှိပြီး", 0xFF64B5F6),
    SCRIPT_GENERATED("Script ထွက်ရှိပြီး", 0xFFE9B949),
    AUDIO_GENERATED("အသံသွင်းပြီး", 0xFF8FDCA0),
    EXPORTED("ပြီးပြည့်စုံ", 0xFF4CAF50)
}

/**
 * Counts syllables for Myanmar text to calculate TTS duration and speaking pacing.
 * Based on 310 syllables/minute in natural spoken Myanmar Unicode.
 */
fun countMyanmarSyllables(text: String): Int {
    if (text.isEmpty()) return 0
    var count = 0
    for (i in text.indices) {
        val c = text[i].code
        // Myanmar consonant bases or digits starting a syllable
        val isBase = (c in 0x1000..0x1021) || (c in 0x1023..0x102A) || c == 0x103F || (c in 0x1040..0x1049)
        if (!isBase) continue
        val next = if (i + 1 < text.length) text[i + 1].code else 0
        val next2 = if (i + 2 < text.length) text[i + 2].code else 0
        if (next == 0x103A || next == 0x1039) continue // asat or virama
        if ((next in 0x1036..0x1038) && next2 == 0x103A) continue
        count++
    }
    // Also add latin words
    val latinWords = text.replace(Regex("[က-႟\\u0E00-\\u0E7F\\u3040-\\u30FF\\u3400-\\u9FFF\\uAC00-\\uD7A3]+"), " ")
        .split(Regex("\\s+")).count { it.isNotBlank() }
    return (count + (latinWords * 1.5).toInt()).coerceAtLeast(1)
}
