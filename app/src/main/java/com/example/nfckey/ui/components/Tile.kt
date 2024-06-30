package com.example.nfckey.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Tile(
    text: String,
    drawable: ImageVector? = null,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    shadowElevation: Dp = 0.dp,
    tonalElevation: Dp = 0.dp,
    shape: Shape = MaterialTheme.shapes.small,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .heightIn(min = 50.dp)
            .widthIn(min = 50.dp)
            .then(modifier)
            .clickable { onClick.invoke() },
        shape,
        backgroundColor,
        contentColor,
        tonalElevation,
        shadowElevation
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(all = 10.dp)
        ) {
            Spacer(Modifier.weight(0.2f))
            Column(Modifier.weight(1f)) {
                drawable?.let { vector ->
                    val preferableHeight = LocalConfiguration.current.screenWidthDp * 0.1

                    Image(
                        imageVector = vector,
                        contentDescription = null,
                        modifier = Modifier.size(preferableHeight.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    )
                }
                Text(text, style = textStyle)
            }

        }
    }
}

@Preview
@Composable
fun PreviewTile() {
    Tile(
        "This is a tile",
        drawable = Icons.Outlined.Info,
        modifier = Modifier,
    ) {}
}