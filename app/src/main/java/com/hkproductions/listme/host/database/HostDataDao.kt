package com.hkproductions.listme.host.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HostDataDao {

    @Insert
    suspend fun insert(data: HostData)

    @Update
    suspend fun update(data: HostData)

    @Query("SELECT * FROM host_data_table WHERE end_time_milli = -1")
    suspend fun getOpenEntries(): LiveData<List<HostData>>

    @Query("SELECT * FROM host_data_table")
    suspend fun getAllEntries(): LiveData<List<HostData>>

    @Query(
        "SELECT DISTINCT * FROM host_data_table WHERE " +
                "first_name+' '+last_name LIKE '%'+:name+'%'"
    )
    suspend fun getEntriesByName(name: String): LiveData<List<HostData>>

    @Query(
        "SELECT DISTINCT * FROM host_data_table WHERE " +
                "(start_time_milli >= :starttime AND start_time_milli <= :endtime) " +
                "OR (end_time_milli >= :starttime AND end_time_milli <= :endtime)"
    )
    suspend fun getEntriesByTime(starttime: Long, endtime: Long)

    @Query(
        "SELECT DISTINCT * FROM host_data_table WHERE " +
                "((start_time_milli >= :starttime AND start_time_milli <= :endtime) " +
                "OR (end_time_milli >= :starttime AND end_time_milli <= :endtime)) " +
                "AND first_name+' '+last_name LIKE '%:name%'"
    )
    suspend fun getEntriesByNameAndTime(name: String, starttime: Long, endtime: Long)

}