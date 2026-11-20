package com.example.data.repository

import com.example.data.model.AntiCopyrightConfig
import com.example.data.model.EdgeVoiceConfig
import com.example.data.model.ProjectStatus
import com.example.data.model.RecapFlow
import com.example.data.model.RecapProject
import com.example.data.model.ScriptBeat
import com.example.data.model.SrtSegment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProjectRepository {
    private val _projects = MutableStateFlow<List<RecapProject>>(emptyList())
    val projects: StateFlow<List<RecapProject>> = _projects.asStateFlow()

    private val _walletBalance = MutableStateFlow(4850)
    val walletBalance: StateFlow<Int> = _walletBalance.asStateFlow()

    private val _userApiKey = MutableStateFlow("")
    val userApiKey: StateFlow<String> = _userApiKey.asStateFlow()

    init {
        // Initialize with realistic pre-existing projects for display
        val sample1 = RecapProject(
            id = "proj-1",
            title = "Kingdom of Shadows (Action Recap)",
            flow = RecapFlow.REAL_RECAP,
            targetMinutes = 10,
            videoFileName = "shadow_kingdom_1080p.mp4",
            videoDurationSec = 5400.0,
            characterNames = listOf("Zaw Zaw", "Lin Lin", "Boss Peter", "Narrator"),
            srtList = getSampleSrtList(),
            scriptBeats = getSampleBeats(RecapFlow.REAL_RECAP),
            status = ProjectStatus.AUDIO_GENERATED
        )

        val sample2 = RecapProject(
            id = "proj-2",
            title = "Chhota Bheem & Raju Adventure",
            flow = RecapFlow.HINDI_CARTOON,
            targetMinutes = 8,
            videoFileName = "cartoon_adventure_ep12.mp4",
            videoDurationSec = 1200.0,
            characterNames = listOf("Bheem", "Raju", "Chutki", "Kalia"),
            srtList = getSampleCartoonSrt(),
            scriptBeats = getSampleBeats(RecapFlow.HINDI_CARTOON),
            status = ProjectStatus.EXPORTED
        )

        val sample3 = RecapProject(
            id = "proj-3",
            title = "Tokyo Revenge High School Dub",
            flow = RecapFlow.DIALOGUE_DUB,
            targetMinutes = 12,
            videoFileName = "anime_dub_fight.mp4",
            videoDurationSec = 1440.0,
            characterNames = listOf("Takemichi", "Mikey", "Draken"),
            srtList = getSampleSrtList(),
            scriptBeats = getSampleBeats(RecapFlow.DIALOGUE_DUB),
            status = ProjectStatus.SCRIPT_GENERATED
        )

        _projects.value = listOf(sample1, sample2, sample3)
    }

    fun setApiKey(key: String) {
        _userApiKey.value = key.trim()
    }

    fun deductCoins(amount: Int): Boolean {
        if (_walletBalance.value >= amount) {
            _walletBalance.update { it - amount }
            return true
        }
        return false
    }

    fun addCoins(amount: Int) {
        _walletBalance.update { it + amount }
    }

    fun saveProject(project: RecapProject) {
        _projects.update { current ->
            val idx = current.indexOfFirst { it.id == project.id }
            if (idx >= 0) {
                current.toMutableList().apply { set(idx, project) }
            } else {
                listOf(project) + current
            }
        }
    }

    fun deleteProject(id: String) {
        _projects.update { it.filterNot { p -> p.id == id } }
    }

    fun getProject(id: String): RecapProject? {
        return _projects.value.find { it.id == id }
    }

    companion object {
        fun getSampleSrtList(): List<SrtSegment> = listOf(
            SrtSegment(
                index = 1,
                startTimeMs = 5000,
                endTimeMs = 15000,
                character = "Zaw Zaw",
                text = "ဒီညမှာ ငါတို့ ရန်သူ့စခန်းကို ဝင်စီးရမယ်။ အားလုံး အသင့်ပြင်ထားကြ။"
            ),
            SrtSegment(
                index = 2,
                startTimeMs = 18000,
                endTimeMs = 28000,
                character = "Lin Lin",
                text = "သတိထားနော် Zaw Zaw။ သူတို့ဘက်မှာ လက်နက်အင်အား အများကြီးရှိတယ်။"
            ),
            SrtSegment(
                index = 3,
                startTimeMs = 30000,
                endTimeMs = 42000,
                character = "Boss Peter",
                text = "ဟားဟား! မင်းတို့ ငါ့ရဲ့ ခံတပ်ကို ဘယ်တော့မှ ကျော်ဖြတ်နိုင်မှာ မဟုတ်ဘူး။"
            ),
            SrtSegment(
                index = 4,
                startTimeMs = 45000,
                endTimeMs = 60000,
                character = "Zaw Zaw",
                text = "ကြည့်ကြသေးတာပေါ့ Peter! တရားမျှတမှုက အမြဲတမ်း အနိုင်ရရမယ်။"
            ),
            SrtSegment(
                index = 5,
                startTimeMs = 65000,
                endTimeMs = 80000,
                character = "Narrator",
                text = "နှစ်ဖက် အပြင်းအထန် တိုက်ခိုက်မှုတွေ စတင်ခဲ့ပြီး မထင်မှတ်ထားတဲ့ ဗျူဟာတွေ သုံးခဲ့ကြပါတယ်။"
            ),
            SrtSegment(
                index = 6,
                startTimeMs = 85000,
                endTimeMs = 100000,
                character = "Lin Lin",
                text = "Zaw Zaw, ညာဘက်ကနေ ရန်သူအင်အားတွေ ထပ်တိုးလာနေပြီ!"
            ),
            SrtSegment(
                index = 7,
                startTimeMs = 105000,
                endTimeMs = 120000,
                character = "Zaw Zaw",
                text = "အစီအစဉ် B ကို စတင်မယ်။ အားလုံး လမ်းကြောင်းရှင်းထုတ်လိုက်!"
            ),
            SrtSegment(
                index = 8,
                startTimeMs = 125000,
                endTimeMs = 140000,
                character = "Boss Peter",
                text = "မဖြစ်နိုင်ဘူး... ငါ့ရဲ့ အစီအစဉ်တွေ အကုန် ပျက်စီးသွားပြီ!"
            ),
            SrtSegment(
                index = 9,
                startTimeMs = 145000,
                endTimeMs = 160000,
                character = "Narrator",
                text = "နောက်ဆုံးမှာတော့ စခန်းကို အောင်မြင်စွာ သိမ်းပိုက်နိုင်ခဲ့ပြီး အားလုံးရဲ့ လွတ်လပ်မှုကို ပြန်လည်ရယူနိုင်ခဲ့ပါတယ်။"
            )
        )

        fun getSampleCartoonSrt(): List<SrtSegment> = listOf(
            SrtSegment(
                index = 1,
                startTimeMs = 2000,
                endTimeMs = 10000,
                character = "Bheem",
                text = "ဟေး Raju! ဒီနေ့ ရွာထဲမှာ မုန့်လုပ်တဲ့ပွဲရှိတယ်၊ သွားစားကြရအောင်!"
            ),
            SrtSegment(
                index = 2,
                startTimeMs = 12000,
                endTimeMs = 20000,
                character = "Kalia",
                text = "မင်းတို့ထက် အရင် ငါရောက်ပြီး မုန့်တွေ အကုန် စားပစ်မယ် ကွ!"
            ),
            SrtSegment(
                index = 3,
                startTimeMs = 22000,
                endTimeMs = 32000,
                character = "Chutki",
                text = "Kalia နင်ကတော့ အမြဲတမ်း အစားပုတ်နေတာပဲနော်!"
            ),
            SrtSegment(
                index = 4,
                startTimeMs = 35000,
                endTimeMs = 48000,
                character = "Bheem",
                text = "အားလုံး အပြေးပြိုင်ကြမယ်။ ဘယ်သူ အရင်ရောက်မလဲ စောင့်ကြည့်လိုက်!"
            )
        )

        fun getSampleBeats(flow: RecapFlow): List<ScriptBeat> = when (flow) {
            RecapFlow.REAL_RECAP -> listOf(
                ScriptBeat(
                    beatNumber = 1,
                    timeRange = "N00.00.05-00.00.15",
                    startSec = 5.0,
                    endSec = 15.0,
                    character = "Zaw Zaw",
                    narration = "ညမှောင်မှောင်ထဲမှာ Zaw Zaw ဦးဆောင်တဲ့ အဖွဲ့ဟာ ရန်သူ့ခံတပ်စခန်းဆီကို လျှို့ဝှက်စွာ ချဉ်းကပ်လာခဲ့ပါတယ်။"
                ),
                ScriptBeat(
                    beatNumber = 2,
                    timeRange = "N00.00.18-00.00.28",
                    startSec = 18.0,
                    endSec = 28.0,
                    character = "Lin Lin",
                    narration = "Lin Lin က အန္တရာယ်ကြီးမားကြောင်း သတိပေးလိုက်ပေမယ့် Zaw Zaw ကတော့ စိတ်ဓာတ်မကျဘဲ ဆက်လက်တိုးဝင်ခဲ့တယ်။"
                ),
                ScriptBeat(
                    beatNumber = 3,
                    timeRange = "N00.00.30-00.00.42",
                    startSec = 30.0,
                    endSec = 42.0,
                    character = "Boss Peter",
                    narration = "ဒီအချိန်မှာ Boss Peter က မာန်ဝင့်စွာနဲ့ ပေါ်ထွက်လာပြီး သူတို့ကို ဝိုင်းရံဖမ်းဆီးဖို့ အမိန့်ပေးလိုက်ပါတယ်။"
                ),
                ScriptBeat(
                    beatNumber = 4,
                    timeRange = "N00.00.45-00.01.00",
                    startSec = 45.0,
                    endSec = 60.0,
                    character = "Zaw Zaw",
                    narration = "သို့သော် Zaw Zaw တို့အဖွဲ့ရဲ့ အလွန်သွက်လက်တဲ့ တုံ့ပြန်မှုကြောင့် ရန်သူ့တပ်ဖွဲ့တွေ ကစဉ့်ကလျား ဖြစ်သွားခဲ့ရပါတယ်။"
                )
            )
            RecapFlow.HINDI_CARTOON -> listOf(
                ScriptBeat(
                    beatNumber = 1,
                    timeRange = "N00.00.02-00.00.10",
                    startSec = 2.0,
                    endSec = 10.0,
                    character = "Bheem",
                    narration = "ဟားဟား.. ဒီနေ့တော့ မောင် Bheem တို့အဖွဲ့ မုန့်စားပွဲကို အပြေးလွှား ရောက်ရှိလာပါပြီခင်ဗျာ။"
                ),
                ScriptBeat(
                    beatNumber = 2,
                    timeRange = "N00.00.12-00.00.20",
                    startSec = 12.0,
                    endSec = 20.0,
                    character = "Kalia",
                    narration = "ကြည့်ကြပါဦးဗျာ၊ တစ်ဖက်က Kalia ကြီးကတော့ မုန့်တွေအကုန် တစ်ယောက်တည်း ဝါးမြိုဖို့ ကြံစည်နေပါပြီ။"
                )
            )
            else -> listOf(
                ScriptBeat(
                    beatNumber = 1,
                    timeRange = "N00.00.05-00.00.18",
                    startSec = 5.0,
                    endSec = 18.0,
                    character = "Narrator",
                    narration = "ဇာတ်ကွက်တစ်ခုချင်းစီတိုင်းမှာ ဇာတ်ကောင်တွေရဲ့ ခံစားချက်နဲ့ ဆုံးဖြတ်ချက်တွေက ရင်ခုန်စရာ ကောင်းနေခဲ့ပါတယ်။"
                )
            )
        }
    }
}
