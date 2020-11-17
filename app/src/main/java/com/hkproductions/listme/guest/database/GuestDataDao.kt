package com.hkproductions.listme.guest.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GuestDataDao {

    @Insert
    suspend fun insert(guest: GuestData)

    @Update
    suspend fun update(guest: GuestData)

    @Query("SELECT * FROM guest_data_table WHERE guestDataId = :id")
    suspend fun getDataById(id: Long): List<GuestData>

    @Query("SELECT * FROM guest_data_table")
    fun getAllGuestData(): LiveData<List<GuestData>>

    @Query("SELECT * FROM guest_data_table WHERE phone_owner = 1 LIMIT 1")
    suspend fun getPhoneOwner(): GuestData?

    @Query("SELECT * FROM guest_data_table WHERE phone_owner = 0")
    fun getHouseMembers(): LiveData<List<GuestData>>
}