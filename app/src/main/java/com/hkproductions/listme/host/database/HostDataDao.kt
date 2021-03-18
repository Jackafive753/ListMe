package com.hkproductions.listme.host.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HostDataDao {

    //INSERT
    @Insert
    suspend fun insertHostData(data: HostData): Long

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

    @Query("DELETE FROM host_data_table")
    suspend fun deleteAllHostData()

    //HOSTDATA - GET
    @Query("SELECT * FROM host_data_table WHERE hostDataId = :id LIMIT 1")
    suspend fun getHostDataById(id: Long): HostData

    @Query("SELECT * FROM host_data_table WHERE first_name = :fn AND last_name = :ln AND street = :s AND house_number = :hn AND postal_code = :pc AND city = :c AND phone_number = :pn AND end_time_milli = -1")
    suspend fun getHostDataByAttributesAndCheckedIn(
        fn: String,
        ln: String,
        s: String,
        hn: String,
        pc: String,
        c: String,
        pn: String
    ): HostData?

    @Query("SELECT * FROM host_data_table WHERE end_time_milli = -1")
    fun getOpenEntries(): LiveData<List<HostData>>

    @Query("SELECT * FROM host_data_table")
    fun getAllEntries(): LiveData<List<HostData>>

    @Query("SELECT * FROM host_data_table")
    suspend fun getAllEntriesAsList(): List<HostData>

    @Query("SELECT DISTINCT * FROM host_data_table WHERE LOWER(first_name || ' ' || last_name) LIKE '%'||TRIM(:name)||'%'")
    fun getEntriesByName(name: String): List<HostData>

    @Query(
        "SELECT * FROM host_data_table WHERE((:starttime <= start_time_milli AND start_time_milli <= :endtime) OR (:starttime <= end_time_milli AND end_time_milli <= :endtime) OR (start_time_milli <= :starttime AND :endtime <= end_time_milli))"
    )
    suspend fun getEntriesByTime(starttime: Long, endtime: Long): List<HostData>

    @Query(
        "SELECT * FROM host_data_table WHERE ((:starttime <= start_time_milli AND start_time_milli <= :endtime) OR (:starttime <= end_time_milli AND end_time_milli <= :endtime) OR (start_time_milli <= :starttime AND :endtime <= end_time_milli)) AND LOWER(first_name || ' ' || last_name) LIKE '%'||TRIM(:name)||'%'"
    )
    suspend fun getEntriesByNameAndTime(
        name: String,
        starttime: Long,
        endtime: Long
    ): List<HostData>

    @Query(
        "SELECT * FROM host_data_table WHERE(((:starttime <= start_time_milli AND start_time_milli <= :endtime) OR (:starttime <= end_time_milli AND end_time_milli <= :endtime) OR (start_time_milli <= :starttime AND :endtime <= end_time_milli))AND area_name= :area AND area_name!= '' AND hostDataId !=:id)"
    )
    suspend fun getContactPersonOne(
        starttime: Long,
        endtime: Long,
        area: Long,
        id: Long
    ): List<HostData>

    @Query(
        "SELECT * FROM host_data_table WHERE(((:starttime <= start_time_milli AND start_time_milli <= :endtime) OR (:starttime <= end_time_milli AND end_time_milli <= :endtime) OR (start_time_milli <= :starttime AND :endtime <= end_time_milli))AND (area_name != :area OR area_name= ''))"
    )
    suspend fun getContactPersonTwo(starttime: Long, endtime: Long, area: Long): List<HostData>

    @Query("SELECT * FROM host_data_table WHERE area_name = :areaId")
    suspend fun getHostDataByArea(areaId: Long): List<HostData>

    //AREA - GET
    @Query("SELECT * FROM area_data_table WHERE areaId = :id LIMIT 1")
    fun getLiveAreaById(id: Long): LiveData<Area?>

    @Query("SELECT * FROM area_data_table WHERE areaId = :areaId LIMIT 1")
    suspend fun getAreaById(areaId: Long): Area

    @Query("SELECT * FROM area_data_table ORDER BY area_name ASC")
    fun getAllAreas(): LiveData<List<Area>>

    @Query("SELECT areaId FROM area_data_table")
    fun getAllAreaIds(): LiveData<List<Long>>

    @Query("SELECT * FROM area_data_table ORDER BY area_name ASC")
    suspend fun getAllAreasAsList(): List<Area>

    @Query("SELECT * FROM host_data_table WHERE area_name = :area AND end_time_milli = -1")
    suspend fun getOpenEntriesInAreaAsList(area: Long): List<HostData>

}