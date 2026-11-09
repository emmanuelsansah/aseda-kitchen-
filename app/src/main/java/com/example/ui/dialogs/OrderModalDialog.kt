package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.MenuItemEntity
import com.example.ui.OrderFormState
import com.example.ui.components.FoodImage
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary
import com.example.ui.theme.WhatsAppGreen

@Composable
fun OrderModalDialog(
    item: MenuItemEntity,
    formState: OrderFormState,
    onFormUpdate: (String, String, String, Int, String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val totalPrice = item.priceGhs * formState.quantity

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Place Food Order",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Direct delivery via WhatsApp DM (0551464715)",
                            fontSize = 11.sp,
                            color = AsedaOrangeSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Item Summary Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FoodImage(
                            drawableResName = item.drawableResName,
                            contentDescription = item.name,
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color.Gray, RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${item.origin} • ${item.category}",
                                fontSize = 11.sp,
                                color = AsedaOrangeSecondary
                            )
                            Text(
                                text = "GH₵ ${"%.2f".format(item.priceGhs)} each",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = AsedaRedPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Inputs
                OutlinedTextField(
                    value = formState.customerName,
                    onValueChange = {
                        onFormUpdate(it, formState.customerPhone, formState.deliveryAddress, formState.quantity, formState.notes)
                    },
                    label = { Text("Your Full Name *") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = AsedaRedPrimary) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_input_name"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AsedaRedPrimary,
                        focusedLabelColor = AsedaRedPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = formState.customerPhone,
                    onValueChange = {
                        onFormUpdate(formState.customerName, it, formState.deliveryAddress, formState.quantity, formState.notes)
                    },
                    label = { Text("Phone Number * (e.g., 0551464715)") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = AsedaRedPrimary) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_input_phone"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AsedaRedPrimary,
                        focusedLabelColor = AsedaRedPrimary
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = formState.deliveryAddress,
                    onValueChange = {
                        onFormUpdate(formState.customerName, formState.customerPhone, it, formState.quantity, formState.notes)
                    },
                    label = { Text("Delivery Location / Address *") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = AsedaRedPrimary) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("order_input_address"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AsedaRedPrimary,
                        focusedLabelColor = AsedaRedPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quantity Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quantity:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (formState.quantity > 1) {
                                    onFormUpdate(
                                        formState.customerName,
                                        formState.customerPhone,
                                        formState.deliveryAddress,
                                        formState.quantity - 1,
                                        formState.notes
                                    )
                                }
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = AsedaRedPrimary)
                        }

                        Text(
                            text = "${formState.quantity}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        IconButton(
                            onClick = {
                                onFormUpdate(
                                    formState.customerName,
                                    formState.customerPhone,
                                    formState.deliveryAddress,
                                    formState.quantity + 1,
                                    formState.notes
                                )
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase", tint = AsedaRedPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = formState.notes,
                    onValueChange = {
                        onFormUpdate(formState.customerName, formState.customerPhone, formState.deliveryAddress, formState.quantity, it)
                    },
                    label = { Text("Special Cooking Instructions / Notes") },
                    placeholder = { Text("e.g. Extra shito, mild pepper, extra fish") },
                    leadingIcon = { Icon(Icons.Default.Notes, contentDescription = null, tint = AsedaOrangeSecondary) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AsedaOrangeSecondary,
                        focusedLabelColor = AsedaOrangeSecondary
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Divider()

                Spacer(modifier = Modifier.height(12.dp))

                // Total Price Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Order Amount:",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "GH₵ ${"%.2f".format(totalPrice)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = AsedaRedPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_order_whatsapp_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WhatsAppGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "WhatsApp",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Order via WhatsApp (0551464715)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
