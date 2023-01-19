package com.example.assignment3_courseregistrationapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "registered_course_table")
data class RegisteredCourse (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "courseId") val courseId: String,
    @ColumnInfo(name = "courseName") val courseName: String,
    @ColumnInfo(name = "courseTerm") val courseTerm: String,
    @ColumnInfo(name = "coursePrerequisite") val coursePrerequisite: String,
    @ColumnInfo(name = "courseStatus") val courseStatus: String
)