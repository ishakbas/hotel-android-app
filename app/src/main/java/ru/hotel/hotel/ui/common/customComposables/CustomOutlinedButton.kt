package ru.hotel.hotel.ui.common.customComposables

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CustomOutlinedButton(
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClickAction: () -> Unit
) {
    Box(modifier = modifier) {
        OutlinedButton(enabled = enabled, onClick = { onClickAction() }) {
            Text(text = label)
        }
    }
}
