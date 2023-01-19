package com.example.assignment3_courseregistrationapp.database

import androidx.room.*
import com.example.assignment3_courseregistrationapp.database.entity.Course
import com.example.assignment3_courseregistrationapp.database.entity.RegisteredCourse
import com.example.assignment3_courseregistrationapp.database.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    //To check If User is already Registered
    @Query("SELECT userId FROM user_table WHERE userRole ='student'")
    fun checkIfUserExits(): List<String>

    @Query("SELECT * FROM course_table ORDER BY courseId ASC")
    fun getAll(): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewUser(user: User)

    @Query("SELECT userPassword FROM user_table WHERE userId=:studentId AND userRole ='student'")
    fun checkStudentCredentials(studentId: String): String

    @Query("SELECT userPassword FROM user_table WHERE userId=:adminId AND userRole ='admin'")
    fun checkAdminCredentials(adminId: String): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRegisteredCourse(registeredCourse: RegisteredCourse)

    @Query("SELECT * FROM course_table WHERE courseId = :courseId AND courseTerm =:courseTerm")
    fun getCourseDetail(courseId: String, courseTerm: String): Flow<Course>

    @Query("SELECT * FROM course_table WHERE courseType='Mandatory'")
    fun getMandatoryCourseId(): List<Course>

    @Delete
    suspend fun delete(course: Course)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCourse(course: Course)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCourse(course: Course)

    @Query("SELECT * FROM registered_course_table WHERE userId=:studentId AND courseStatus='Registered' Order By courseTerm")
    fun getRegisteredCourse(studentId: String): Flow<List<RegisteredCourse>>

    @Query("SELECT * FROM registered_course_table WHERE userId=:studentId AND courseStatus='Completed' Order By courseTerm")
    fun getCompletedCourseList(studentId: String): List<RegisteredCourse>

    @Query("SELECT * FROM registered_course_table WHERE userId=:studentId AND courseStatus='Registered' Order By courseTerm")
    fun getRegisteredCourseList(studentId: String): List<RegisteredCourse>

    @Delete
    suspend fun deleteRegisteredCourse(registeredCourse: RegisteredCourse)

    @Query("SELECT * FROM registered_course_table  WHERE courseStatus='Registered' Order By userId,courseTerm")
    fun getAllUserRegisteredCourses(): Flow<List<RegisteredCourse>>


}