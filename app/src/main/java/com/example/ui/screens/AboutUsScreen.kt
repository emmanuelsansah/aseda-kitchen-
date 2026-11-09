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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.FoodImage
import com.example.ui.theme.AsedaBlackBg
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary

@Composable
fun AboutUsScreen(
    onExploreMenuClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Banner Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            FoodImage(
                drawableResName = "img_hero_banner",
                contentDescription = "About ASEDA Kitchen",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Surface(
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        color = AsedaRedPrimary,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = " OUR STORY & HERITAGE ",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "About ASEDA Kitchen",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Bridging Ghanaian & Nigerian Culinary Excellence",
                        fontSize = 13.sp,
                        color = AsedaOrangeSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Main Narrative Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Welcome to ASEDA Kitchen!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsedaRedPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "At ASEDA Kitchen, food is more than sustenance—it is a celebration of West African culture, warmth, and togetherness. Founded by Chef Aseda, our kitchen was built on a passionate vision: to serve authentic Ghanaian delicacies alongside beloved Nigerian comfort dishes under one roof.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 22.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Whether you crave piping hot Banku with freshly grilled Tilapia and spicy Shito, smooth Pounded Yam with rich Egusi soup, authentic Waakye Supreme, or spicy Nigerian Suya skewers, ASEDA Kitchen brings home straight to your plate.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Our Four Culinary Pillars",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 4 Pillars Grid
            PillarItem(
                icon = Icons.Default.Restaurant,
                title = "100% Authentic Recipes",
                description = "We source genuine herbs and spices including Ghanaian Shito, Nigerian Yaji pepper, locust beans (Iru), scent leaves, and authentic dried smoked fish."
            )

            Spacer(modifier = Modifier.height(10.dp))

            PillarItem(
                icon = Icons.Default.LocalDining,
                title = "Fresh Daily Preparation",
                description = "Our Banku, Fufu, Waakye, Konkonte, Egusi, and Light Soups are pounded and prepared fresh every single morning."
            )

            Spacer(modifier = Modifier.height(10.dp))

            PillarItem(
                icon = Icons.Default.CleanHands,
                title = "Strict Hygiene Standards",
                description = "We maintain ultra-clean kitchen environments, hygienic food handling, and safe eco-friendly packaging for all orders."
            )

            Spacer(modifier = Modifier.height(10.dp))

            PillarItem(
                icon = Icons.Default.Favorite,
                title = "Direct WhatsApp Delivery (0551464715)",
                description = "Order seamlessly straight from our app to our WhatsApp line! Fast delivery to your doorstep."
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Founder Showcase Card
            Card(
                colors = CardDefaults.cardColors(containerColor = AsedaBlackBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AsedaOrangeSecondary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"My goal is to make everyone feel the love and authentic warmth of Ghanaian and Nigerian cooking with every single bite.\"",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "— Chef Aseda (Founder, ASEDA Kitchen)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AsedaRedPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onExploreMenuClick,
                        colors = ButtonDefaults.buttonColors(containerColor = AsedaRedPrimary),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Explore Our Menu & Order", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun PillarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                color = AsedaRedPrimary,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp
                )
            }
        }
    }
}
