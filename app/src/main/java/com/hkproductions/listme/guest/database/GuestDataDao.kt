package com.hkproductions.listme.guest.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GuestDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(guest: GuestData)

    @Query("SELECT * FROM guest_data_table WHERE guestDataId = :id LIMIT 1")
    suspend fun getDataById(id: Long): GuestData?

    @Query("SELECT * FROM guest_data_table")
    suspend fun getAllGuestData(): List<GuestData>

    @Query("SELECT * FROM guest_data_table WHERE phone_owner = 1 LIMIT 1")
    suspend fun getPhoneOwner(): GuestData?

    @Query("SELECT * FROM guest_data_table WHERE phone_owner = 0")
    fun getContacts(): LiveData<List<GuestData>>

    @Query("DELETE FROM guest_data_table")
    suspend fun clear()

    @Query("DELETE FROM guest_data_table WHERE guestDataId = :id")
    suspend fun clearDataById(id: Long)
}