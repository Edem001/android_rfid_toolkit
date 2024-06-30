package com.example.nfckey

import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.MifareClassic
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfckey.util.MifareClassic1kTag
import com.example.nfckey.util.MutableTag
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(): ViewModel() {
    private val _tagState: MutableStateFlow<Tag?> = MutableStateFlow(null)
    val tagState: StateFlow<Tag?> = _tagState

    private val _tagNdefState: MutableStateFlow<MutableTag?> = MutableStateFlow(null)
    val tagNdefState: StateFlow<MutableTag?> = _tagNdefState

    var records = mutableStateOf<List<ByteArray>>(listOf())
    val recordsString = mutableStateOf<List<String>>(listOf())

    private var key: ByteArray = ByteArray(0)

    fun getSummaryTagInfo(tag: Tag?) {
        viewModelScope.launch {
            _tagState.emit(tag)
                .also { tag?.let{ getDetailedTagInfo(it) } ?: _tagNdefState.emit(null)}
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getDetailedTagInfo(tag: Tag){
        CoroutineScope(Dispatchers.Default).launch {
            val tag: MutableTag? = with(tag.techList){
                when {
                    this.contains(MIFARE_CLASSIC) -> MifareClassic1kTag(tag)
                    else -> null
                }
            }

            tag?.let {
                try {
                    val rawRecords = it.read() ?: listOf()
                    recordsString.value = rawRecords.map { bytes: ByteArray ->
                        bytes.toHexString(
                            HexFormat {
                                bytes {
                                    byteSeparator = ":"
                                }
                                this.upperCase = true
                            }
                        )
                    }

                    records.value = rawRecords
                    _tagNdefState.emit(it)
                } catch (e: IOException){
                    e.printStackTrace()
                } catch (e: TagLostException){
                    e.printStackTrace()
                }
            } ?: _tagNdefState.emit(null)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun setKey(key: String){
        this.key = if (key.isBlank()) MifareClassic.KEY_DEFAULT else key.hexToByteArray()
        tagNdefState.value?.apply {
            this.key = this@ApplicationViewModel.key
        }

        // FIXME: create non-singular key card for testing and extend to key-per-segment functionality
    }

    companion object {
        const val MIFARE_CLASSIC = "android.nfc.tech.MifareClassic"
    }
}