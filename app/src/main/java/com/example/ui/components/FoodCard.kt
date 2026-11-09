package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MenuItemEntity
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary
import com.example.ui.theme.GhanaGold
import com.example.ui.theme.NigeriaGreen

@Composable
fun FoodCard(
    menuItem: MenuItemEntity,
    onOrderClick: (MenuItemEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("food_card_${menuItem.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                FoodImage(
                    drawableResName = menuItem.drawableResName,
                    contentDescription = menuItem.name,
                    modifier = Modifier.fillMaxWidth()
                )

                // Origin Badge (Ghanaian / Nigerian / Both)
                val (badgeText, badgeBg, badgeTextColor) = when (menuItem.origin) {
                    "Ghanaian" -> Triple("🇬🇭 Ghanaian Dish", GhanaGold, Color.Black)
                    "Nigerian" -> Triple("🇳🇬 Nigerian Dish", NigeriaGreen, Color.White)
                    else -> Triple("🌍 West African Special", AsedaOrangeSecondary, Color.White)
                }

                Surface(
                    color = badgeBg,
                    shape = RoundedCornerShape(bottomEnd = 12.dp, topStart = 16.dp),
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeTextColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                if (menuItem.isFeatured) {
                    Surface(
                        color = AsedaRedPrimary,
                        shape = RoundedCornerShape(bottomStart = 12.dp),
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Text(
                            text = "⭐ FEATURED",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = menuItem.name,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GH₵ ${"%.2f".format(menuItem.priceGhs)}",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AsedaRedPrimary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = menuItem.category,
                    fontSize = 12.sp,
                    color = AsedaOrangeSecondary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = menuItem.description,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Availability Status
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (menuItem.isAvailable) Color(0xFF4CAF50) else Color.Red)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (menuItem.isAvailable) "Available" else "Out of Stock",
                            fontSize = 12.sp,
                            color = if (menuItem.isAvailable) Color(0xFF2E7D32) else Color.Red,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = { onOrderClick(menuItem) },
                        enabled = menuItem.isAvailable,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AsedaRedPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.testTag("order_button_${menuItem.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Order",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Order Now",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
