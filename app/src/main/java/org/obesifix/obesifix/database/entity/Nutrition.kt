package org.obesifix.obesifix.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition")
data class Nutrition(
    @PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "id")
    var id: Long = 0,

    @field:ColumnInfo(name = "userid")
    var userid: String = "",

    @field:ColumnInfo(name = "calorie")
    var calorie: Float = 0f,

    @field:ColumnInfo(name = "fat")
    var fat: Float = 0f,

    @field:ColumnInfo(name = "protein")
    var protein: Float = 0f,

    @field:ColumnInfo(name = "carbohydrate")
    var carbohydrate: Float = 0f,

    @ColumnInfo(name = "date")
    var date: String = ""
)
