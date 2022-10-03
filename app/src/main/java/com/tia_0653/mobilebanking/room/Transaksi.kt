package com.tia_0653.mobilebanking.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class Transaksi (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val UserName: String,
    val TanggalTransaksi: String,
    val JumlahUang: String,
)