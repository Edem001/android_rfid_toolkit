package com.example.nfckey.ui.screens.home

import android.nfc.Tag
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.example.nfckey.ApplicationViewModel
import com.example.nfckey.R
import com.example.nfckey.ui.components.Tile
import com.example.nfckey.ui.screens.read.TagSummary

@Composable
fun HomeScreen(viewModel: ApplicationViewModel) {
    val widthType = currentWindowAdaptiveInfo()
        .windowSizeClass
        .windowWidthSizeClass

    when (widthType) {
        WindowWidthSizeClass.COMPACT -> HomeScreenSmallSize(viewModel)
        WindowWidthSizeClass.MEDIUM -> HomeScreenMediumSize(viewModel)
        WindowWidthSizeClass.EXPANDED -> HomeScreenExpandedSize(viewModel)
    }
}

@Composable
fun HomeScreenSmallSize(viewModel: ApplicationViewModel) {
    val tag: Tag? by viewModel.tagState.collectAsState()
    val backGroundColor =
        if (tag == null) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.background

    Scaffold(
        modifier = Modifier,
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(ImageVector.vectorResource(R.drawable.nfc_icon), "Saved tags")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(backGroundColor)
        ) {
            Column(
                Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .fillMaxWidth()
                    .animateContentSize()
                    .align(Alignment.CenterHorizontally),
            ) {
                TagSummary(tag)
            }

            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                columns = StaggeredGridCells.Fixed(2),
            ) {
                item {
                    Tile(
                        "Scan",
                        drawable = Icons.Outlined.Search,
                        textStyle = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp, vertical = 5.dp),
                        shadowElevation = 2.dp,
                        tonalElevation = 2.dp,
                    ) { }
                }
                item {
                    Tile(
                        "Write",
                        drawable = Icons.Outlined.Create,
                        textStyle = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp, vertical = 5.dp),
                        shadowElevation = 2.dp,
                        tonalElevation = 2.dp,
                    ) { }
                }
                item {
                    Tile(
                        "Keys management",
                        drawable = Icons.Outlined.Lock,
                        textStyle = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp, vertical = 5.dp),
                        shadowElevation = 2.dp,
                        tonalElevation = 2.dp,
                    ) { }
                }
                item {
                    Tile(
                        "Clone",
                        drawable = ImageVector.vectorResource(R.drawable.copy_icon),
                        textStyle = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 5.dp, vertical = 5.dp),
                        shadowElevation = 2.dp,
                        tonalElevation = 2.dp,
                    ) { }
                }
            }
        }
    }
}

// TODO: Medium size implementation
@Composable
fun HomeScreenMediumSize(viewModel: ApplicationViewModel){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(ImageVector.vectorResource(R.drawable.nfc_icon), "Saved tags")
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    true,
                    onClick = {},
                    icon = { Icon(Icons.Outlined.Search, null) },
                    label = { Text("Scan") }
                )

                NavigationBarItem(
                    false,
                    onClick = {},
                    icon = { Icon(Icons.Outlined.Create, null) },
                    label = { Text("Write") }
                )

                NavigationBarItem(
                    false,
                    onClick = {},
                    icon = { Icon(Icons.Outlined.Lock, null) },
                    label = { Text("Keys management") }
                )

                NavigationBarItem(
                false,
                onClick = {},
                icon = { Icon(ImageVector.vectorResource(R.drawable.copy_icon), null, modifier = Modifier.size(22.dp)) },
                label = { Text("Clone") }
                )
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {

        }
    }
}

//TODO: Implement expanded layout for tablets
@Composable
fun HomeScreenExpandedSize(viewModel: ApplicationViewModel){
    Scaffold() { innerPadding ->
        Column(Modifier.padding(innerPadding)) {

        }
    }
}

@Preview("Standard size - phone", device = Devices.PHONE)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(ApplicationViewModel())
}

@Preview("Medium size - foldable", device = Devices.FOLDABLE)
@Composable
fun PreviewHomeScreen_Medium(){
    HomeScreen(ApplicationViewModel())
}

@Preview("Expanded size - tablet", device = Devices.TABLET)
@Composable
fun PreviewHomeScreen_Expanded(){
    HomeScreen(ApplicationViewModel())
}