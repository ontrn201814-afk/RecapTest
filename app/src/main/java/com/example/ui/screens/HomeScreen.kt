package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.RecapFlow
import com.example.data.model.RecapProject
import com.example.ui.RecapViewModel
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

@Composable
fun HomeScreen(
    viewModel: RecapViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val walletBalance by viewModel.walletBalance.collectAsState()

    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.startNewRecap(uiState.selectedFlow)
            viewModel.onVideoPicked(uri, "selected_video_${System.currentTimeMillis() % 1000}.mp4")
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ZippyBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp)
    ) {
        // App Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, ZippyGold.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Zippy Recap Pro",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = ZippyGold
                            )
                        )
                        Text(
                            text = "AI Video Recap & Dub Studio",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                        )
                    }
                }

                // Wallet Chip
                Surface(
                    color = ZippyCard,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZippyGold.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable { viewModel.switchTab(com.example.ui.AppTab.WALLET) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🪙", fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$walletBalance",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = ZippyGoldLight,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Hero Workflow Banner: Auto Recap Launcher
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine2),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_workflow_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ZippyGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Auto Recap Pipeline",
                                style = MaterialTheme.typography.labelMedium.copy(color = ZippyGold)
                            )
                        }

                        Surface(
                            color = ZippyCard2,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Anti-Copyright 100%",
                                color = ZippyOk,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "ဗီဒီယို တင်ပြီး Gemini Auto Script & Edge Dub ထုတ်မည်",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = ZippyText,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "SRT Dialogue အရင်ထုတ်ယူပြီး ဇာတ်ကောင် အမည်မလွဲအောင် ထိန်းသိမ်းကာ သတ်မှတ်မိနစ်အတိုင်း အတိအကျ ပြုလုပ်ပေးသည်",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { videoPickerLauncher.launch("video/*") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ZippyGold,
                                contentColor = ZippyOnGold
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_upload_video")
                        ) {
                            Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("ဗီဒီယို ရွေးချယ်မည်", fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.startNewRecap(uiState.selectedFlow)
                                viewModel.loadSampleVideoAndExtractSrt()
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine2),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ZippyGoldLight),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("btn_sample_video")
                        ) {
                            Text("စမ်းသပ် ဗီဒီယို စတင်ရန်", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        // Section: Recap Flows Carousel
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "လုပ်ငန်းစဉ် အမျိုးအစားများ (Recap Flows)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyText
                        )
                    )
                    Text(
                        text = "ရွေးချယ်ထား: ${uiState.selectedFlow.titleMy}",
                        style = MaterialTheme.typography.labelSmall.copy(color = ZippyGold)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(RecapFlow.entries) { flow ->
                        val isSelected = flow == uiState.selectedFlow
                        FlowCard(
                            flow = flow,
                            isSelected = isSelected,
                            onSelect = {
                                viewModel.selectFlow(flow)
                            },
                            onStart = {
                                viewModel.startNewRecap(flow)
                                viewModel.loadSampleVideoAndExtractSrt()
                            }
                        )
                    }
                }
            }
        }

        // Pipeline Process Overview Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard2),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "🔄 အဆင့် (၅) ဆင့် အလိုအလျောက် စနစ်",
                        style = MaterialTheme.typography.labelLarge.copy(color = ZippyGold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    PipelineStepRow(num = "1", title = "SRT Dialogue & ဇာတ်ကောင် အမည် သော့ခတ်ခြင်း", active = true)
                    PipelineStepRow(num = "2", title = "Flow ရွေးချယ် & မိနစ် (3-20') သတ်မှတ်ခြင်း", active = true)
                    PipelineStepRow(num = "3", title = "Gemini API မှ Script Beats ထုတ်လုပ်ခြင်း", active = true)
                    PipelineStepRow(num = "4", title = "Edge TTS (Thiha / Nilar) အသံသွင်းခြင်း", active = true)
                    PipelineStepRow(num = "5", title = "Video Frame + Audio Sync (ထစ်ငေါ့မှုမရှိ + Anti Copyright)", active = true)
                }
            }
        }

        // Recent Projects Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "မကြာမီက ပရောဂျက်များ (${projects.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ZippyText
                    )
                )
                Text(
                    text = "အားလုံးကြည့်မည်",
                    style = MaterialTheme.typography.labelSmall.copy(color = ZippyGold),
                    modifier = Modifier.clickable { viewModel.switchTab(com.example.ui.AppTab.PROJECTS) }
                )
            }
        }

        items(projects) { project ->
            ProjectItemCard(
                project = project,
                onOpen = { viewModel.openProjectInStudio(project) }
            )
        }
    }
}

@Composable
fun FlowCard(
    flow: RecapFlow,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onStart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) ZippyCard2 else ZippyCard
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) ZippyGold else ZippyLine
        ),
        modifier = Modifier
            .width(220.dp)
            .clickable { onSelect() }
            .testTag("flow_card_${flow.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ZippyBg2)
            ) {
                Image(
                    painter = painterResource(id = flow.iconRes),
                    contentDescription = flow.titleMy,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                if (isSelected) {
                    Surface(
                        color = ZippyGold,
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = ZippyOnGold,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = flow.titleMy,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) ZippyGold else ZippyText
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = flow.subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = flow.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = ZippyDim,
                    fontSize = 11.sp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) ZippyGold else ZippyCard,
                    contentColor = if (isSelected) ZippyOnGold else ZippyGold
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp)
            ) {
                Text(text = "စတင်မည်", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PipelineStepRow(num: String, title: String, active: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = if (active) ZippyGold else ZippyCard,
            shape = CircleShape,
            modifier = Modifier.size(22.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = num,
                    color = if (active) ZippyOnGold else ZippyMute,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(color = ZippyText)
        )
    }
}

@Composable
fun ProjectItemCard(
    project: RecapProject,
    onOpen: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ZippyCard),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
            .testTag("project_item_${project.id}")
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = project.flow.iconRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ZippyBg2)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = project.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ZippyText
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = ZippyCard2,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = project.flow.titleMy,
                            style = MaterialTheme.typography.labelSmall.copy(color = ZippyGold),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "⏱ ${project.targetMinutes} မိနစ်",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyDim)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                color = Color(project.status.colorHex).copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(project.status.colorHex).copy(alpha = 0.6f)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = project.status.labelMy,
                    color = Color(project.status.colorHex),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
