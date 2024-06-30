package com.example.nfckey.util

interface DetailedTag {
    val records: ArrayList<ByteArray>?
    var key: ByteArray
    suspend fun read(): List<ByteArray>?
    suspend fun refresh()
}