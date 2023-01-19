package com.example.assignment3_courseregistrationapp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_table",primaryKeys = ["courseId", "courseTerm"])
data class Course (

    val courseId: String,
    val courseTerm: String,
    @ColumnInfo(name = "courseName") val courseName: String,
    @ColumnInfo(name = "coursePrerequisite") val coursePrerequisite: String,
    @ColumnInfo(name = "courseType") val courseType: String
)
