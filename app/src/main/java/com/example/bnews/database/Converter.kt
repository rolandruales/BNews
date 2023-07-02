package com.example.bnews.database

import androidx.room.TypeConverter
import com.example.bnews.model.Source

class Converter {

    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}