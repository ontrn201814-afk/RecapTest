package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RecapFlow
import com.example.data.model.RecapProject
import com.example.ui.RecapViewModel
import com.example.ui.theme.ZippyBad
import com.example.ui.theme.ZippyBg
import com.example.ui.theme.ZippyBg2
import com.example.ui.theme.ZippyCard
import com.example.ui.theme.ZippyCard2
import com.example.ui.theme.ZippyDim
import com.example.ui.theme.ZippyGold
import com.example.ui.theme.ZippyGoldLight
import com.example.ui.theme.ZippyLine
import com.example.ui.theme.ZippyLine2
import com.example.ui.theme.ZippyMute
import com.example.ui.theme.ZippyOnGold
import com.example.ui.theme.ZippyText

@Composable
fun ProjectsScreen(
    viewModel: RecapViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.projects.collectAsState()
    var selectedFilter by remember { mutableStateOf<RecapFlow?>(null) }

    val filteredList = if (selectedFilter == null) {
        projects
    } else {
        projects.filter { it.flow == selectedFilter }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ZippyBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ပရောဂျက်များ (My Projects)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = ZippyGold
                        )
                    )
                    Text(
                        text = "ဖန်တီးထားသော Recap ပရောဂျက် စုစုပေါင်း (${projects.size}) ခု",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                    )
                }

                Button(
                    onClick = { viewModel.startNewRecap() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ZippyGold,
                        contentColor = ZippyOnGold
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_new_project")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("အသစ်", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Filter Chips Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { selectedFilter = null },
                    label = { Text("အားလုံး (${projects.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ZippyGold,
                        selectedLabelColor = ZippyOnGold,
                        containerColor = ZippyCard,
                        labelColor = ZippyText
                    )
                )

                RecapFlow.entries.take(3).forEach { flow ->
                    val isSel = selectedFilter == flow
                    val count = projects.count { it.flow == flow }
                    FilterChip(
                        selected = isSel,
                        onClick = { selectedFilter = if (isSel) null else flow },
                        label = { Text("${flow.titleMy} ($count)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ZippyGold,
                            selectedLabelColor = ZippyOnGold,
                            containerColor = ZippyCard,
                            labelColor = ZippyText
                        )
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            item {
                Surface(
                    color = ZippyCard,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ဤအမျိုးအစားတွင် ပရောဂျက် မရှိသေးပါ။",
                        color = ZippyDim,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(32.dp)
                    )
                }
            }
        } else {
            items(filteredList) { project ->
                ProjectDetailCard(
                    project = project,
                    onOpen = { viewModel.openProjectInStudio(project) }
                )
            }
        }
    }
}

@Composable
fun ProjectDetailCard(
    project: RecapProject,
    onOpen: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ZippyCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = project.flow.iconRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
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
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = ZippyCard2,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = project.flow.titleMy,
                                color = ZippyGold,
                                style = MaterialTheme.typography.labelSmall,
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

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ZippyCard2,
                        contentColor = ZippyGoldLight
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("စတူဒီယိုတွင် ပြင်ဆင်မည်", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
