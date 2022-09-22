package com.tia_0653.mobilebanking.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun addUser(note: User)

    @Update
    fun updateUser(note: User)

    @Delete
    fun deleteUser(note: User)

    @Query("SELECT * FROM user")
    fun getUsers(): List<User>

    @Query("SELECT * FROM User WHERE id =:note_id")
    fun getUser(note_id: Int): User

    @Query("SELECT * FROM User WHERE username = :username AND password = :password")
    fun getLogin(username: String, password: String): User?
}