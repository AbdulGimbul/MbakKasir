package features.cashier_role

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import features.cashier_role.sales.presentation.entry_sales.EntrySalesScreen
import mbakkasir.composeapp.generated.resources.Poppins_Regular
import mbakkasir.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font
import ui.navigation.tab.cashier_role.HistoryTab
import ui.navigation.tab.cashier_role.HomeTab
import ui.navigation.tab.cashier_role.ProfileTab
import ui.navigation.tab.cashier_role.SalesTab
import ui.theme.icon
import ui.theme.primary

class CashierScreen() : Screen {

    @Composable
    override fun Content() {
        var isFabVisible by remember { mutableStateOf(true) }
        var isBottomBarVisible by remember { mutableStateOf(true) }

        TabNavigator(HomeTab) {
            val currentTab = LocalTabNavigator.current

            isBottomBarVisible = when (currentTab.current) {
                is SalesTab -> true  // Show bottom bar when navigating to SalesTab
                else -> showBottomBar(currentTab.current)
            }

            Scaffold(
                bottomBar = {
                    AnimatedVisibility(
                        visible = isBottomBarVisible,
                        enter = slideInVertically { height -> height },
                        exit = slideOutVertically { height -> height }
                    ) {
                        BottomAppBar(
                            containerColor = Color.White
                        ) {
                            TabNavigationItem(HomeTab)
                            TabNavigationItem(SalesTab(
                                updateFabVisibility = { isFabVisible = it },
                                updateBottomBarVisibility = { isBottomBarVisible = it }
                            ))
                            TabNavigationItem(HistoryTab)
                            TabNavigationItem(ProfileTab)
                        }
                    }
                },
                floatingActionButton = {
                    if (currentTab.current is SalesTab && isFabVisible) {
                        FloatingActionButton(
                            onClick = {
                                (currentTab.current as SalesTab)._navigator?.push(EntrySalesScreen())
                            },
                            shape = CircleShape,
                            containerColor = primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White
                            )
                        }
                    }
                },
                modifier = Modifier.statusBarsPadding().navigationBarsPadding()
            ) {
                CurrentTab()
            }
        }
    }
}

private fun showBottomBar(tab: Tab): Boolean {
    return tab is HomeTab || tab is SalesTab || tab is HistoryTab || tab is ProfileTab
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = {
            Text(
                tab.options.title,
                fontFamily = FontFamily(Font(Res.font.Poppins_Regular))
            )
        },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = primary,
            selectedTextColor = primary,
            indicatorColor = Color.White,
            unselectedIconColor = icon,
            unselectedTextColor = icon
        ),
    )
}