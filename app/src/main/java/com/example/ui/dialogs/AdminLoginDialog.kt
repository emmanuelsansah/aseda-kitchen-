package com.example.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AsedaOrangeSecondary
import com.example.ui.theme.AsedaRedPrimary

@Composable
fun AdminLoginDialog(
    passwordInput: String,
    isError: Boolean,
    onPasswordChange: (String) -> Unit,
    onLoginSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "ASEDA Kitchen Admin",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column {
                Text(
                    text = "Please enter the admin access password to control, edit, and upload food items.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = onPasswordChange,
                    label = { Text("Admin Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = AsedaRedPrimary) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    isError = isError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_password_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AsedaRedPrimary,
                        focusedLabelColor = AsedaRedPrimary
                    )
                )
                if (isError) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Incorrect password! Try again.",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onLoginSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = AsedaRedPrimary),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("admin_login_submit")
            ) {
                Text("Unlock Dashboard", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = AsedaOrangeSecondary)
            }
        }
    )
}
