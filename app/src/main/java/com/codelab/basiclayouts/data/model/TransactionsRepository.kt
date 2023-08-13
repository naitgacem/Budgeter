package com.codelab.basiclayouts.data.model

import android.content.Context
import com.codelab.basiclayouts.ui.components.DaySummary
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class TransactionsRepository(private val context: Context) {
    private val fileDirectory: File = context.filesDir
    fun readDailyTransactionsFromFile(date: String): DaySummary? {
        val json = Json { ignoreUnknownKeys = true }
        val file = File(fileDirectory, "$date.json")

        return if (file.exists()) {
            val jsonString = file.readText()
            json.decodeFromString<DaySummary>(string = jsonString)
        } else {
            null
        }
    }

    fun addEntry(daySummary: DaySummary){
        val file = File(fileDirectory, "${daySummary.date}")
        return if(!file.exists()){
            writeDailyTransactionsToFile(daySummary)
        } else {
            val json = Json { ignoreUnknownKeys = true }
            val jsonString = file.readText()
            val oldDaySummary = json.decodeFromString<DaySummary>(string = jsonString)
            val newDaySummary = oldDaySummary.copy(spending = oldDaySummary.spending + daySummary.spending)
            writeDailyTransactionsToFile(newDaySummary)
        }
    }
    fun writeDailyTransactionsToFile(dailyTransactions: DaySummary?) {
        if (dailyTransactions != null) {
            val json = Json { prettyPrint = true }
            val jsonString = json.encodeToString(dailyTransactions)
            val file = File(fileDirectory, "${dailyTransactions.date}.json")
            file.writeText(jsonString)
        }
    }

    fun listAllFiles(): List<File> {
        return fileDirectory.listFiles()?.toList() ?: emptyList()
    }

    fun deleteAllFiles() {
        fileDirectory.listFiles()?.forEach { file ->
            file.delete()
        }
    }
}