package com.diegoparra.kino.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Favourite::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class KinoDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "com.diegoparra.kino.kinodb"
    }

    abstract fun favouritesDao(): FavouritesDao

}