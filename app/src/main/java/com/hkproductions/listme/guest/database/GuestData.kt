package com.hkproductions.listme.guest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents data from a user in guest-mode or his household_members
 * STORED VALUES:
 * -->ID
 * -->FirstName
 * -->LastName
 * -->Street
 * -->House Number
 * -->Postal-Code
 * -->City
 * -->Phone Number
 */
@Entity(tableName = "guest_data")
data class GuestData(
    @PrimaryKey(autoGenerate = true)
    var dataId: Long = 0L,

    @ColumnInfo(name = "first_name")
    var firstName: String = "",

    @ColumnInfo(name = "last_name")
    var lastName: String = "",

    @ColumnInfo(name = "street")
    var street: String = "",

    @ColumnInfo(name = "house_number")
    var houseNumber: String = "",

    @ColumnInfo(name = "postal_code")
    var postalCode: Int = 0,

    @ColumnInfo(name = "city")
    var city: String = "",

    @ColumnInfo(name = "phone_number")
    var phoneNumber: String = ""
)
