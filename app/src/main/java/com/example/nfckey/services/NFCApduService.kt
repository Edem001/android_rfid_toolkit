package com.example.nfckey.services

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log

class NFCApduService: HostApduService() {
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        return ByteArray(0)
    }

    override fun onDeactivated(reason: Int) {
        Log.d("Debug/NFC", "Deactivated: $reason")
    }
}