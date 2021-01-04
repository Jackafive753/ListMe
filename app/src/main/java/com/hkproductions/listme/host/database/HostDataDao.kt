package com.hkproductions.listme.host.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HostDataDao {

    @Insert
    fun insert(data: HostData)

    @Update
    fun update(data: HostData)

    @Query("SELECT * FROM host_data_table WHERE end_time_milli = -1")
    fun getOpenEntries(): LiveData<List<HostData>>

    @Query("SELECT * FROM host_data_table")
    fun getAllEntries(): LiveData<List<HostData>>

    @Query("SELECT DISTINCT * FROM host_data_table WHERE first_name+' '+last_name LIKE '%'+:name+'%'")
    fun getEntriesByName(name: String): LiveData<List<HostData>>

    @Query(
        "SELECT DISTINCT * FROM host_data_table WHERE (start_time_milli >= :starttime AND start_time_milli <= :endtime) OR (end_time_milli >= :starttime AND end_time_milli <= :endtime)"
    )
    fun getEntriesByTime(starttime: Long, endtime: Long): LiveData<List<HostData>>

    @Query(
        "SELECT DISTINCT * FROM host_data_table WHERE ((start_time_milli >= :starttime AND start_time_milli <= :endtime) OR (end_time_milli >= :starttime AND end_time_milli <= :endtime)) AND first_name+' '+last_name LIKE '%'+:name+'%'"
    )
    fun getEntriesByNameAndTime(
        name: String,
        starttime: Long,
        endtime: Long
    ): LiveData<List<HostData>>

    @Query("SELECT * FROM area_data_table")
    suspend fun getAllAreas(): List<Area>

    @Query("SELECT * FROM host_data_table WHERE area_name = :area AND end_time_milli = -1")
    suspend fun getOpenEntriesInArea(area: String): List<HostData>

}