package com.example.nfckey.util

interface MutableTag { // TODO: Make more flexible, unify for most popular tag classes
    val records: ArrayList<ByteArray>?

    suspend fun write(data: ByteArray)
    suspend fun read(): ArrayList<ByteArray>?
    suspend fun refresh()
}