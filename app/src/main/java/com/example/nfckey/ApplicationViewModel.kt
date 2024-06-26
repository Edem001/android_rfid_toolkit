package com.example.nfckey

import android.nfc.Tag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nfckey.util.MifareClassic1kTag
import com.example.nfckey.util.MutableTag
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(): ViewModel() {
    private val _tagState: MutableStateFlow<Tag?> = MutableStateFlow(null)
    val tagState: StateFlow<Tag?> = _tagState

    private val _tagNdefState: MutableStateFlow<MutableTag?> = MutableStateFlow(null)
    val tagNdefState: StateFlow<MutableTag?> = _tagNdefState

    fun getSummaryTagInfo(tag: Tag?) {
        viewModelScope.launch {
            _tagState.emit(tag)
                .also { tag?.let{ getDetailedTagInfo(it) } ?: _tagNdefState.emit(null)}
        }
    }

    fun getDetailedTagInfo(tag: Tag){
        CoroutineScope(Dispatchers.IO).launch {
            val tag: MutableTag? = with(tag.techList){
                when {
                    this.contains(MIFARE_CLASSIC) -> MifareClassic1kTag(tag)
                    else -> null
                }
            }

            tag?.let {
                tag.read()
                _tagNdefState.emit(tag)
            } ?: _tagNdefState.emit(null)
        }
    }

    companion object {
        const val MIFARE_CLASSIC = "android.nfc.tech.MifareClassic"
    }
}