package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.ZippyWarn

@Composable
fun WalletScreen(
    viewModel: RecapViewModel,
    modifier: Modifier = Modifier
) {
    val walletBalance by viewModel.walletBalance.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(ZippyBg)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 90.dp)
    ) {
        // Balance Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard2),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyGold.copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("wallet_balance_card")
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "လက်ကျန် ဒင်္ဂါး (Tokens Balance)",
                        style = MaterialTheme.typography.bodyMedium.copy(color = ZippyMute)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🪙", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "$walletBalance",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                color = ZippyGold,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Coins",
                            style = MaterialTheme.typography.titleMedium.copy(color = ZippyGoldLight)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { /* Claim daily bonus */ },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ZippyGold,
                                contentColor = ZippyOnGold
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CardGiftcard, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("နေ့စဉ်ဆုလာဘ် ရယူမည်", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        // Coin Packages
        item {
            Text(
                text = "ဒင်္ဂါး ပက်ကေ့ချ်များ (Coin Packages)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ZippyText
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                CoinPackageItem(
                    name = "Starter Creator",
                    coins = "1,000 Coins",
                    price = "3,000 MMK",
                    isPopular = false
                )
                CoinPackageItem(
                    name = "Pro Creator Package",
                    coins = "5,000 Coins (+1,000 Bonus)",
                    price = "12,000 MMK",
                    isPopular = true
                )
                CoinPackageItem(
                    name = "Studio VIP Unlimited",
                    coins = "15,000 Coins (+5,000 Bonus)",
                    price = "30,000 MMK",
                    isPopular = false
                )
            }
        }

        // Transaction & Usage History
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ZippyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, contentDescription = null, tint = ZippyGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "အသုံးပြုမှု မှတ်တမ်း (Usage History)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ZippyGoldLight
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    UsageRow(title = "Gemini AI Script Generation", time = "Today, 12:40 PM", cost = "-50 Coins")
                    UsageRow(title = "Edge TTS Batch Audio (Thiha)", time = "Today, 12:45 PM", cost = "-100 Coins")
                    UsageRow(title = "Daily Free Login Reward", time = "Today, 08:00 AM", cost = "+200 Coins", isPositive = true)
                    UsageRow(title = "1080p FHD Anti-Copyright Render", time = "Yesterday", cost = "-150 Coins")
                }
            }
        }
    }
}

@Composable
fun CoinPackageItem(
    name: String,
    coins: String,
    price: String,
    isPopular: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isPopular) ZippyCard2 else ZippyCard
        ),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPopular) ZippyGold else ZippyLine
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isPopular) ZippyGold else ZippyText
                        )
                    )
                    if (isPopular) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = ZippyGold,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "POPULAR",
                                color = ZippyOnGold,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = coins,
                    style = MaterialTheme.typography.bodySmall.copy(color = ZippyOk)
                )
            }

            Surface(
                color = ZippyBg2,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ZippyLine2)
            ) {
                Text(
                    text = price,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = ZippyGoldLight,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun UsageRow(
    title: String,
    time: String,
    cost: String,
    isPositive: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = ZippyText, style = MaterialTheme.typography.bodyMedium)
            Text(text = time, color = ZippyDim, style = MaterialTheme.typography.bodySmall)
        }
        Text(
            text = cost,
            color = if (isPositive) ZippyOk else ZippyWarn,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
