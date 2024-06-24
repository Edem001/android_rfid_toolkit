package com.example.nfckey.util

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.util.Log


class NFCForegroundDispatcherImpl : NFCForegroundDispatcher {
    override fun enableDispatch(activity: Activity) {
        val adapter = NfcAdapter.getDefaultAdapter(activity)
        val intentFilters = arrayOf(
            IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
            IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        )
        val intent: Intent =
            Intent(activity, activity.javaClass)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(activity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE)

        adapter.enableForegroundDispatch(activity, pendingIntent, intentFilters, null)
    }

    override fun disableDispatch(activity: Activity) =
        NfcAdapter
            .getDefaultAdapter(activity)
            .disableForegroundDispatch(activity)
}