@file:OptIn(ExperimentalStdlibApi::class)

package com.example.nfckey.ui.screens.read

import android.nfc.Tag
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.nfckey.ApplicationViewModel
import com.example.nfckey.util.DetailedTag

@Composable
fun ReadPage(mainViewModel: ApplicationViewModel){

    val tag: Tag? by mainViewModel.tagState.collectAsState()
    val detailedInfo: DetailedTag? by mainViewModel.tagNdefState.collectAsState()
    val windowMode = currentWindowAdaptiveInfo().windowSizeClass

    Scaffold { innerPadding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .scrollable(scrollState, orientation = Orientation.Vertical)
                .background(
                    if (tag == null)
                        MaterialTheme.colorScheme.errorContainer
                    else MaterialTheme.colorScheme.background
                ),
        ) {
            Text(
                "Read tag contents",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)
            )

            if (windowMode.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) {
                TagSummary(tag, modifier = Modifier.padding(start = 20.dp))
            }

            Spacer(Modifier.height(10.dp))

            AnimatedVisibility(tag != null) {
                var isKeyTextFieldShown by rememberSaveable { mutableStateOf(false) }
                Button (
                    modifier = Modifier.padding(start = 10.dp),
                    onClick = {
                        isKeyTextFieldShown = !isKeyTextFieldShown
                    }
                ) {
                    Icon(Icons.Outlined.Lock, null)
                    Spacer(Modifier.width(10.dp))
                    Text("Enter custom key")
                }

                var key by rememberSaveable { mutableStateOf("") }
                AnimatedVisibility(isKeyTextFieldShown, modifier = Modifier.padding(horizontal = 10.dp)) {
                    TextField(
                        key,
                        { value -> key = value },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Tag key") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            mainViewModel.setKey(key)
                        })
                    )
                }

                val records by mainViewModel.records
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 10.dp),
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    items(records.size, { index -> index } ){ index ->
                        Text("Sector #$index", modifier = Modifier.fillMaxWidth())
                        Text(
                            text = records[index].toHexString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun TagSummary(tag: Tag?, modifier: Modifier = Modifier) {
    if (tag == null) {
        Text(
            "⚠\uFE0F Scan tag to get basic data",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = modifier
        )
    } else {
        Text(
            "\uD83D\uDEE0\uFE0F Tag data",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            """ID: ${tag.id.toHexString()}
                        |Supported technologies: 
                    """.trimMargin(),
            modifier
        )
        Text(
            tag.techList.joinToString(separator = "\n"),
            style = TextStyle(fontStyle = FontStyle.Italic),
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun PreviewReadPage(){
    val fakeViewModel = ApplicationViewModel()
    ReadPage(fakeViewModel)
}

@Preview("Foldable", device = Devices.FOLDABLE)
@Composable
fun PreviewReadPage_Foldable(){
    val fakeViewModel = ApplicationViewModel()
    ReadPage(fakeViewModel)
}

@Preview("Tablet", device = Devices.TABLET)
@Composable
fun PreviewReadPage_Tablet(){
    val fakeViewModel = ApplicationViewModel()
    ReadPage(fakeViewModel)
}