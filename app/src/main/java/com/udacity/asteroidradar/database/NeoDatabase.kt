package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*

@Database(entities = [NearEarthObject::class], version = 1, exportSchema = false)
abstract class NeoDatabase: RoomDatabase() {

    abstract val neoDatabaseDao: NeoDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: NeoDatabase? = null

        fun getInstance(context: Context): NeoDatabase {

            // Only one thread may enter a synchronized block at a time.
            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NeoDatabase::class.java,
                        "asteroid_database"
                    ).fallbackToDestructiveMigration().build()

                    // Assign null DB instance to the newly created database.
                    INSTANCE = instance
                }

                return instance
            }
        }
    }



//    override fun clearAllTables() {
//
//    }

//    override fun createInvalidationTracker(): InvalidationTracker {
//
//    }

//    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
//
//    }
}