package com.hkproductions.listme.host.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HostDataDao {

    //INSERT
    @Insert
    suspend fun insertHostData(data: HostData)

    @Insert
    suspend fun insertArea(area: Area)

    //UPDATE
    @Update
    suspend fun updateHostData(data: HostData)

    @Update
    suspend fun updateArea(area: Area)

    //DELETE
    @Delete
    suspend fun deleteHostData(data: HostData)

    @Delete
    suspend fun deleteArea(area: Area)

    @Query("DELETE FROM area_data_table WHERE areaId = :areaId")
    suspend fun deleteAreaById(areaId: Long)

    //HOSTDATA - GET
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


    //AREA - GET
    @Query("SELECT area_qualifier FROM area_data_table LIMIT 1")
    suspend fun getAreaDesignation(): String

    @Query("SELECT * FROM area_data_table WHERE areaId = :id LIMIT 1")
    fun getLiveAreaById(id: Long): LiveData<Area>

    @Query("SELECT * FROM area_data_table WHERE areaId = :areaId LIMIT 1")
    suspend fun getAreaById(areaId: Long): Area

    @Query("SELECT * FROM area_data_table")
    fun getAllAreas(): LiveData<List<Area>>

    @Query("SELECT areaId FROM area_data_table")
    fun getAllAreaIds(): LiveData<List<Long>>

    @Query("SELECT * FROM area_data_table")
    suspend fun getAllAreasAsList(): List<Area>

    @Query("SELECT * FROM host_data_table WHERE area_name = :area AND end_time_milli = -1")
    suspend fun getOpenEntriesInAreaAsList(area: String): List<HostData>

}