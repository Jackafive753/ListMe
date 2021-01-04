package com.hkproductions.listme.host.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "area_data_table")
data class Area(
    @PrimaryKey(autoGenerate = true)
    var areaId: Long = 0L,

    /**
     * name of the area
     * default: numerized
     */
    @ColumnInfo(name = "area_name")
    var name: String = "",

    /**
     * designation of the area
     * e.g. table
     */
    @ColumnInfo(name = "area_qualifier")
    var designation: String = ""
)