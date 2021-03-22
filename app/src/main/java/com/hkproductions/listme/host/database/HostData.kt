package com.hkproductions.listme.host.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents data of a guest stored by the host
 * * STORED VALUES:
 * -->ID
 * -->FirstName
 * -->LastName
 * -->Street
 * -->House Number
 * -->Postal-Code
 * -->City
 * -->Phone Number
 * -->StartTime
 * -->End Time
 * -->Area Name (Placement)
 */
@Entity(tableName = "host_data_table")
data class HostData(
    @PrimaryKey(autoGenerate = true)
    var hostDataId: Long = 0L,

    @ColumnInfo(name = "first_name")
    var firstName: String = "",

    @ColumnInfo(name = "last_name")
    var lastName: String = "",

    @ColumnInfo(name = "street")
    var street: String = "",

    @ColumnInfo(name = "house_number")
    var houseNumber: String = "",

    @ColumnInfo(name = "postal_code")
    var postalCode: String = "",

    @ColumnInfo(name = "city")
    var city: String = "",

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String = "",

    /**
     * start_time_milli represents the time of the check-in
     */
    @ColumnInfo(name = "start_time_milli")
    val startTimeMilli: Long = System.currentTimeMillis(),

    /**
     * end_time_milli represents the time of the check-out
     */
    @ColumnInfo(name = "end_time_milli")
    var endTimeMilli: Long = -1,

    /**
     * area represents an area in a room, e.g. table
     */
    @ColumnInfo(name = "area_name")
    var areaName: Long = -1
)
