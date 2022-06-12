package com.example.jetsplitbill.widgets

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageVector: ImageVector,
    contentDescription: String,
    shape: Shape,
    colors: IconButtonColors
) {

        FilledIconButton(
            modifier = modifier,
            onClick = { onClick.invoke() },
            shape = shape,
            colors = colors
        ) {
            Icon(imageVector = imageVector, contentDescription = contentDescription, modifier = modifier)
        }
    }
