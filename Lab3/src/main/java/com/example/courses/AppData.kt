package com.example.courses

object AppData {

    var currentUser: String = ""

    val courses = mutableListOf(
        Course(1, "Python для початківців", "Основи мови Python",        "Іваненко О."),
        Course(2, "JavaScript та React",    "Сучасна веб-розробка",       "Петренко В."),
        Course(3, "Django Framework",       "Веб-розробка на Python",     "Сидоренко М."),
        Course(4, "Kotlin для Android",     "Розробка мобільних додатків","Коваленко Д."),
        Course(5, "SQL та бази даних",      "Основи реляційних БД",       "Мельник Т."),
    )

    val enrolled = mutableMapOf<String, MutableSet<Int>>()

    fun enrollUser(username: String, courseId: Int) {
        enrolled.getOrPut(username) { mutableSetOf() }.add(courseId)
        val course = courses.find { it.id == courseId }
        if (course?.status == "not_enrolled") course.status = "in_progress"
    }

    fun isEnrolled(username: String, courseId: Int): Boolean =
        enrolled[username]?.contains(courseId) == true

    fun getUserCourses(username: String): List<Course> =
        courses.filter { isEnrolled(username, it.id) }
}
