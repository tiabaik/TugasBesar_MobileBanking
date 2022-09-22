package com.tia_0653.mobilebanking.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(note: User)

    @Update
    suspend fun updateUser(note: User)

    @Delete
    suspend fun deleteUser(note: User)

    @Query("SELECT * FROM user")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM User WHERE id =:note_id")
    suspend fun getUser(note_id: Int): List<User>
}