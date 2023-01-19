package com.example.assignment3_courseregistrationapp

import android.app.Application
import com.example.assignment3_courseregistrationapp.database.CourseDatabase

class CourseRegistrationApplication: Application() {
    val database: CourseDatabase by lazy { CourseDatabase.getDatabase(this) }
}
