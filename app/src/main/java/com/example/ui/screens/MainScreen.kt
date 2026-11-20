package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.RecapViewModel
import com.example.ui.theme.ZippyBg
import com.example.ui.theme.ZippyBg2
import com.example.ui.theme.ZippyCard2
import com.example.ui.theme.ZippyGold
import com.example.ui.theme.ZippyLine
import com.example.ui.theme.ZippyMute
import com.example.ui.theme.ZippyOnGold
import com.example.ui.theme.ZippyText

data class NavTabItem(
    val tab: AppTab,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun MainScreen(
    viewModel: RecapViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    val navItems = listOf(
        NavTabItem(AppTab.HOME, Icons.Default.Home, "nav_home"),
        NavTabItem(AppTab.TOOLS, Icons.Default.Build, "nav_tools"),
        NavTabItem(AppTab.WALLET, Icons.Default.AccountBalanceWallet, "nav_wallet"),
        NavTabItem(AppTab.PROJECTS, Icons.Default.VideoLibrary, "nav_projects"),
        NavTabItem(AppTab.ACCOUNT, Icons.Default.Person, "nav_account")
    )

    if (uiState.isInStudio) {
        RecapStudioScreen(viewModel = viewModel)
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = ZippyBg2,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    navItems.forEach { item ->
                        val isSelected = uiState.currentTab == item.tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.switchTab(item.tab) },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.tab.titleMy,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = item.tab.titleMy,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ZippyOnGold,
                                selectedTextColor = ZippyGold,
                                indicatorColor = ZippyGold,
                                unselectedIconColor = ZippyMute,
                                unselectedTextColor = ZippyMute
                            ),
                            modifier = Modifier.testTag(item.testTag)
                        )
                    }
                }
            },
            containerColor = ZippyBg,
            modifier = modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (uiState.currentTab) {
                    AppTab.HOME -> HomeScreen(viewModel = viewModel)
                    AppTab.TOOLS -> ToolsScreen(viewModel = viewModel)
                    AppTab.WALLET -> WalletScreen(viewModel = viewModel)
                    AppTab.PROJECTS -> ProjectsScreen(viewModel = viewModel)
                    AppTab.ACCOUNT -> AccountScreen(viewModel = viewModel)
                }
            }
        }
    }
}
