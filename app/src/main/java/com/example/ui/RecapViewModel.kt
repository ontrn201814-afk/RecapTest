package com.example.ui

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiRepository
import com.example.data.model.AntiCopyrightConfig
import com.example.data.model.EdgeVoiceConfig
import com.example.data.model.ProjectStatus
import com.example.data.model.RecapFlow
import com.example.data.model.RecapProject
import com.example.data.model.ScriptBeat
import com.example.data.model.SrtSegment
import com.example.data.repository.ProjectRepository
import com.example.data.tts.EdgeTtsEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val titleMy: String, val titleEn: String) {
    HOME("ပင်မ", "Home"),
    TOOLS("Tools", "Tools"),
    WALLET("Wallet", "Wallet"),
    PROJECTS("Projects", "Projects"),
    ACCOUNT("အကောင့်", "Account")
}

enum class StudioStep(val stepNum: Int, val titleMy: String) {
    VIDEO_SRT(1, "၁။ ဗီဒီယို & SRT ထုတ်ယူခြင်း"),
    FLOW_TARGET(2, "၂။ Flow & မိနစ် သတ်မှတ်ခြင်း"),
    GEMINI_SCRIPT(3, "၃။ Gemini Script ဖန်တီးခြင်း"),
    EDGE_VOICE(4, "၄။ Edge အသံ (Thiha/Nilar)"),
    SYNC_STUDIO(5, "၅။ Video+Audio Sync & Anti-Copyright")
}

data class UiState(
    val currentTab: AppTab = AppTab.HOME,
    val isInStudio: Boolean = false,
    val studioStep: StudioStep = StudioStep.VIDEO_SRT,
    val activeProject: RecapProject = RecapProject(title = "New Recap Project"),
    val selectedFlow: RecapFlow = RecapFlow.REAL_RECAP,
    val targetMinutes: Int = 10,
    val characterNamesInput: String = "Zaw Zaw, Lin Lin, Boss Peter, Narrator",
    val customPromptNotes: String = "",
    val isExtractingSrt: Boolean = false,
    val isGeneratingScript: Boolean = false,
    val isPlayingAudio: Boolean = false,
    val activePlayingBeatId: String? = null,
    val audioProgress: Float = 0f,
    val isExportingVideo: Boolean = false,
    val exportProgress: Float = 0f,
    val userApiKeyInput: String = "",
    val activeToolIndex: Int = 0,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class RecapViewModel(application: Application) : AndroidViewModel(application) {
    private val projectRepo = ProjectRepository()
    private val geminiRepo = GeminiRepository()
    val ttsEngine = EdgeTtsEngine(application)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val projects = projectRepo.projects
    val walletBalance = projectRepo.walletBalance
    val userApiKey = projectRepo.userApiKey

    fun switchTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab, isInStudio = false) }
    }

    fun startNewRecap(flow: RecapFlow = RecapFlow.REAL_RECAP) {
        val newProj = RecapProject(
            title = "${flow.titleMy} - ${System.currentTimeMillis() % 10000}",
            flow = flow,
            targetMinutes = 10,
            characterNames = listOf("Zaw Zaw", "Lin Lin", "Boss Peter", "Narrator"),
            srtList = emptyList(),
            scriptBeats = emptyList()
        )
        _uiState.update {
            it.copy(
                isInStudio = true,
                studioStep = StudioStep.VIDEO_SRT,
                selectedFlow = flow,
                targetMinutes = 10,
                activeProject = newProj,
                characterNamesInput = newProj.characterNames.joinToString(", ")
            )
        }
    }

    fun openProjectInStudio(project: RecapProject) {
        val step = when (project.status) {
            ProjectStatus.DRAFT -> StudioStep.VIDEO_SRT
            ProjectStatus.SRT_EXTRACTED -> StudioStep.FLOW_TARGET
            ProjectStatus.SCRIPT_GENERATED -> StudioStep.EDGE_VOICE
            ProjectStatus.AUDIO_GENERATED -> StudioStep.SYNC_STUDIO
            ProjectStatus.EXPORTED -> StudioStep.SYNC_STUDIO
        }
        _uiState.update {
            it.copy(
                isInStudio = true,
                studioStep = step,
                activeProject = project,
                selectedFlow = project.flow,
                targetMinutes = project.targetMinutes,
                characterNamesInput = project.characterNames.joinToString(", ")
            )
        }
    }

    fun exitStudio() {
        _uiState.update { it.copy(isInStudio = false) }
    }

    fun setStudioStep(step: StudioStep) {
        _uiState.update { it.copy(studioStep = step) }
    }

    fun loadSampleVideoAndExtractSrt() {
        viewModelScope.launch {
            _uiState.update { it.copy(isExtractingSrt = true) }
            kotlinx.coroutines.delay(1200) // Realistic processing delay

            val sampleSrt = if (_uiState.value.selectedFlow == RecapFlow.HINDI_CARTOON) {
                ProjectRepository.getSampleCartoonSrt()
            } else {
                ProjectRepository.getSampleSrtList()
            }
            val extractedCharacters = sampleSrt.map { it.character }.distinct().filter { it.isNotBlank() }

            _uiState.update { current ->
                val updatedProj = current.activeProject.copy(
                    srtList = sampleSrt,
                    characterNames = extractedCharacters,
                    status = ProjectStatus.SRT_EXTRACTED
                )
                projectRepo.saveProject(updatedProj)
                current.copy(
                    isExtractingSrt = false,
                    activeProject = updatedProj,
                    characterNamesInput = extractedCharacters.joinToString(", "),
                    successMessage = "SRT dialogue နှင့် ဇာတ်ကောင် အမည် (${extractedCharacters.size}) ခု အောင်မြင်စွာ ထုတ်ယူပြီးပါပြီ!"
                )
            }
        }
    }

    fun onVideoPicked(uri: Uri, name: String) {
        _uiState.update { current ->
            current.copy(
                activeProject = current.activeProject.copy(
                    videoFileName = name,
                    videoUri = uri.toString()
                )
            )
        }
        loadSampleVideoAndExtractSrt()
    }

    fun updateCharacterNames(namesStr: String) {
        _uiState.update { it.copy(characterNamesInput = namesStr) }
    }

    fun selectFlow(flow: RecapFlow) {
        _uiState.update {
            it.copy(
                selectedFlow = flow,
                activeProject = it.activeProject.copy(flow = flow)
            )
        }
    }

    fun updateTargetMinutes(minutes: Int) {
        _uiState.update {
            it.copy(
                targetMinutes = minutes,
                activeProject = it.activeProject.copy(targetMinutes = minutes)
            )
        }
    }

    fun updateCustomPromptNotes(notes: String) {
        _uiState.update { it.copy(customPromptNotes = notes) }
    }

    fun generateGeminiScript() {
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingScript = true, errorMessage = null) }
            val current = _uiState.value
            val chars = current.characterNamesInput.split(",").map { it.trim() }.filter { it.isNotBlank() }

            val result = geminiRepo.generateRecapScript(
                flow = current.selectedFlow,
                srtSegments = current.activeProject.srtList,
                characterNames = chars,
                targetMinutes = current.targetMinutes,
                customPromptNotes = current.customPromptNotes,
                customApiKey = projectRepo.userApiKey.value
            )

            result.onSuccess { beats ->
                _uiState.update { state ->
                    val updatedProj = state.activeProject.copy(
                        flow = state.selectedFlow,
                        targetMinutes = state.targetMinutes,
                        characterNames = chars,
                        scriptBeats = beats,
                        status = ProjectStatus.SCRIPT_GENERATED
                    )
                    projectRepo.saveProject(updatedProj)
                    state.copy(
                        isGeneratingScript = false,
                        activeProject = updatedProj,
                        studioStep = StudioStep.GEMINI_SCRIPT,
                        successMessage = "Gemini မှ Script Beats (${beats.size}) ခု အောင်မြင်စွာ ဖန်တီးပြီးပါပြီ!"
                    )
                }
            }.onFailure { err ->
                _uiState.update {
                    it.copy(
                        isGeneratingScript = false,
                        errorMessage = "Script generation failed: ${err.message}"
                    )
                }
            }
        }
    }

    fun updateBeatNarration(beatId: String, newText: String) {
        _uiState.update { state ->
            val updatedBeats = state.activeProject.scriptBeats.map { beat ->
                if (beat.id == beatId) {
                    beat.copy(narration = newText)
                } else beat
            }
            val updatedProj = state.activeProject.copy(scriptBeats = updatedBeats)
            projectRepo.saveProject(updatedProj)
            state.copy(activeProject = updatedProj)
        }
    }

    fun updateVoiceConfig(config: EdgeVoiceConfig) {
        _uiState.update { state ->
            val updatedProj = state.activeProject.copy(voiceConfig = config)
            projectRepo.saveProject(updatedProj)
            state.copy(activeProject = updatedProj)
        }
    }

    fun updateAntiCopyrightConfig(config: AntiCopyrightConfig) {
        _uiState.update { state ->
            val updatedProj = state.activeProject.copy(antiCopyright = config)
            projectRepo.saveProject(updatedProj)
            state.copy(activeProject = updatedProj)
        }
    }

    fun playBeatAudio(beat: ScriptBeat) {
        val config = _uiState.value.activeProject.voiceConfig
        _uiState.update {
            it.copy(
                isPlayingAudio = true,
                activePlayingBeatId = beat.id,
                audioProgress = 0f
            )
        }
        viewModelScope.launch {
            ttsEngine.playBeat(
                beat = beat,
                config = config,
                onProgress = { p ->
                    _uiState.update { it.copy(audioProgress = p) }
                },
                onComplete = {
                    _uiState.update {
                        it.copy(
                            isPlayingAudio = false,
                            activePlayingBeatId = null,
                            audioProgress = 0f
                        )
                    }
                }
            )
        }
    }

    fun stopAudio() {
        ttsEngine.stop()
        _uiState.update {
            it.copy(isPlayingAudio = false, activePlayingBeatId = null, audioProgress = 0f)
        }
    }

    fun exportFinalRecapVideo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isExportingVideo = true, exportProgress = 0f) }
            for (i in 1..10) {
                kotlinx.coroutines.delay(200)
                _uiState.update { it.copy(exportProgress = i / 10f) }
            }
            _uiState.update { state ->
                val updated = state.activeProject.copy(status = ProjectStatus.EXPORTED)
                projectRepo.saveProject(updated)
                state.copy(
                    isExportingVideo = false,
                    exportProgress = 1f,
                    activeProject = updated,
                    successMessage = "Recap ဗီဒီယို Export ပြုလုပ်ခြင်း အောင်မြင်ပါသည်!"
                )
            }
        }
    }

    fun saveApiKey(key: String) {
        projectRepo.setApiKey(key)
        _uiState.update {
            it.copy(
                userApiKeyInput = key,
                successMessage = "Gemini API Key သိမ်းဆည်းပြီးပါပြီ!"
            )
        }
    }

    fun clearFeedbackMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun setActiveTool(index: Int) {
        _uiState.update { it.copy(activeToolIndex = index) }
    }

    override fun onCleared() {
        super.onCleared()
        ttsEngine.release()
    }
}
