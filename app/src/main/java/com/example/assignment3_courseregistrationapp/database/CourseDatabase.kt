package com.example.assignment3_courseregistrationapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.assignment3_courseregistrationapp.database.entity.Course
import com.example.assignment3_courseregistrationapp.database.entity.RegisteredCourse
import com.example.assignment3_courseregistrationapp.database.entity.User

@Database(
    entities = [User::class, Course::class, RegisteredCourse::class],
    version = 2,
)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao() : CourseDao
    companion object{
        @Volatile
        private var INSTANCE : CourseDatabase? = null
        fun getDatabase (context : Context): CourseDatabase {
            return (INSTANCE ?: synchronized(this){
                val instance: CourseDatabase =
                    Room.databaseBuilder(
                        context,
                        CourseDatabase::class.java,
                        "course_database")
                        .createFromAsset("database/course_database.db")
                        .allowMainThreadQueries()
                        .build()
                INSTANCE = instance
                instance
            })
        }
    }
}
