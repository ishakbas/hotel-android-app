package ru.hotel.hotel.ui.common.customComposables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    label: String,
    value: String,
    password: Boolean = false,
    leadingIcon: ImageVector,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorText: String = "",
) {
    OutlinedTextField(
        label = { Text(text = label) },
        value = value,
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null
            )
        },
        onValueChange = onValueChange,
        shape = RoundedCornerShape(50.dp),
        singleLine = true,
        visualTransformation = if (password) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = if (password) {
            KeyboardOptions(keyboardType = KeyboardType.Password)
        } else {
            KeyboardOptions.Default
        },
        supportingText = {
            if (isError) {
                ErrorTextInputField(text = errorText)
            }
        },
        modifier = modifier.widthIn(min = 250.dp, max = 600.dp)
    )
}
