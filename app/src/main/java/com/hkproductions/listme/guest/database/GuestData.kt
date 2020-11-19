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
@Entity(tableName = "guest_data_table")
data class GuestData(
    @PrimaryKey(autoGenerate = true)
    var guestDataId: Long = 0L,

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
     * phone owner is the main data. There can only be one
     * true -> my data
     * false -> this person is a house_member
     */
    @ColumnInfo(name = "phone_owner")
    var phoneOwner: Boolean = false
)
