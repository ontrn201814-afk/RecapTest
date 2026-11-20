package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AntiCopyrightConfig
import com.example.data.model.EdgeVoiceConfig
import com.example.data.model.RecapFlow
import com.example.data.model.ScriptBeat
import com.example.data.model.SrtSegment
import com.example.ui.RecapViewModel
import com.example.ui.StudioStep
import com.example.ui.theme.ZippyBad
import com.example.ui.theme.ZippyBg
import com.example.ui.theme.ZippyBg2
import com.example.ui.theme.ZippyCard
import com.example.ui.theme.ZippyCard2
import com.example.ui.theme.ZippyDim
import com.example.ui.theme.ZippyGold
import com.example.ui.theme.ZippyGoldDark
import com.example.ui.theme.ZippyGoldLight
import com.example.ui.theme.ZippyLine
import com.example.ui.theme.ZippyLine2
import com.example.ui.theme.ZippyMute
import com.example.ui.theme.ZippyOk
import com.example.ui.theme.ZippyOnGold
import com.example.ui.theme.ZippyText
import com.example.ui.theme.ZippyWarn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecapStudioScreen(
    viewModel: RecapViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val project = uiState.activeProject

    // Handle back button to exit studio
    BackHandler {
        viewModel.exitStudio()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ZippyGold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${project.flow.titleMy} • ${project.targetMinutes} မိနစ်",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.exitStudio() },
                        modifier = Modifier.testTag("btn_studio_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = ZippyGold
                        )
                    }
                },
                actions = {
                    Surface(
                        color = Color(project.status.colorHex).copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = project.status.labelMy,
                            color = Color(project.status.colorHex),
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ZippyBg2
                )
            )
        },
        containerColor = ZippyBg,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Horizontal Step Selector Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ZippyBg2)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StudioStep.entries.forEach { step ->
                    val isSelected = uiState.studioStep == step
                    val isDone = step.stepNum < uiState.studioStep.stepNum
                    Surface(
                        color = if (isSelected) ZippyGold else if (isDone) ZippyCard2 else ZippyCard,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) ZippyGoldLight else ZippyLine
                        ),
                        modifier = Modifier
                            .clickable { viewModel.setStudioStep(step) }
                            .testTag("step_tab_${step.stepNum}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${step.stepNum}",
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) ZippyOnGold else if (isDone) ZippyOk else ZippyMute,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = step.titleMy.substringAfter("။ "),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) ZippyOnGold else ZippyText
                                )
                            )
                        }
                    }
                }
            }

            // Status notification bar (Success / Error)
            AnimatedVisibility(visible = uiState.successMessage != null || uiState.errorMessage != null) {
                val isErr = uiState.errorMessage != null
                Surface(
                    color = if (isErr) ZippyBad.copy(alpha = 0.2f) else ZippyOk.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = uiState.errorMessage ?: uiState.successMessage ?: "",
                            color = if (isErr) ZippyBad else ZippyOk,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "✕",
                            color = ZippyText,
                            modifier = Modifier
                                .clickable { viewModel.clearFeedbackMessages() }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // Step Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                when (uiState.studioStep) {
                    StudioStep.VIDEO_SRT -> StepVideoSrtContent(viewModel = viewModel)
                    StudioStep.FLOW_TARGET -> StepFlowTargetContent(viewModel = viewModel)
                    StudioStep.GEMINI_SCRIPT -> StepGeminiScriptContent(viewModel = viewModel)
                    StudioStep.EDGE_VOICE -> StepEdgeVoiceContent(viewModel = viewModel)
                    StudioStep.SYNC_STUDIO -> StepSyncStudioContent(viewModel = viewModel)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 1: VIDEO & SRT EXTRACTION
// -------------------------------------------------------------
@Composable
fun StepVideoSrtContent(viewModel: RecapViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val project = uiState.activeProject

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onVideoPicked(uri, "uploaded_movie_clip.mp4")
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Video Source Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📹 ရင်းမြစ် ဗီဒီယို (Source Video)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = ZippyBg2,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Videocam, contentDescription = null, tint = ZippyGold)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = project.videoFileName,
                                style = MaterialTheme.typography.titleMedium.copy(color = ZippyText)
                            )
                            Text(
                                text = "ကြာချိန်: 15:00 • 1080p FHD • 60fps",
                                style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                            )
                        }
                        OutlinedButton(
                            onClick = { videoPickerLauncher.launch("video/*") },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ZippyGoldLight),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine2)
                        ) {
                            Text("ပြောင်းမည်", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }

        // SRT Auto Extraction Action
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard2),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "🎯 အလိုအလျောက် SRT & ဇာတ်ကောင် ခွဲခြမ်းစိတ်ဖြာခြင်း",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ZippyText
                                )
                            )
                            Text(
                                text = "ဇာတ်ကောင် အမည်များ မမှားစေရန် Dialogue စာသား အရင်ထုတ်ယူပါမည်",
                                style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.isExtractingSrt) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                color = ZippyGold,
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "SRT Dialogue များနှင့် ဇာတ်ကောင်များကို ထုတ်ယူနေပါသည်...",
                                style = MaterialTheme.typography.bodySmall.copy(color = ZippyGoldLight)
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.loadSampleVideoAndExtractSrt() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ZippyGold,
                                contentColor = ZippyOnGold
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_extract_srt")
                        ) {
                            Icon(Icons.Default.Subtitles, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (project.srtList.isEmpty()) "SRT & ဇာတ်ကောင် အမည်များ အလိုအလျောက် ထုတ်ယူမည်" else "SRT ပြန်လည် ထုတ်ယူမည်",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Locked Character Names Field
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🔒 သော့ခတ်ထားသော ဇာတ်ကောင် အမည်များ (Locked Characters)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGoldLight
                        )
                    )
                    Text(
                        text = "Gemini မှ Script ရေးသားရာတွင် ဤအမည်များကိုသာ အတိအကျ သုံးပါမည် (အလွဲမဖြစ်စေရ):",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.characterNamesInput,
                        onValueChange = { viewModel.updateCharacterNames(it) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ZippyGold,
                            unfocusedBorderColor = ZippyLine,
                            focusedTextColor = ZippyText,
                            unfocusedTextColor = ZippyText,
                            focusedContainerColor = ZippyBg2,
                            unfocusedContainerColor = ZippyBg2
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_character_names")
                    )
                }
            }
        }

        // Extracted SRT Segments List
        item {
            Text(
                text = "ထုတ်ယူရရှိထားသော SRT Dialogue များ (${project.srtList.size} ခု)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ZippyText
                )
            )
        }

        if (project.srtList.isEmpty()) {
            item {
                Surface(
                    color = ZippyBg2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "SRT စာသား မရှိသေးပါ။ အပေါ်ရှိ 'SRT အလိုအလျောက် ထုတ်ယူမည်' ခလုတ်ကို နှိပ်ပါ။",
                        color = ZippyDim,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        } else {
            items(project.srtList) { seg ->
                SrtSegmentItem(segment = seg)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { viewModel.setStudioStep(StudioStep.FLOW_TARGET) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ZippyGold,
                        contentColor = ZippyOnGold
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_goto_step2")
                ) {
                    Text(text = "နောက်တစ်ဆင့်: Flow & မိနစ် သတ်မှတ်မည် ➔", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SrtSegmentItem(segment: SrtSegment) {
    Surface(
        color = ZippyCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = ZippyCard2,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${segment.formatStartTime()} ➔ ${segment.formatEndTime()}",
                        style = MaterialTheme.typography.labelSmall.copy(color = ZippyGoldLight),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    color = ZippyGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = segment.character,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ZippyGold,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = segment.text,
                style = MaterialTheme.typography.bodyMedium.copy(color = ZippyText)
            )
        }
    }
}

// -------------------------------------------------------------
// STEP 2: FLOW & TARGET MINUTES CUSTOMIZATION
// -------------------------------------------------------------
@Composable
fun StepFlowTargetContent(viewModel: RecapViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val project = uiState.activeProject

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Flow Selection
        item {
            Text(
                text = "၁။ Recap Flow ရွေးချယ်ပါ",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ZippyGold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                RecapFlow.entries.take(3).forEach { flow ->
                    val isSelected = flow == uiState.selectedFlow
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) ZippyCard2 else ZippyCard
                        ),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) ZippyGold else ZippyLine
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.selectFlow(flow) }
                            .testTag("flow_select_${flow.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = flow.iconRes),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = flow.titleMy,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = if (isSelected) ZippyGold else ZippyText,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = flow.description,
                                    style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = ZippyGold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Target Duration in Minutes
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⏱ မိနစ် သတ်မှတ်ခြင်း (Target Length)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ZippyGoldLight
                            )
                        )
                        Text(
                            text = "${uiState.targetMinutes} မိနစ်",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = ZippyGold,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Preset minute chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(3, 5, 8, 10, 12, 15, 20).forEach { mins ->
                            val isSel = uiState.targetMinutes == mins
                            Surface(
                                color = if (isSel) ZippyGold else ZippyCard2,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.clickable { viewModel.updateTargetMinutes(mins) }
                            ) {
                                Text(
                                    text = "$mins မိနစ်",
                                    color = if (isSel) ZippyOnGold else ZippyText,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Slider(
                        value = uiState.targetMinutes.toFloat(),
                        onValueChange = { viewModel.updateTargetMinutes(it.toInt()) },
                        valueRange = 1f..30f,
                        steps = 28,
                        colors = SliderDefaults.colors(
                            thumbColor = ZippyGold,
                            activeTrackColor = ZippyGold,
                            inactiveTrackColor = ZippyLine
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "ခန့်မှန်း အသံထွက် ဝဏ္ဏပေါင်း: ~${uiState.targetMinutes * 310} syllables",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyOk)
                        )
                        Text(
                            text = "Scene Beats: ~${uiState.targetMinutes * 3} ခု",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                        )
                    }
                }
            }
        }

        // Custom Prompt Notes
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "✍️ အထူး ညွှန်ကြားချက် (Prompt Notes - Optional)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGoldLight
                        )
                    )
                    Text(
                        text = "ဥပမာ - ဇာတ်သိမ်းကို လျှို့ဝှက်ချက်မဖော်ဘဲ cliffhanger ပုံစံထားပါ၊ ဟာသပိုထည့်ပါ",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.customPromptNotes,
                        onValueChange = { viewModel.updateCustomPromptNotes(it) },
                        placeholder = { Text("ထပ်ဆောင်း ညွှန်ကြားချက်များ ရိုက်ထည့်ပါ...", color = ZippyDim) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ZippyGold,
                            unfocusedBorderColor = ZippyLine,
                            focusedTextColor = ZippyText,
                            unfocusedTextColor = ZippyText,
                            focusedContainerColor = ZippyBg2,
                            unfocusedContainerColor = ZippyBg2
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            }
        }

        // Generate Script Button
        item {
            Button(
                onClick = { viewModel.generateGeminiScript() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ZippyGold,
                    contentColor = ZippyOnGold
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !uiState.isGeneratingScript,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_generate_script")
            ) {
                if (uiState.isGeneratingScript) {
                    CircularProgressIndicator(
                        color = ZippyOnGold,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gemini AI Script ဖန်တီးနေပါသည်...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gemini AI Script စတင် ဖန်တီးမည်", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 3: GEMINI SCRIPT REVIEW
// -------------------------------------------------------------
@Composable
fun StepGeminiScriptContent(viewModel: RecapViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val project = uiState.activeProject

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "📜 Gemini Script Beats (${project.scriptBeats.size} ခု)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGold
                        )
                    )
                    Text(
                        text = "စာသားများကို တိုက်ရိုက် ပြင်ဆင်နိုင်ပြီး အသံထွက်ကြာချိန်ကို အလိုအလျောက် ချိန်ညှိပါမည်",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.generateGeminiScript() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ZippyGold),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine2)
                ) {
                    Text("ပြန်ထုတ်မည်", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        items(project.scriptBeats) { beat ->
            ScriptBeatItem(
                beat = beat,
                onEdit = { newText -> viewModel.updateBeatNarration(beat.id, newText) },
                onPlay = { viewModel.playBeatAudio(beat) },
                isPlaying = uiState.isPlayingAudio && uiState.activePlayingBeatId == beat.id
            )
        }

        item {
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { viewModel.setStudioStep(StudioStep.EDGE_VOICE) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ZippyGold,
                    contentColor = ZippyOnGold
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_goto_step4")
            ) {
                Text("နောက်တစ်ဆင့်: Edge အသံ (Thiha/Nilar) ရွေးချယ်မည် ➔", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ScriptBeatItem(
    beat: ScriptBeat,
    onEdit: (String) -> Unit,
    onPlay: () -> Unit,
    isPlaying: Boolean
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedText by remember { mutableStateOf(beat.narration) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) ZippyCard2 else ZippyCard
        ),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPlaying) ZippyGold else ZippyLine
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = ZippyBg2,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = beat.timeRange,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ZippyGold,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "[${beat.character}]",
                        style = MaterialTheme.typography.labelSmall.copy(color = ZippyOk)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${beat.syllableCount} syllables (~${String.format("%.1f", beat.audioDurationSec)}s)",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyDim, fontSize = 11.sp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            tint = if (isPlaying) ZippyBad else ZippyGold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                OutlinedTextField(
                    value = editedText,
                    onValueChange = { editedText = it },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ZippyGold,
                        unfocusedBorderColor = ZippyLine,
                        focusedTextColor = ZippyText,
                        unfocusedTextColor = ZippyText,
                        focusedContainerColor = ZippyBg2,
                        unfocusedContainerColor = ZippyBg2
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            onEdit(editedText)
                            isEditing = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ZippyGold, contentColor = ZippyOnGold),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("သိမ်းဆည်းမည်", style = MaterialTheme.typography.labelSmall)
                    }
                }
            } else {
                Text(
                    text = beat.narration,
                    style = MaterialTheme.typography.bodyMedium.copy(color = ZippyText),
                    modifier = Modifier.clickable { isEditing = true }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 4: EDGE TTS VOICE STUDIO (THIHA & NILAR)
// -------------------------------------------------------------
@Composable
fun StepEdgeVoiceContent(viewModel: RecapViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val project = uiState.activeProject
    val voiceConfig = project.voiceConfig

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Voice Selection: Thiha vs Nilar
        item {
            Text(
                text = "🎙️ Edge အသံ ရွေးချယ်ပါ (Thiha / Nilar)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ZippyGold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Thiha Card
                val isThiha = voiceConfig.voiceName == "Thiha"
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isThiha) ZippyCard2 else ZippyCard
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isThiha) 2.dp else 1.dp,
                        color = if (isThiha) ZippyGold else ZippyLine
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.updateVoiceConfig(voiceConfig.copy(voiceName = "Thiha")) }
                        .testTag("voice_thiha")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "👨 Thiha", fontWeight = FontWeight.Bold, color = ZippyGold)
                            if (isThiha) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ZippyGold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "my-MM-ThihaNeural",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyDim)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "မင်းသားသံ / ဇာတ်လမ်း Recap အတွက် အသံဩဇာပြည့်ဝသော Baritone အသံ",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                        )
                    }
                }

                // Nilar Card
                val isNilar = voiceConfig.voiceName == "Nilar"
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isNilar) ZippyCard2 else ZippyCard
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isNilar) 2.dp else 1.dp,
                        color = if (isNilar) ZippyGold else ZippyLine
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { viewModel.updateVoiceConfig(voiceConfig.copy(voiceName = "Nilar")) }
                        .testTag("voice_nilar")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "👩 Nilar", fontWeight = FontWeight.Bold, color = ZippyGold)
                            if (isNilar) Icon(Icons.Default.CheckCircle, contentDescription = null, tint = ZippyGold)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "my-MM-NilarNeural",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyDim)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "ဇာတ်လမ်းပြောသူသံ / သာယာကြည်လင်ပြီး ရှင်းလင်းပြတ်သားသော အသံ",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                        )
                    }
                }
            }
        }

        // Voice Controls: BASE_RATE, BASE_PITCH, MAX_CONCURRENT
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚙️ Edge Voice အသေးစိတ် ချိန်ညှိချက်များ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGoldLight
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // BASE_RATE Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "အမြန်နှုန်း (BASE_RATE)", style = MaterialTheme.typography.bodyMedium.copy(color = ZippyText))
                        Text(
                            text = "${if (voiceConfig.baseRate >= 0) "+" else ""}${voiceConfig.baseRate}%",
                            color = ZippyGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = voiceConfig.baseRate.toFloat(),
                        onValueChange = { viewModel.updateVoiceConfig(voiceConfig.copy(baseRate = it.toInt())) },
                        valueRange = -50f..50f,
                        colors = SliderDefaults.colors(thumbColor = ZippyGold, activeTrackColor = ZippyGold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // BASE_PITCH Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "အသံအနိမ့်အမြင့် (BASE_PITCH)", style = MaterialTheme.typography.bodyMedium.copy(color = ZippyText))
                        Text(
                            text = "${if (voiceConfig.basePitch >= 0) "+" else ""}${voiceConfig.basePitch} Hz",
                            color = ZippyGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = voiceConfig.basePitch.toFloat(),
                        onValueChange = { viewModel.updateVoiceConfig(voiceConfig.copy(basePitch = it.toInt())) },
                        valueRange = -50f..50f,
                        colors = SliderDefaults.colors(thumbColor = ZippyGold, activeTrackColor = ZippyGold)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // MAX_CONCURRENT Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "တစ်ပြိုင်နက် သွင်းမှု (MAX_CONCURRENT)", style = MaterialTheme.typography.bodyMedium.copy(color = ZippyText))
                        Text(
                            text = "${voiceConfig.maxConcurrent} Channels",
                            color = ZippyGold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = voiceConfig.maxConcurrent.toFloat(),
                        onValueChange = { viewModel.updateVoiceConfig(voiceConfig.copy(maxConcurrent = it.toInt())) },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(thumbColor = ZippyGold, activeTrackColor = ZippyGold)
                    )
                }
            }
        }

        // Live Audio Preview Player Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard2),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🔊 လက်ရှိ အသံ အစမ်းနားဆင်ခြင်း",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val firstBeat = project.scriptBeats.firstOrNull()
                    val sampleLine = firstBeat?.narration ?: "ဇာတ်လမ်း Recap ကို သာယာကြည်လင်သော မြန်မာ အသံထွက်ဖြင့် ဖတ်ပြပေးပါမည်။"
                    Text(
                        text = "\"$sampleLine\"",
                        style = MaterialTheme.typography.bodyMedium.copy(color = ZippyText)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (uiState.isPlayingAudio) {
                        LinearProgressIndicator(
                            progress = { uiState.audioProgress },
                            color = ZippyGold,
                            trackColor = ZippyLine,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                if (uiState.isPlayingAudio) {
                                    viewModel.stopAudio()
                                } else {
                                    firstBeat?.let { viewModel.playBeatAudio(it) }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (uiState.isPlayingAudio) ZippyBad else ZippyGold,
                                contentColor = ZippyOnGold
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (uiState.isPlayingAudio) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (uiState.isPlayingAudio) "ရပ်တန့်မည်" else "အသံ စမ်းသပ် ဖွင့်မည်", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.setStudioStep(StudioStep.SYNC_STUDIO) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ZippyGold,
                    contentColor = ZippyOnGold
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_goto_step5")
            ) {
                Text("နောက်တစ်ဆင့်: Video+Audio Frame Sync & Anti-Copyright ➔", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 5: VIDEO + AUDIO SYNC & ANTI-COPYRIGHT
// -------------------------------------------------------------
@Composable
fun StepSyncStudioContent(viewModel: RecapViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val project = uiState.activeProject
    val anti = project.antiCopyright

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Video Preview Canvas with Anti-Copyright Overlays
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyBg2),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine2)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        // Simulated Video Frame
                        Image(
                            painter = painterResource(id = R.drawable.intro_banner),
                            contentDescription = "Video Canvas",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                            // Flip effect simulation
                        )

                        // Top Blur Overlay Mask
                        if (anti.blurTopBottom) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .fillMaxWidth()
                                    .height(35.dp)
                                    .background(Color.Black.copy(alpha = 0.6f))
                            ) {
                                Text(
                                    text = "🌫 Top Blur Safe Zone",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 10.sp,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }

                        // Bottom Subtitle & Blur Mask
                        if (anti.blurTopBottom) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .height(45.dp)
                                    .background(Color.Black.copy(alpha = 0.75f))
                            ) {
                                Text(
                                    text = project.scriptBeats.firstOrNull()?.narration ?: "Burmese Subtitle & Narration Sync Bar",
                                    color = ZippyGold,
                                    fontSize = 11.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(horizontal = 8.dp)
                                )
                            }
                        }

                        // Anti-Copyright Watermark Badge
                        Surface(
                            color = ZippyGold.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Anti-Copyright 100%",
                                color = ZippyOnGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // Ratio Indicator
                        Surface(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = anti.canvasRatio,
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "⚡ Frame + Audio Sync: လှုပ်ခါမှု မရှိစေဘဲ အသံနှင့် ဗီဒီယို အတိအကျ ချိန်ညှိထားသည်",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyOk)
                    )
                }
            }
        }

        // Canvas Aspect Ratio Picker
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "📐 ဗီဒီယို ပုံစံ (Canvas Aspect Ratio)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGoldLight
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("9:16" to "TikTok/Reels", "16:9" to "YouTube", "1:1" to "Square", "4:5" to "Facebook").forEach { (ratio, sub) ->
                            val isSel = anti.canvasRatio == ratio
                            Surface(
                                color = if (isSel) ZippyGold else ZippyCard2,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateAntiCopyrightConfig(anti.copy(canvasRatio = ratio)) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = ratio,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) ZippyOnGold else ZippyText
                                    )
                                    Text(
                                        text = sub,
                                        fontSize = 9.sp,
                                        color = if (isSel) ZippyOnGold.copy(alpha = 0.8f) else ZippyDim
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Anti-Copyright Protection Suite
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🛡️ Anti-Copyright ကာကွယ်မှု စနစ် (Fair Use)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGoldLight
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Horizontal Flip Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "↔ အလျားလိုက် ပြောင်းပြန်လှန်ခြင်း (Horizontal Flip)", color = ZippyText, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "ဗီဒီယို Content ID Match မမိစေရန်", color = ZippyDim, style = MaterialTheme.typography.bodySmall)
                        }
                        Switch(
                            checked = anti.flipHorizontal,
                            onCheckedChange = { viewModel.updateAntiCopyrightConfig(anti.copy(flipHorizontal = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = ZippyGold, checkedTrackColor = ZippyCard2)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Top/Bottom Blur Mask Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "🌫 အထက်/အောက် Blur Mask အုပ်ခြင်း", color = ZippyText, style = MaterialTheme.typography.bodyMedium)
                            Text(text = "မူရင်း ရေစာနှင့် တိုက်ရိုက် မူပိုင်ခွင့် ကာကွယ်ရန်", color = ZippyDim, style = MaterialTheme.typography.bodySmall)
                        }
                        Switch(
                            checked = anti.blurTopBottom,
                            onCheckedChange = { viewModel.updateAntiCopyrightConfig(anti.copy(blurTopBottom = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = ZippyGold, checkedTrackColor = ZippyCard2)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Zoom factor slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "🔍 Dynamic Zoom ချဲ့ထွင်မှု", color = ZippyText, style = MaterialTheme.typography.bodyMedium)
                        Text(text = "${String.format("%.2f", anti.zoomFactor)}x", color = ZippyGold, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = anti.zoomFactor,
                        onValueChange = { viewModel.updateAntiCopyrightConfig(anti.copy(zoomFactor = it)) },
                        valueRange = 1.0f..1.3f,
                        colors = SliderDefaults.colors(thumbColor = ZippyGold, activeTrackColor = ZippyGold)
                    )
                }
            }
        }

        // Export Render Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard2),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyGold)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎬 Recap ဗီဒီယို အပြီးသတ် ထုတ်လုပ်ခြင်း (Export)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Audio + Video Frame များကို အတိအကျ ပေါင်းစပ်၍ 1080p FHD အရည်အသွေးဖြင့် Render ပြုလုပ်ပါမည်",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (uiState.isExportingVideo) {
                        Column {
                            LinearProgressIndicator(
                                progress = { uiState.exportProgress },
                                color = ZippyGold,
                                trackColor = ZippyLine,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Render ပြုလုပ်နေပါသည်... ${(uiState.exportProgress * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall.copy(color = ZippyGoldLight),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    } else {
                        Button(
                            onClick = { viewModel.exportFinalRecapVideo() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ZippyGold,
                                contentColor = ZippyOnGold
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_export_video")
                        ) {
                            Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Render & Save Recap Video", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}
