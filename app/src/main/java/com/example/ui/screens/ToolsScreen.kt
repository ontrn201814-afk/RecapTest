package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.RecapViewModel
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
import com.example.ui.theme.ZippyOk
import com.example.ui.theme.ZippyText

data class ToolItem(
    val titleMy: String,
    val subtitle: String,
    val description: String,
    val icon: ImageVector,
    val tag: String
)

@Composable
fun ToolsScreen(
    viewModel: RecapViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    val toolsList = listOf(
        ToolItem(
            titleMy = "✂️ Video Editor & Timeline Cutter",
            subtitle = "Video Editing Engine",
            description = "ဗီဒီယို အပိုင်းအစများ ဖြတ်ညှပ်ခြင်း၊ Split၊ Trim၊ အမြန်နှုန်း ချိန်ညှိခြင်းနှင့် Transition အထူးပြုလုပ်ချက်များ",
            icon = Icons.Default.ContentCut,
            tag = "tool_cutter"
        ),
        ToolItem(
            titleMy = "📝 SRT Subtitle Generator & Editor",
            subtitle = "Subtitle Studio",
            description = "ဗီဒီယိုမှ စကားပြောစာသားများကို အလိုအလျောက် SRT ထုတ်ယူခြင်းနှင့် မြန်မာယူနီကုဒ်ဖြင့် ပြင်ဆင်တည်းဖြတ်ခြင်း",
            icon = Icons.Default.Subtitles,
            tag = "tool_subtitles"
        ),
        ToolItem(
            titleMy = "🎙️ Edge Voice Studio (Thiha / Nilar)",
            subtitle = "TTS Synthesizer",
            description = "Thiha & Nilar အသံများဖြင့် မြန်မာစာသားများကို အသံဖိုင်အဖြစ် အလွယ်တကူ သွင်းယူထုတ်လုပ်ခြင်း",
            icon = Icons.Default.RecordVoiceOver,
            tag = "tool_voice"
        ),
        ToolItem(
            titleMy = "🛡️ Anti-Copyright & Blur Mask Studio",
            subtitle = "Fair Use Transform",
            description = "Horizontal Flip၊ Blur Box အကွက်များ၊ Dynamic Zoom နှင့် Color Grading များဖြင့် မူပိုင်ခွင့် ကာကွယ်ခြင်း",
            icon = Icons.Default.Shield,
            tag = "tool_anticopyright"
        ),
        ToolItem(
            titleMy = "🖼️ Recap Poster & Thumbnail Maker",
            subtitle = "Poster & Art Maker",
            description = "TikTok၊ YouTube နှင့် Facebook အတွက် ဆွဲဆောင်မှုရှိသော မြန်မာ စာတန်းထိုး ကာဗာပုံများ ဖန်တီးခြင်း",
            icon = Icons.Default.Image,
            tag = "tool_poster"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ZippyBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        item {
            Text(
                text = "ကိရိယာများ (Zippy Studio Tools)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = ZippyGold
                )
            )
            Text(
                text = "Recap ဖန်တီးရာတွင် သီးခြား အသုံးပြုနိုင်သော စတူဒီယို ကိရိယာများ",
                style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
            )
        }

        itemsIndexed(toolsList) { index, tool ->
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.startNewRecap()
                        viewModel.loadSampleVideoAndExtractSrt()
                    }
                    .testTag(tool.tag)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = ZippyBg2,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(50.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(tool.icon, contentDescription = null, tint = ZippyGold, modifier = Modifier.size(24.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tool.titleMy,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ZippyText
                            )
                        )
                        Text(
                            text = tool.subtitle,
                            style = MaterialTheme.typography.labelSmall.copy(color = ZippyGoldLight)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = tool.description,
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute, fontSize = 11.5.sp)
                        )
                    }
                }
            }
        }
    }
}
