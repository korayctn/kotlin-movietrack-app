package com.koraycetin.myapplication.data.local

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromIntList(list: List<Int>?): String? = list?.joinToString(",")

    @TypeConverter
    fun toIntList(data: String?): List<Int> =
        data?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

    @TypeConverter
    fun fromStringList(list: List<String>?): String? = list?.joinToString(",")

    @TypeConverter
    fun toStringList(data: String?): List<String> =
        data?.split(",")?.map { it } ?: emptyList()
} 