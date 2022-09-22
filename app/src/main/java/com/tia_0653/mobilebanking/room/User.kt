package com.tia_0653.mobilebanking.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val UserName: String,
    val Email: String,
    val Password: String,
    val TanggalLahir: String,
    val NoHandphone: String,
)