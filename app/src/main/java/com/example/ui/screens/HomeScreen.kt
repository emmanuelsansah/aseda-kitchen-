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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MenuItemEntity
import com.example.ui.ScreenState
import com.example.ui.components.FoodCard
import com.example.ui.components.FoodImage
import com.example.ui.theme.AsedaBlackBg
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary

@Composable
fun HomeScreen(
    featuredItems: List<MenuItemEntity>,
    onExploreMenuClick: () -> Unit,
    onChefAiClick: () -> Unit,
    onOrderClick: (MenuItemEntity) -> Unit
) {
    var selectedOriginFilter by remember { mutableStateOf("All") }

    val displayedItems = featuredItems.filter { item ->
        selectedOriginFilter == "All" || item.origin.equals(selectedOriginFilter, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Section Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            FoodImage(
                drawableResName = "img_hero_banner",
                contentDescription = "ASEDA Kitchen Hero Banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Dark gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Hero Text & Action
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Surface(
                    color = AsedaRedPrimary,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = " authentic ghanian & nigerian food ".uppercase(),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Welcome to ASEDA Kitchen",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Text(
                    text = "Banku, Fufu, Waakye, Egusi, Suya & Konkonte prepared with love!",
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onExploreMenuClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AsedaRedPrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.testTag("hero_explore_menu_button")
                    ) {
                        Text("Explore Menu", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }

                    Button(
                        onClick = onChefAiClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AsedaOrangeSecondary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.testTag("hero_ask_chef_ai_button")
                    ) {
                        Text("Ask Chef AI 👨‍🍳", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Chef AI Interactive Banner Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onChefAiClick() },
            colors = CardDefaults.cardColors(containerColor = AsedaBlackBg),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = AsedaRedPrimary,
                    shape = CircleShape,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("👨‍🍳", fontSize = 22.sp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Chef Aseda AI Concierge 🤖",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Need dish recommendations, ingredient details, or pepper guidance? Tap to chat!",
                        fontSize = 11.sp,
                        color = AsedaOrangeSecondary
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Features Bar (Badges)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FeatureBadge(icon = Icons.Default.RestaurantMenu, title = "Authentic Taste", subtitle = "Traditional Recipes")
            FeatureBadge(icon = Icons.Default.DeliveryDining, title = "Fast WhatsApp DM", subtitle = "0551464715")
            FeatureBadge(icon = Icons.Default.Verified, title = "Hygienic Prep", subtitle = "Fresh Ingredients")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title: Featured Delicacies
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Featured Dishes",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Customer favorites in Ghana & Nigeria",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "See All",
                color = AsedaRedPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onExploreMenuClick() }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filter chips (All | 🇬🇭 Ghanaian | 🇳🇬 Nigerian)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "All" to "All Delicacies",
                "Ghanaian" to "🇬🇭 Ghanaian",
                "Nigerian" to "🇳🇬 Nigerian"
            ).forEach { (originKey, label) ->
                val isSelected = selectedOriginFilter == originKey
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedOriginFilter = originKey },
                    label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AsedaRedPrimary,
                        selectedLabelColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Featured Dishes Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (displayedItems.isEmpty()) {
                Text(
                    text = "No featured items found in this category.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                displayedItems.forEach { item ->
                    FoodCard(
                        menuItem = item,
                        onOrderClick = onOrderClick
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Special Highlight Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = AsedaBlackBg)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "HUNGRY FOR REAL HOME-COOKED MEALS?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = AsedaOrangeSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "We serve Banku with Tilapia, Fufu with Light Soup, Waakye Supreme, Egusi Soup & Suya daily!",
                    fontSize = 13.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = onExploreMenuClick,
                    colors = ButtonDefaults.buttonColors(containerColor = AsedaRedPrimary),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Order Now on WhatsApp", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Customer Reviews Carousel
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "What Our Customers Say",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))

            val reviews = listOf(
                Pair("Kwame Mensah (Accra)", "The Banku with hot pepper and Tilapia from ASEDA Kitchen is out of this world! Pure perfection."),
                Pair("Chinedu Eze (Lagos/Legon)", "Authentic Nigerian Egusi and Pounded Yam in Ghana! Fast WhatsApp delivery. Highly recommended."),
                Pair("Akosua Serwaa (Kumasi)", "Their Waakye with Shito and Wele takes me back home. Always fresh and generous portions!")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(reviews) { (reviewer, comment) ->
                    Card(
                        modifier = Modifier
                            .width(260.dp)
                            .height(130.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"$comment\"",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 3
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = reviewer,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AsedaRedPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun FeatureBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Surface(
            color = AsedaRedPrimary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = AsedaRedPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = subtitle,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
