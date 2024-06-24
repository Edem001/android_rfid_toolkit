package com.example.nfckey.util

import android.app.Activity

interface NFCForegroundDispatcher {
    fun enableDispatch(activity: Activity)
    fun disableDispatch(activity: Activity)
}