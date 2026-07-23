package com.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.FactCheckResult
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ResultDetailScreen
import com.example.ui.screens.SourcesGuideScreen
import com.example.ui.screens.TrendingScreen
import com.example.ui.screens.VerifierScreen
import com.example.ui.theme.BrandPrimary
import com.example.ui.viewmodel.FactCheckViewModel

enum class NavTab(val title: String, val icon: ImageVector, val tag: String) {
    VERIFY("Verificar", Icons.Default.FactCheck, "tab_nav_verify"),
    TRENDING("Em Alta", Icons.Default.LocalFireDepartment, "tab_nav_trending"),
    QUIZ("Quiz", Icons.Default.Psychology, "tab_nav_quiz"),
    HISTORY("Histórico", Icons.Default.History, "tab_nav_history"),
    GUIDE("Guia Fontes", Icons.Default.MenuBook, "tab_nav_guide")
}

@Composable
fun MainAppNavigation(
    viewModel: FactCheckViewModel = viewModel()
) {
    var currentTab by remember { mutableStateOf(NavTab.VERIFY) }
    var viewingDetailResult by remember { mutableStateOf<FactCheckResult?>(null) }

    if (viewingDetailResult != null) {
        ResultDetailScreen(
            result = viewingDetailResult!!,
            onBack = { viewingDetailResult = null },
            onToggleBookmark = { result ->
                viewModel.toggleBookmark(result)
                viewingDetailResult = viewingDetailResult?.copy(isBookmarked = !result.isBookmarked)
            }
        )
    } else {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    NavTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = tab.icon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BrandPrimary,
                                selectedTextColor = BrandPrimary,
                                indicatorColor = BrandPrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag(tab.tag)
                        )
                    }
                }
            }
        ) { innerPadding ->
            val modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

            when (currentTab) {
                NavTab.VERIFY -> VerifierScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { result -> viewingDetailResult = result },
                    modifier = modifier
                )
                NavTab.TRENDING -> TrendingScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { result -> viewingDetailResult = result },
                    modifier = modifier
                )
                NavTab.QUIZ -> QuizScreen(
                    viewModel = viewModel,
                    modifier = modifier
                )
                NavTab.HISTORY -> HistoryScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { result -> viewingDetailResult = result },
                    modifier = modifier
                )
                NavTab.GUIDE -> SourcesGuideScreen(
                    modifier = modifier
                )
            }
        }
    }
}
