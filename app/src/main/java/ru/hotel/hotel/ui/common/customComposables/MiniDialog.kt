package ru.hotel.hotel.ui.common.customComposables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NetworkWifi3Bar
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun MinimalDialog(
    fieldValue: String,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextField(
                    label = "Ip адрес",
                    value = fieldValue,
                    leadingIcon = Icons.Filled.NetworkWifi3Bar,
                    onValueChange = onValueChange
                )
                CustomOutlinedButton(label = "Ok", onClickAction = onConfirmRequest)
            }
        }
    }
}