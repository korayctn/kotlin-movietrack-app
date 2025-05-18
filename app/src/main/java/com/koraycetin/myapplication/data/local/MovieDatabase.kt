package com.koraycetin.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.koraycetin.myapplication.data.model.Movie
import com.koraycetin.myapplication.data.model.TvShow

@Database(
    entities = [Movie::class, TvShow::class],
    version = 9,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvShowDao(): TvShowDao
} 