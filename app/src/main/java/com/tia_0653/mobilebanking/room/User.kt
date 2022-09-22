package com.tia_0653.mobilebanking.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity

data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val note: String
)