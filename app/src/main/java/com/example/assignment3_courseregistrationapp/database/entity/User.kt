package com.example.assignment3_courseregistrationapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = false) val userId: String,
    @ColumnInfo(name = "userName") val userName: String,
    @ColumnInfo(name = "userPassword") val userPassword: String,
    @ColumnInfo(name = "userRole") val userRole: String
)