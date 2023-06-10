package org.obesifix.obesifix.database.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.obesifix.obesifix.database.entity.Nutrition

@Database(entities = [Nutrition::class], version = 2,exportSchema = true)
abstract class NutritionRoomDatabase:RoomDatabase() {
    abstract fun nutritionDao(): NutritionDao

    companion object {
        @Volatile
        private var INSTANCE: NutritionRoomDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): NutritionRoomDatabase {
            if (INSTANCE == null) {
                synchronized(NutritionRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NutritionRoomDatabase::class.java, "nutrition_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as NutritionRoomDatabase
        }
    }
}