import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
data class Employee(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val contactNumber: Long,
    val reportingTo: Int
)
data class EmployeeAttendance(
    val employeeId: Int,
    val checkInDate: LocalDate,
    val checkInTime: LocalTime
)
object EmployeeSystem {
    private val employeeDetails = mutableMapOf<Int, Employee>()
    private val checkedInDetails = mutableMapOf<LocalDate, MutableList<Int>>()
    private var employeeIdCounter = 1
    fun addEmployee() {
        println("Enter First Name: ")
        val firstName = readln()
        println("Enter Last Name: ")
        val lastName = readln()
        println("Enter Role: ")
        val role = readln()
        var contactNumber: Long? = null
        while (contactNumber == null) {
            println("Enter Contact Number: ")
            val input = readln()
            contactNumber = try {
                input.toLong()
            } catch (e: NumberFormatException) {
                println("Invalid contact number")
                null
            }
        }
        var reportingTo: Int? = null
        while (reportingTo == null) {
            println("Enter Reporting To ID, if no one enter 0: ")
            val input = readln()
            reportingTo = try {
                val id = input.toInt()
                if (id == 0 || employeeDetails.containsKey(id)) {
                    id
                }
                else {
                    println("Reporting manager ID not found")
                    null
                }
            } catch (e: NumberFormatException) {
                println("Invalid ID")
                null
            }
        }
        val employee = Employee(
            id = employeeIdCounter,
            firstName = firstName,
            lastName = lastName,
            role = role,
            contactNumber = contactNumber,
            reportingTo = reportingTo
        )
        employeeDetails[employeeIdCounter] = employee
        println("Employee added successfully")
        employeeIdCounter++
    }
    fun listEmployees() {
        println("Employee List with their ID:")
        if (employeeDetails.isEmpty()) {
            println("No employees found")
        } else {
            for ((id, emp) in employeeDetails) {
                println("ID: $id, Name: ${emp.firstName} ${emp.lastName}")
            }
        }
    }
    private fun validateId(id: Int): Boolean = employeeDetails.containsKey(id)
    private fun hasCheckedIn(id: Int, date: LocalDate): Boolean = checkedInDetails[date]?.contains(id) == true
    fun createCheckIn(id: Int, dateTime: LocalDateTime) {
        if (!validateId(id)) {
            println("Employee ID does not exist")
            return
        }
        val date = dateTime.toLocalDate()
        val time = dateTime.toLocalTime()
        if (hasCheckedIn(id, date)) {
            println("Already checked in on $date")
            return
        }
        checkedInDetails.getOrPut(date) { mutableListOf() }.add(id)
        println("Check-in successful at $time on $date")
    }
}
fun getValidDateTimeInput(): LocalDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    while (true) {
        print("Enter Date and Time (yyyy-MM-dd HH:mm) or press Enter to use current time: ")
        val input = readln()
        if (input.isBlank()) return LocalDateTime.now()
        try {
            val parsed = LocalDateTime.parse(input, formatter)
            if (parsed.isAfter(LocalDateTime.now())) {
                println("Check-in date cannot be in the future")
            } else return parsed
        } catch (e: Exception) {
            println("Invalid date and time format")
        }
    }
}
fun main() {
    while (true) {
        println(
            """
            Check-In CLI
            
            1. Add Employee
            2. List Employees
            3. Check-In
            4. Exit
            Enter your choice:
        """.trimIndent()
        )
        when (readln()) {
            "1" -> EmployeeSystem.addEmployee()
            "2" -> EmployeeSystem.listEmployees()
            "3" -> {
                val id = try {
                    print("Enter your Employee ID: ")
                    readln().toInt()
                } catch (e: NumberFormatException) {
                    println("Invalid employee ID")
                    continue
                }
                val dateTime = getValidDateTimeInput()
                EmployeeSystem.createCheckIn(id, dateTime)
            }
            "4" -> {
                println("Exiting, Thank you!")
                break
            }
            else -> println("Invalid choice")
        }
    }
}
