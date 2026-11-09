package com.example.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MenuItemEntity
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFoodDialog(
    editingItem: MenuItemEntity?,
    onSave: (name: String, desc: String, price: Double, category: String, origin: String, drawableRes: String, isFeatured: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(editingItem?.name ?: "") }
    var description by remember { mutableStateOf(editingItem?.description ?: "") }
    var priceText by remember { mutableStateOf(editingItem?.priceGhs?.toString() ?: "") }
    var category by remember { mutableStateOf(editingItem?.category ?: "Ghanaian Specialties") }
    var origin by remember { mutableStateOf(editingItem?.origin ?: "Ghanaian") }
    var drawableResName by remember { mutableStateOf(editingItem?.drawableResName ?: "img_banku_tilapia") }
    var isFeatured by remember { mutableStateOf(editingItem?.isFeatured ?: false) }

    val categories = listOf("Ghanaian Specialties", "Nigerian Favorites", "Rice & Swallows", "Grills & Soups", "Drinks")
    val origins = listOf("Ghanaian", "Nigerian", "Both")
    val imagePresets = listOf(
        Pair("Banku with Tilapia", "img_banku_tilapia"),
        Pair("Fufu with Light Soup", "img_fufu_soup"),
        Pair("Egusi & Pounded Yam", "img_egusi_pounded_yam"),
        Pair("Hero African Feast", "img_hero_banner")
    )

    var categoryExpanded by remember { mutableStateOf(false) }
    var originExpanded by remember { mutableStateOf(false) }
    var imageExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (editingItem == null) "Upload New Food Item" else "Edit Food Item",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Food Name *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("admin_food_name_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsedaRedPrimary)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description *") },
                    modifier = Modifier.fillMaxWidth().testTag("admin_food_desc_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsedaRedPrimary)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Price in GH₵ *") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("admin_food_price_input"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AsedaRedPrimary)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Origin Dropdown
                ExposedDropdownMenuBox(
                    expanded = originExpanded,
                    onExpandedChange = { originExpanded = !originExpanded }
                ) {
                    OutlinedTextField(
                        value = origin,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Cuisine Origin") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = originExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = originExpanded,
                        onDismissRequest = { originExpanded = false }
                    ) {
                        origins.forEach { orig ->
                            DropdownMenuItem(
                                text = { Text(orig) },
                                onClick = {
                                    origin = orig
                                    originExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Image Preset Dropdown
                ExposedDropdownMenuBox(
                    expanded = imageExpanded,
                    onExpandedChange = { imageExpanded = !imageExpanded }
                ) {
                    OutlinedTextField(
                        value = imagePresets.firstOrNull { it.second == drawableResName }?.first ?: drawableResName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Dish Image Visual") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = imageExpanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = imageExpanded,
                        onDismissRequest = { imageExpanded = false }
                    ) {
                        imagePresets.forEach { (label, res) ->
                            DropdownMenuItem(
                                text = { Text(label) },
                                onClick = {
                                    drawableResName = res
                                    imageExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isFeatured,
                        onCheckedChange = { isFeatured = it },
                        colors = CheckboxDefaults.colors(checkedColor = AsedaRedPrimary)
                    )
                    Text("Highlight on Home Page (Featured)")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val parsedPrice = priceText.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank() && parsedPrice > 0) {
                        onSave(name, description, parsedPrice, category, origin, drawableResName, isFeatured)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = AsedaRedPrimary),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("admin_save_food_button")
            ) {
                Text("Save Item", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = AsedaOrangeSecondary)
            }
        }
    )
}
