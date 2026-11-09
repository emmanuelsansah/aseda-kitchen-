package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScreenState
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AsedaTopBar(
    currentScreen: ScreenState,
    isAdminAuthenticated: Boolean,
    orderCount: Int,
    onOrderHistoryClick: () -> Unit,
    onAdminClick: () -> Unit
) {
    val context = LocalContext.current
    var titleTapCount by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = AsedaRedPrimary,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Secret 3-Tap or Long-Press Gesture on Brand Logo Title
            Column(
                modifier = Modifier
                    .combinedClickable(
                        onClick = {
                            titleTapCount++
                            if (titleTapCount >= 3) {
                                titleTapCount = 0
                                onAdminClick()
                            }
                        },
                        onLongClick = {
                            titleTapCount = 0
                            onAdminClick()
                        }
                    )
                    .testTag("secret_admin_logo_trigger")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "ASEDA KITCHEN",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = " 🍲",
                        fontSize = 18.sp
                    )
                    if (isAdminAuthenticated) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFF4CAF50),
                            shape = CircleShape,
                            modifier = Modifier.size(8.dp)
                        ) {}
                    }
                }
                Text(
                    text = "Authentic Ghanaian & Nigerian Cuisine",
                    fontSize = 11.sp,
                    color = AsedaOrangeSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // My Orders Button with Badge
                IconButton(
                    onClick = onOrderHistoryClick,
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .testTag("my_orders_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (orderCount > 0) {
                                Badge(
                                    containerColor = AsedaOrangeSecondary,
                                    contentColor = Color.White
                                ) {
                                    Text("$orderCount", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Order History",
                            tint = Color.White
                        )
                    }
                }

                // If Admin is unlocked, show active Admin Panel button
                if (isAdminAuthenticated) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onAdminClick,
                        modifier = Modifier
                            .background(
                                color = AsedaOrangeSecondary,
                                shape = CircleShape
                            )
                            .testTag("admin_unlocked_dashboard_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Admin Unlocked",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
