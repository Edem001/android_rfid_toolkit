@file:OptIn(ExperimentalStdlibApi::class)

package com.example.nfckey.ui.screens.read

import android.nfc.Tag
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.nfckey.ApplicationViewModel

@Composable
fun ReadPage(mainViewModel: ApplicationViewModel, navController: NavController = rememberNavController()){

    val tag: Tag? by mainViewModel.tagState.collectAsState()
    val windowMode = currentWindowAdaptiveInfo().windowSizeClass

    Scaffold(
        containerColor = if (tag != null) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.errorContainer
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
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
                Column {
                    val records by mainViewModel.records
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 10.dp),
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxSize(),
                    ) {
                        items(records.size, { index -> index } ){ index ->
                            Text("Sector #$index", modifier = Modifier.fillMaxWidth())
                            SelectionContainer {
                                Text(
                                    text = records[index].toHexString(
                                        HexFormat {
                                            bytes {
                                                byteSeparator = ":"
                                            }
                                            this.upperCase = true
                                        }
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        item {
                            val encodedString = buildString {
                                records.forEach {
                                    append(it.toString(Charsets.UTF_8))
                                }
                            }

                            Text("String contents", style = MaterialTheme.typography.headlineSmall)
                            Text(text = encodedString, style = MaterialTheme.typography.bodySmall)
                        }
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