package com.example.ud1_aplicacion_sencilla.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ud1_aplicacion_sencilla.R


@Composable
fun BuildCarousel(content: LazyListScope.() -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        content();
    }
}

@Composable
fun BuildCarouselListaFija() {
    BuildCarousel {
        items(count = 10) { index ->
            BuildCardSingleText("${stringResource(R.string.card)} ${index+1}")
        }
    }
}