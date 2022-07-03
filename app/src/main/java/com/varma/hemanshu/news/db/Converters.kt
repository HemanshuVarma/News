package com.varma.hemanshu.news.db

import androidx.room.TypeConverter
import com.varma.hemanshu.news.models.Source

class Converters {

    // Using converter to tell Room, how to interpret source.
    // FYI, Room only understands primitives by default
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}