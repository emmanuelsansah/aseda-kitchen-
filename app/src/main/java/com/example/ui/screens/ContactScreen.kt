package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.ui.theme.AsedaBlackBg
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary
import com.example.ui.theme.WhatsAppGreen
import java.net.URLEncoder

@Composable
fun ContactScreen() {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Contact Banner
        Surface(
            color = AsedaRedPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Contact ASEDA Kitchen",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "We are always ready to serve you fresh Ghanaian & Nigerian food!",
                    fontSize = 13.sp,
                    color = Color(0xFFFFDADA)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // WhatsApp Direct Action Card
            Card(
                colors = CardDefaults.cardColors(containerColor = AsedaBlackBg),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = WhatsAppGreen,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "WhatsApp",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "WhatsApp Hotline",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "0551464715 / +233 55 146 4715",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = WhatsAppGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Click below to open a direct WhatsApp chat for instant food orders, event catering, or table reservations.",
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            openDirectWhatsApp(context, "Hello ASEDA Kitchen! I would like to make an inquiry / order.")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WhatsAppGreen),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("contact_whatsapp_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Chat on WhatsApp (0551464715)", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contact Info List
            Text(
                text = "Restaurant Location & Hours",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            com.example.ui.components.AsedaGoogleMapCard()

            Spacer(modifier = Modifier.height(14.dp))

            ContactInfoRow(
                icon = Icons.Default.LocationOn,
                title = "Address & Location",
                detail = "ASEDA Kitchen, Ring Road Central, Near Osu / East Legon Junction, Accra, Ghana"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactInfoRow(
                icon = Icons.Default.AccessTime,
                title = "Opening Hours",
                detail = "Monday to Sunday: 8:00 AM – 10:00 PM (Everyday)"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactInfoRow(
                icon = Icons.Default.Phone,
                title = "Call / WhatsApp Line",
                detail = "0551464715"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ContactInfoRow(
                icon = Icons.Default.Email,
                title = "Email Support",
                detail = "asedakitchen.gh@gmail.com"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Inquiry Form Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Send Us a Message / Inquiry",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AsedaRedPrimary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("contact_input_name"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsedaRedPrimary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = AsedaRedPrimary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("contact_input_phone"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsedaRedPrimary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject (e.g. Catering, Reservation)") },
                        leadingIcon = { Icon(Icons.Default.Message, contentDescription = null, tint = AsedaOrangeSecondary) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsedaOrangeSecondary)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Your Message") },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("contact_input_message"),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsedaRedPrimary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (name.isBlank() || message.isBlank()) {
                                Toast.makeText(context, "Please enter your name and message.", Toast.LENGTH_SHORT).show()
                            } else {
                                val msg = "Inquiry from $name ($phone)\nSubject: $subject\nMessage: $message"
                                openDirectWhatsApp(context, msg)
                                name = ""
                                phone = ""
                                subject = ""
                                message = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AsedaRedPrimary),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth().testTag("contact_submit_button")
                    ) {
                        Text("Send Message via WhatsApp", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
private fun ContactInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    detail: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = AsedaRedPrimary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AsedaOrangeSecondary
                )
                Text(
                    text = detail,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

private fun openDirectWhatsApp(context: Context, textMsg: String) {
    try {
        val phoneNumber = "233551464715"
        val encodedMessage = URLEncoder.encode(textMsg, "UTF-8")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phoneNumber?text=$encodedMessage"))
        context.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(context, "Could not open WhatsApp: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
    }
}
