package com.example.nfckey.util

import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.MifareClassic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class MifareClassic1kTag(val tag: Tag, val key: ByteArray = MifareClassic.KEY_DEFAULT): MutableTag {
    private var _records: ArrayList<ByteArray>? = null
    override val records get() = _records

    override suspend fun write(data: ByteArray) {
        TODO("Not yet implemented")
    }

    override suspend fun refresh() { CoroutineScope(Dispatchers.IO).launch { _records = readContents() ?: ArrayList() } }

    override suspend fun read(): ArrayList<ByteArray> = records ?: readContents().also { _records = it }

    @Throws(Exception::class)
    private fun readContents(): ArrayList<ByteArray> {
        val mifare = MifareClassic.get(tag)

        try {
            if (mifare.isConnected) mifare.close()
            mifare.connect()

            val outputList: ArrayList<ByteArray> = ArrayList(MIFARE_CLASSIC_1K_SECTORS)
            for (i in 0 until MIFARE_CLASSIC_1K_SECTORS){
                if (mifare.authenticateSectorWithKeyA(i, key)){
                    val startBlock = i * MIFARE_CLASSIC_1K_BLOCKS_IN_SECTOR + 1
                    val endBlock = (i + 1) * MIFARE_CLASSIC_1K_BLOCKS_IN_SECTOR - 1
                    for (j in startBlock until endBlock){
                        outputList.add(mifare.readBlock(j))
                    }

                } else {
                    throw Exception("MIFARE AUTH on sector $i failed. Check key.")
                } // If some of sectors have different keys, another method is more suitable
            }

            mifare.close()
            return outputList
        } catch (e: IOException){
            e.printStackTrace()
        } catch (e: TagLostException){
            e.printStackTrace()
        }

//        return null
        throw Exception("MIFARE Read failed. Check IOException or TagLostException.")
    }

    /* TODO: add possibility to encrypt separate sectors with different keys and also reading them
        Also consider creating dump method
    */

    companion object {
        const val MIFARE_CLASSIC_1K_SECTORS = 16
        const val MIFARE_CLASSIC_1K_BLOCKS_IN_SECTOR = 4
    }
}