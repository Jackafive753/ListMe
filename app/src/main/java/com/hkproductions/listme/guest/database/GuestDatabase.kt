package com.hkproductions.listme.guest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GuestData::class], version = 1, exportSchema = false)
abstract class GuestDatabase : RoomDatabase() {

    abstract val guestDataDao: GuestDataDao

    companion object {

        /**
         * INSTANCE keep a reference to database returned via getInstance.
         *
         * Avoid multiple initialized databases
         *
         * Volatile means that the variable never be cached,
         * all changes will be directly done on the main memory.
         * This is important for consistent data holding
         */
        @Volatile
        private var INSTANCE: GuestDatabase? = null

        /**
         * Helper function to get the database
         *
         * If a database has already been retrieved, the previous databse will be returned.
         * Else create a new database.
         *
         * This function is threadsafe
         *
         * @param context The application context, used to get access to the filesystem
         */
        fun getInstance(context: Context): GuestDatabase {
            /*
            * If multiple threads ask for a database,
            * ensure that there is only one database initialize
            */
            synchronized(this) {
                // Copy the current value of INSTANCE to a local variable so Kotlin can smart cast.
                // Smart cast is only available to local variables.
                var instance = INSTANCE

                //if instance == null create a new database
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GuestDatabase::class.java,
                        "guest_data_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    //Assign INSTANCE to the newly created database
                    INSTANCE = instance
                }

                return instance
            }
        }

    }
}
