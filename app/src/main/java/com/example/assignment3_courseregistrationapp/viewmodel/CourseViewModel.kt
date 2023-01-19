package com.example.assignment3_courseregistrationapp.viewmodel

import androidx.lifecycle.*
import com.example.assignment3_courseregistrationapp.database.CourseDao
import com.example.assignment3_courseregistrationapp.database.entity.Course
import com.example.assignment3_courseregistrationapp.database.entity.RegisteredCourse
import com.example.assignment3_courseregistrationapp.database.entity.User
import kotlinx.coroutines.launch

class CourseViewModel(private val courseDao: CourseDao) : ViewModel() {


    /************************************* Admin Login Fragment Functions Start **************************************/

    fun isAdminLoginCredentialsCorrect(adminEmailId: String, adminPassword: String): Boolean {

        val password: String = courseDao.checkAdminCredentials(adminEmailId)
        if (adminPassword == password) {
            return true
        }
        return false
    }

    fun isAdminLoginEntryValid(adminEmailId: String, adminPassword: String): Boolean {
        if (adminEmailId.isBlank() || adminPassword.isBlank()) {
            return false
        }
        return true
    }


    /************************************* Admin Login Fragment Functions End **************************************/
    /************************************* Admin Course Detail Fragment Functions End *******************************************/

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            courseDao.delete(course)
        }
    }

    /************************************* Admin Course Detail Fragment Functions End ********************************************/

    /************************************* Admin Add Course Fragment Functions Start ********************************************/

    fun isAddEntryValid(
        courseName: String,
        courseId: String,
        courseTerm: String,
        coursePrerequisite: String,
        courseType: String,
    ): Boolean {
        if (courseName.isBlank() || courseId.isBlank() || courseTerm.isBlank() || coursePrerequisite.isBlank() || courseType.isBlank()) {
            return false
        }
        return true
    }

    fun addNewCourse(
        courseName: String,
        courseId: String,
        courseTerm: String,
        coursePrerequisite: String,
        courseType: String,
    ) {
        val newCourse =
            getNewCourseEntry(courseName, courseId, courseTerm, coursePrerequisite, courseType)
        insertNewCourse(newCourse)
    }

    private fun insertNewCourse(course: Course) {
        viewModelScope.launch {
            courseDao.insertNewCourse(course)
        }
    }

    private fun getNewCourseEntry(
        courseName: String,
        courseId: String,
        courseTerm: String,
        coursePrerequisite: String,
        courseType: String,
    ): Course {
        return Course(
            courseName = courseName,
            courseId = courseId,
            courseTerm = courseTerm,
            coursePrerequisite = coursePrerequisite,
            courseType = courseType
        )
    }

    fun updateCourse(
        courseName: String,
        courseId: String,
        courseTerm: String,
        coursePrerequisite: String,
        courseType: String,
    ) {
        val updatedCourse =
            getUpdatedCourseEntry(courseName, courseId, courseTerm, coursePrerequisite, courseType)
        updateCourse(updatedCourse)
    }

    private fun updateCourse(course: Course) {
        viewModelScope.launch {
            courseDao.updateCourse(course)
        }
    }

    private fun getUpdatedCourseEntry(
        courseName: String,
        courseId: String,
        courseTerm: String,
        coursePrerequisite: String,
        courseType: String,
    ): Course {
        return Course(
            courseName = courseName,
            courseId = courseId,
            courseTerm = courseTerm,
            coursePrerequisite = coursePrerequisite,
            courseType = courseType
        )
    }


    /************************************* Admin Add Course Fragment Functions End ********************************************/


    /************************************* User Login Fragment Functions Start **************************************/

    fun isStudentLoginCredentialsCorrect(studentId: String, studentPassword: String): Boolean {

        val pass: String = courseDao.checkStudentCredentials(studentId)
        if (studentPassword == pass) {
            return true
        }
        return false
    }


    fun isStudentLoginEntryValid(studentId: String, studentPassword: String): Boolean {
        if (studentId.isBlank() || studentPassword.isBlank()) {
            return false
        }
        return true
    }

    /************************************* User Login Fragment Functions End **************************************/


    /************************************* Registration Fragment Functions Start **************************************/
    //To check If User is already Registered
    fun retrieveUser(): List<String> {
        return courseDao.checkIfUserExits()
    }

    fun addNewStudent(userId: String, userName: String, userPassword: String) {
        val newStudent = getNewStudentEntry(userId, userName, userPassword)
        insertStudentToUserTable(newStudent)
        addMandatoryCourseForStudentToRegisteredTable(userId)
        addCompletedCourseForStudentToRegisteredTable(userId)
    }

    private fun insertStudentToUserTable(user: User) {
        viewModelScope.launch {
            courseDao.insertNewUser(user)
        }
    }


    fun isStudentRegisterEntryValid(
        userId: String,
        userName: String,
        userPassword: String,
        userRePassword: String,
    ): Boolean {
        if (userId.isBlank() || userName.isBlank() || userPassword.isBlank() || userRePassword.isBlank()) {
            return false
        }
        return true
    }


    private fun getNewStudentEntry(userId: String, userName: String, userPassword: String): User {
        return User(
            userId = userId.lowercase(),
            userName = userName,
            userPassword = userPassword,
            userRole = "student"
        )
    }

    private fun addMandatoryCourseForStudentToRegisteredTable(userId: String) {
        val mandatoryCourseId: List<Course> = courseDao.getMandatoryCourseId()
        for (element in mandatoryCourseId) {
            val mandatoryCourses = getRegisteredCourseType(userId,
                element.courseId,
                element.courseName,
                element.courseTerm,
                element.coursePrerequisite)
            insertIntoRegisteredTable(mandatoryCourses)
        }
    }

    private fun insertIntoRegisteredTable(registeredCourse: RegisteredCourse) {
        viewModelScope.launch {
            courseDao.insertRegisteredCourse(registeredCourse)
        }
    }

    private fun getRegisteredCourseType(
        userId: String,
        courseId: String,
        courseName: String,
        courseTerm: String,
        coursePrerequisite: String,
    ): RegisteredCourse {
        return RegisteredCourse(
            userId = userId.lowercase(),
            courseId = courseId,
            courseName = courseName,
            courseTerm = courseTerm,
            coursePrerequisite = coursePrerequisite,
            courseStatus = "Registered"
        )
    }

    private fun addCompletedCourseForStudentToRegisteredTable(userId: String) {
        val completedCourseId: List<String> = listOf("CS161", "CS162", "Math101")
        for (element in completedCourseId) {
            val name: String
            val term: String
            val prerequsite: String
            when (element) {
                "CS161" -> {
                    name = "Introduction to Programming"
                    term = "1"
                    prerequsite = "None"
                }
                "CS162" -> {
                    name = "Programming and Data Structure"
                    term = "2"
                    prerequsite = "CS161"
                }
                else -> {
                    name = "Math"
                    term = "0"
                    prerequsite = "None"
                }
            }
            val completedCourses =
                getNewStudentCompletedCourse(userId, element, name, term, prerequsite)
            insertIntoRegisteredTable(completedCourses)
        }
    }


    private fun getNewStudentCompletedCourse(
        userId: String,
        courseId: String,
        courseName: String,
        courseTerm: String,
        coursePrerequisite: String,
    ): RegisteredCourse {
        return RegisteredCourse(
            userId = userId.lowercase(),
            courseId = courseId,
            courseName = courseName,
            courseTerm = courseTerm,
            coursePrerequisite = coursePrerequisite,
            courseStatus = "Completed"
        )
    }


    /************************************* Registration Fragment Functions End **************************************/

    /************************************* Course List Fragment Functions Start **************************************/

    val allCourses: LiveData<List<Course>> = courseDao.getAll().asLiveData()


    /************************************* Course List Fragment Functions End **************************************/

    /************************************* Course Detail Fragment Functions Start **************************************/

    fun retrieveCourse(courseId: String, courseTerm: String): LiveData<Course> {
        return courseDao.getCourseDetail(courseId, courseTerm).asLiveData()
    }


    fun addRegisteredCourse(
        studentId: String,
        courseId: String,
        courseName: String,
        courseTerm: String,
        coursePrerequisite: String,
    ) {
        val registerCourse =
            getRegisteredCourseType(studentId, courseId, courseName, courseTerm, coursePrerequisite)
        insertIntoRegisteredTable(registerCourse)
    }

    fun getCompletedCourseList(studentId: String): List<RegisteredCourse> {
        return courseDao.getCompletedCourseList(studentId)
    }

    fun getRegisteredCourseList(studentId: String): List<RegisteredCourse> {
        return courseDao.getRegisteredCourseList(studentId)
    }

    fun getMandatoryCourseList(): List<Course> {
        return courseDao.getMandatoryCourseId()
    }


    /************************************* Course Detail Fragment Functions End **************************************/

    /************************************* Registered Course Fragment Functions Start **************************************/

    fun getRegisteredCourse(studentId: String): LiveData<List<RegisteredCourse>> {
        return courseDao.getRegisteredCourse(studentId).asLiveData()
    }

    fun deleteRegisteredCourse(registeredCourse: RegisteredCourse) {
        viewModelScope.launch {
            courseDao.deleteRegisteredCourse(registeredCourse)
        }
    }

    /************************************* Registered Course Fragment Functions End **************************************/

    /************************************* Admin All User Registered Course Fragment Functions Start **************************************/

    val allUserRegisteredCourses: LiveData<List<RegisteredCourse>> =
        courseDao.getAllUserRegisteredCourses().asLiveData()


    /************************************* Admin All User Registered Course Fragment Functions End **************************************/


}

class CourseViewModelFactory(
    private val courseDao: CourseDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CourseViewModel(courseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}