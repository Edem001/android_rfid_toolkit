@file:OptIn(ExperimentalStdlibApi::class)

package com.example.nfckey

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfckey.ui.screens.home.HomeScreen
import com.example.nfckey.ui.screens.home.HomeScreenSmallSize
import com.example.nfckey.ui.theme.NFCKeyTheme
import com.example.nfckey.util.MutableTag
import com.example.nfckey.util.NFCForegroundDispatcher
import com.example.nfckey.util.NFCForegroundDispatcherImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class MainActivity : ComponentActivity(),
    NFCForegroundDispatcher by NFCForegroundDispatcherImpl() {
    private val viewModel: ApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NFCKeyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val tag by viewModel.tagState.collectAsState()
                    val message by viewModel.tagNdefState.collectAsState()

                    HomeScreen(viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        enableDispatch(this)
    }

    override fun onPause() {
        super.onPause()

        disableDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (intent?.action == NfcAdapter.ACTION_TECH_DISCOVERED || intent?.action == NfcAdapter.ACTION_TAG_DISCOVERED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
                    ?.let { tag -> viewModel.getSummaryTagInfo(tag) }
            } else {
                intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
                    ?.let { tag -> viewModel.getSummaryTagInfo(tag) }
            }
        }
    }
}

@Composable
fun TagGreeting(modifier: Modifier, tag: Tag?, ndefMessage: MutableTag?) {
    var isTagPresented = tag != null

    Column(modifier) {
        if (!isTagPresented) {
            Text(
                "Scan RFID tag",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                "Tag scanned",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.padding(top = 10.dp))

            tag?.let {
                Text("Id: ${it.id.toHexString()}")
                Text("Available technologies:")
                it.techList.forEach { technology -> Text("\t$technology") }

                Spacer(Modifier.padding(top = 10.dp))

                if (ndefMessage == null) {
                    Box(
                        modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier.size(50.dp, 50.dp))
                    }
                } else
                    TagContents(Modifier.fillMaxWidth(), ndefMessage)
            }
        }
    }
}

@Composable
fun TagContents(modifier: Modifier, ndefMessage: MutableTag?) {
    ndefMessage?.let { message ->
        Column(modifier) {
            message.records?.forEach { line ->
                Log.d("Debug/NDEF_MSG", line.toHexString())
                Log.d("Debug/NDEF_MSG", line.toString(Charsets.UTF_8))
                Text(line.toString(Charsets.UTF_8))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NFCKeyTheme {
        TagGreeting(Modifier, null, null)
    }
}