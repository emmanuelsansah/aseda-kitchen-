package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MenuItemEntity
import com.example.data.OrderEntity
import com.example.ui.components.FoodImage
import com.example.ui.theme.AsedaBlackBg
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    menuItems: List<MenuItemEntity>,
    orders: List<OrderEntity>,
    onAddFoodClick: () -> Unit,
    onEditFoodClick: (MenuItemEntity) -> Unit,
    onToggleAvailability: (MenuItemEntity) -> Unit,
    onDeleteFoodClick: (MenuItemEntity) -> Unit,
    onDeleteOrderClick: (Int) -> Unit,
    onLogoutClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    val totalItems = menuItems.size
    val availableCount = menuItems.count { it.isAvailable }
    val totalOrdersCount = orders.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Admin Header
        Surface(
            color = AsedaBlackBg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = AsedaRedPrimary,
                            shape = CircleShape,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("A", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Admin Control Dashboard",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Authorized Access (Password: Asedakitchen)",
                        fontSize = 11.sp,
                        color = AsedaOrangeSecondary
                    )
                }

                IconButton(
                    onClick = onLogoutClick,
                    modifier = Modifier.testTag("admin_logout_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.Red
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick Stats Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(
                icon = Icons.Default.Fastfood,
                title = "Total Dishes",
                value = "$totalItems",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.CheckCircle,
                title = "In Stock",
                value = "$availableCount",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                icon = Icons.Default.ShoppingBag,
                title = "App Orders",
                value = "$totalOrdersCount",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tabs (Manage Menu | Customer Orders)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = AsedaRedPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = AsedaRedPrimary
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ListAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Manage Menu (${menuItems.size})", fontWeight = FontWeight.Bold)
                    }
                },
                modifier = Modifier.testTag("admin_tab_menu")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Orders Log (${orders.size})", fontWeight = FontWeight.Bold)
                    }
                },
                modifier = Modifier.testTag("admin_tab_orders")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == 0) {
            // Manage Menu Tab Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = onAddFoodClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AsedaRedPrimary),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_add_food_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Upload / Add New Food Dish", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                menuItems.forEach { menuItem ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .testTag("admin_food_item_${menuItem.id}"),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FoodImage(
                                drawableResName = menuItem.drawableResName,
                                contentDescription = menuItem.name,
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(Color.Gray, RoundedCornerShape(8.dp))
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = menuItem.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${menuItem.origin} • ${menuItem.category}",
                                    fontSize = 11.sp,
                                    color = AsedaOrangeSecondary
                                )
                                Text(
                                    text = "GH₵ ${"%.2f".format(menuItem.priceGhs)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AsedaRedPrimary
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Row {
                                    // Toggle Stock Availability
                                    IconButton(
                                        onClick = { onToggleAvailability(menuItem) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (menuItem.isAvailable) Icons.Default.CheckCircle else Icons.Default.RemoveCircle,
                                            contentDescription = "Toggle Stock",
                                            tint = if (menuItem.isAvailable) Color(0xFF2E7D32) else Color.Red
                                        )
                                    }

                                    // Edit Item
                                    IconButton(
                                        onClick = { onEditFoodClick(menuItem) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = AsedaOrangeSecondary
                                        )
                                    }

                                    // Delete Item
                                    IconButton(
                                        onClick = { onDeleteFoodClick(menuItem) },
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.Red
                                        )
                                    }
                                }

                                Surface(
                                    color = if (menuItem.isAvailable) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (menuItem.isAvailable) "IN STOCK" else "OUT OF STOCK",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (menuItem.isAvailable) Color(0xFF2E7D32) else Color.Red,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Customer Orders Log Tab Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (orders.isEmpty()) {
                    Text(
                        text = "No customer orders recorded yet.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 30.dp)
                    )
                } else {
                    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    orders.forEach { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = order.customerName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "GH₵ ${"%.2f".format(order.totalPriceGhs)}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AsedaRedPrimary
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "📱 Phone: ${order.customerPhone}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "📍 Address: ${order.deliveryAddress}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "Order: ${order.quantity}x ${order.itemName}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (order.notes.isNotBlank()) {
                                            Text(
                                                text = "Notes: ${order.notes}",
                                                fontSize = 12.sp,
                                                color = AsedaOrangeSecondary
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = dateFormat.format(Date(order.timestamp)),
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )

                                    IconButton(
                                        onClick = { onDeleteOrderClick(order.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete order", tint = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = AsedaRedPrimary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
            Text(text = title, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
