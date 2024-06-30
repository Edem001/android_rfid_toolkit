package com.example.nfckey.util

interface MutableTag: DetailedTag { // TODO: Make more flexible, unify for most popular tag classes
    suspend fun write(data: ByteArray)
}