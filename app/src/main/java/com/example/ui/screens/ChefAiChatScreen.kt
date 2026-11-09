package com.example.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.MessageSender
import com.example.ui.theme.AsedaBlackBg
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.content
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChefAiChatScreen(
    onExploreMenuClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputPrompt by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }

    val initialMessages = remember {
        mutableStateListOf(
            ChatMessage(
                sender = MessageSender.CHEF_AI,
                text = "Akwaaba & Welcome! 👨‍🍳 I am Chef Aseda AI, your Ghanaian & Nigerian culinary concierge. Ask me anything about our dishes—Banku with Tilapia, Fufu, Waakye, Egusi, Suya, Konkonte, or pepper levels!"
            )
        )
    }

    // Lazy init generative model
    val generativeModel = remember {
        try {
            Firebase.ai.generativeModel(
                modelName = "gemini-3.8-flash",
                systemInstruction = content {
                    text(
                        """
                        You are Chef Aseda, the warm, hospitable, expert culinary AI concierge for ASEDA Kitchen restaurant in Ghana.
                        You specialize in authentic Ghanaian (Banku with Tilapia, Fufu with Light Soup, Waakye Supreme, Konkonte/Abetee, Jollof) and Nigerian (Egusi Soup with Pounded Yam, Suya skewers, Goat Meat Pepper Soup, Efo Riro) cuisine.
                        Help customers choose meals based on their taste, spicy preference, or dietary requirements.
                        Keep responses warm, polite, appetizing, concise, and helpful.
                        Remind them that all dishes can be ordered directly via WhatsApp at 0551464715!
                        """.trimIndent()
                    )
                }
            )
        } catch (e: Exception) {
            Log.e("ChefAiChatScreen", "Failed to initialize Gemini model", e)
            null
        }
    }

    // Scroll to bottom when messages update
    LaunchedEffect(initialMessages.size) {
        if (initialMessages.isNotEmpty()) {
            listState.animateScrollToItem(initialMessages.size - 1)
        }
    }

    fun sendMessage(promptText: String) {
        if (promptText.isBlank() || isGenerating) return

        val userMsg = ChatMessage(sender = MessageSender.USER, text = promptText)
        initialMessages.add(userMsg)
        inputPrompt = ""
        isGenerating = true

        scope.launch {
            try {
                // Build history for multi-turn conversation
                val historyContents = initialMessages.dropLast(1).map { msg ->
                    content(if (msg.sender == MessageSender.USER) "user" else "model") {
                        text(msg.text)
                    }
                }

                val chatSession = generativeModel?.startChat(history = historyContents)
                val response = chatSession?.sendMessage(promptText)
                val replyText = response?.text ?: "I am delighted to recommend our Banku with Tilapia, Fufu with Goat Meat Light Soup, or Nigerian Egusi with Pounded Yam! You can order directly via WhatsApp at 0551464715."

                initialMessages.add(ChatMessage(sender = MessageSender.CHEF_AI, text = replyText))
            } catch (e: Exception) {
                Log.e("ChefAiChatScreen", "Gemini AI generation failed", e)
                val fallbackText = "Chef Aseda recommends our customer favorite Banku with Tilapia & Shito! Tap Order Now to place your order via WhatsApp at 0551464715."
                initialMessages.add(ChatMessage(sender = MessageSender.CHEF_AI, text = fallbackText))
            } finally {
                isGenerating = false
            }
        }
    }

    val suggestedPrompts = listOf(
        "🍲 What is Banku with Tilapia?",
        "🇳🇬 Recommend a spicy Nigerian dish",
        "🌾 Is Fufu gluten-free?",
        "🌶️ Which meal has mild pepper?",
        "🍗 Tell me about Waakye Supreme"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // AI Chat Header Banner
        Surface(
            color = AsedaBlackBg,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = AsedaRedPrimary,
                    shape = CircleShape,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Chef AI",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Chef Aseda AI Concierge",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = AsedaOrangeSecondary,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Gemini AI",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Ask anything about Ghanaian & Nigerian dishes!",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }
        }

        // Message Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(initialMessages, key = { it.id }) { msg ->
                ChatMessageBubble(message = msg)
            }

            if (isGenerating) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            color = AsedaRedPrimary,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Chef Aseda is thinking...",
                            fontSize = 13.sp,
                            color = AsedaOrangeSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Suggested Prompt Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            contentPadding = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(suggestedPrompts) { prompt ->
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.clickable { sendMessage(prompt) }
                ) {
                    Text(
                        text = prompt,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Input Field Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputPrompt,
                    onValueChange = { inputPrompt = it },
                    placeholder = { Text("Ask Chef Aseda about food, spices, recommendations...") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chef_ai_input"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AsedaRedPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { sendMessage(inputPrompt) },
                    enabled = inputPrompt.isNotBlank() && !isGenerating,
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (inputPrompt.isNotBlank() && !isGenerating) AsedaRedPrimary else Color.Gray,
                            shape = CircleShape
                        )
                        .testTag("chef_ai_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatMessageBubble(message: ChatMessage) {
    val isUser = message.sender == MessageSender.USER
    val timeFormat = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                color = AsedaRedPrimary,
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Restaurant,
                        contentDescription = "Chef AI",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) AsedaRedPrimary else MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            modifier = Modifier.fillMaxWidth(0.82f)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = message.text,
                    fontSize = 14.sp,
                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = timeFormat.format(Date(message.timestamp)),
                    fontSize = 10.sp,
                    color = if (isUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = AsedaOrangeSecondary,
                shape = CircleShape,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
