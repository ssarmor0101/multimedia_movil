package com.example.ud1_aplicacion_sencilla.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ud1_aplicacion_sencilla.R

@Composable
fun BuildButton(
    enabled: Boolean,
    onClick: () -> Unit,
    text: String
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(20)
    ) {
        Text(text)
    }
}