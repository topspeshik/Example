package com.example.ip_test_task.data

import androidx.room.TypeConverter
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converter {

    @TypeConverter
    fun fromTagsList(tags: List<String>): String {
        return JSONArray(tags).toString()
    }

    @TypeConverter
    fun toTagsList(tagsString: String): List<String> {
        val jsonArray = JSONArray(tagsString)
        val list = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }
        return list
    }
}