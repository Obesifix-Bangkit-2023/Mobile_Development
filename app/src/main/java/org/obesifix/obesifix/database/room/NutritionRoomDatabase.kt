package org.obesifix.obesifix.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.obesifix.obesifix.database.entity.Nutrition
import org.obesifix.obesifix.database.helper.Converters

@Database(entities = [Nutrition::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
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
                        .build()
                }
            }
            return INSTANCE as NutritionRoomDatabase
        }
    }
}