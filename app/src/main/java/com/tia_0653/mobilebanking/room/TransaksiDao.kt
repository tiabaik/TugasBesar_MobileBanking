package com.tia_0653.mobilebanking.room

import androidx.room.*

@Dao
interface TransaksiDao {
    @Insert
    fun addTransaksi(note: Transaksi)

    @Update
    fun updateTransaksi(note: Transaksi)

    @Delete
    fun deleteTransaksi(note: Transaksi)

    @Query("SELECT * FROM Transaksi")
    fun getUsers(): List<Transaksi>

    @Query("SELECT * FROM Transaksi WHERE id =:note_id")
    fun getUser(note_id: Int): Transaksi

    @Query("SELECT * FROM Transaksi WHERE username = :username")
    fun getLogin(username: String): Transaksi?
}