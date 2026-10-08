package com.example.ud1_aplicacion_sencilla.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BuildCard(
    modifier: Modifier = Modifier.size(150.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            content();
        }
    }
}

@Composable
fun BuildCardSingleText(text: String) {
    BuildCard() {
        Text(
            fontSize = 20.sp,
            text = text
        );
    }
}