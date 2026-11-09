package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScreenState
import com.example.ui.theme.AsedaBlackBg
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary

import androidx.compose.material.icons.filled.AutoAwesome

@Composable
fun AsedaBottomNav(
    currentScreen: ScreenState,
    onScreenSelected: (ScreenState) -> Unit
) {
    NavigationBar(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = AsedaBlackBg,
        tonalElevation = 8.dp
    ) {
        val navItems = listOf(
            Triple(ScreenState.HOME, "Home", Icons.Default.Home),
            Triple(ScreenState.MENU, "Menu", Icons.Default.RestaurantMenu),
            Triple(ScreenState.CHEF_AI, "Chef AI", Icons.Default.AutoAwesome),
            Triple(ScreenState.ABOUT, "About Us", Icons.Default.Info),
            Triple(ScreenState.CONTACT, "Contact", Icons.Default.Phone)
        )

        navItems.forEach { (screen, label, icon) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onScreenSelected(screen) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = AsedaOrangeSecondary,
                    indicatorColor = AsedaRedPrimary,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray
                ),
                modifier = Modifier.testTag("nav_tab_${label.lowercase().replace(" ", "_")}")
            )
        }
    }
}
