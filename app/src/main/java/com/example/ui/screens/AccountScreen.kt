package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
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
import com.example.ui.theme.ZippyOnGold
import com.example.ui.theme.ZippyText

@Composable
fun AccountScreen(
    viewModel: RecapViewModel,
    modifier: Modifier = Modifier
) {
    val userApiKey by viewModel.userApiKey.collectAsState()
    var inputKey by remember(userApiKey) { mutableStateOf(userApiKey) }

    val hasBuildConfigKey = runCatching { BuildConfig.GEMINI_API_KEY }.getOrNull()?.let {
        it.isNotBlank() && it != "MY_GEMINI_API_KEY"
    } ?: false

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ZippyBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // User Profile Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("account_profile_card")
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cp2_ava),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .border(2.dp, ZippyGold, CircleShape)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Pro Creator",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ZippyText
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = ZippyGold,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "VIP",
                                    color = ZippyOnGold,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "thesix070bio@gmail.com",
                            style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Zippy Cut Pro II • Licensed Studio",
                            style = MaterialTheme.typography.labelSmall.copy(color = ZippyDim)
                        )
                    }
                }
            }
        }

        // Gemini API Key Configuration
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard2),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyGold.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gemini_key_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Key, contentDescription = null, tint = ZippyGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Gemini API Key ချိန်ညှိချက်",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ZippyGold
                                )
                            )
                        }

                        Surface(
                            color = if (hasBuildConfigKey || userApiKey.isNotBlank()) ZippyOk.copy(alpha = 0.2f) else ZippyCard,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (hasBuildConfigKey || userApiKey.isNotBlank()) "ချိတ်ဆက်ပြီး" else "အသင့်ရှိ",
                                color = if (hasBuildConfigKey || userApiKey.isNotBlank()) ZippyOk else ZippyDim,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Gemini API Key ကို AI Studio Secrets panel မှတစ်ဆင့် သို့မဟုတ် ဤနေရာတွင် ကိုယ်ပိုင် API Key တိုက်ရိုက် ထည့်သွင်းနိုင်ပါသည်:",
                        style = MaterialTheme.typography.bodySmall.copy(color = ZippyMute)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = inputKey,
                        onValueChange = { inputKey = it },
                        placeholder = { Text("AIzaSy... (API Key ထည့်သွင်းပါ)", color = ZippyDim) },
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
                            .testTag("input_api_key")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { viewModel.saveApiKey(inputKey) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ZippyGold,
                            contentColor = ZippyOnGold
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_save_key")
                    ) {
                        Text("API Key သိမ်းဆည်းမည်", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // App Information & Version
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ℹ️ ဆော့ဖ်ဝဲ အချက်အလက် (About)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ZippyGoldLight
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    InfoRow("ဗားရှင်း", "v2.8.5 Pro (Myanmar)")
                    InfoRow("Gemini Engine", "gemini-2.5-flash")
                    InfoRow("TTS Voice Engine", "Edge TTS (Thiha / Nilar)")
                    InfoRow("Anti-Copyright Protection", "Enabled (Fair Use 100%)")
                    InfoRow("Video Render", "1080p FHD 60fps Hardware Accelerated")
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(color = ZippyMute))
        Text(text = value, style = MaterialTheme.typography.bodyMedium.copy(color = ZippyText, fontWeight = FontWeight.Medium))
    }
}
