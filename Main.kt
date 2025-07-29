import java.time.LocalDate
import java.time.LocalTime
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
    private val attendanceLog = mutableListOf<EmployeeAttendance>()
    private var employeeIdCounter = 1
    fun addEmployee() {
        println("Enter First Name: ")
        val firstName = readln()
        println("Enter Last Name: ")
        val lastName = readln()
        println("Enter Role: ")
        val role = readln()
        var contactNumber : Long? = null
        while (contactNumber == null) {
            println("Enter Contact Number: ")
            val input = readln()
            contactNumber = try {
                input.toLong()
            } catch (e: NumberFormatException) {
                println("Invalid contact number. Please enter digits only.")
                null
            }
        }
        var reportingTo: Int? = null
        while (reportingTo == null) {
            println("Enter Reporting To ID,if no one enter 0: ")
            val input = readln()
            reportingTo = try {
                val id = input.toInt()
                if (id == 0 || employeeDetails.containsKey(id)) {
                    id
                } else {
                    println("Reporting manager ID not found.")
                    null
                }
            } catch (e: NumberFormatException) {
                println("Invalid ID")
                null
            }
        }
        if (reportingTo != 0 && !employeeDetails.containsKey(reportingTo)) {
            println("Reporting manager ID $reportingTo not found.")
            return
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
            println("No employees found.")
        } else {
            for ((id, emp) in employeeDetails) {
                println("ID: $id, Name: ${emp.firstName} ${emp.lastName}")
            }
        }
    }

    private fun validateId(id: Int): Boolean = employeeDetails.containsKey(id)

    private fun hasCheckedIn(id: Int, date: LocalDate): Boolean =
        checkedInDetails[date]?.contains(id) == true
    fun createCheckIn() {
        val date = LocalDate.now()
        val time = LocalTime.now()
        createCheckIn(date, time)
    }
    fun createCheckIn(date: LocalDate, time: LocalTime) {
        val id = try {
            print("Enter your Employee ID: ")
            readln().toInt()
        } catch (e: NumberFormatException) {
            println("Invalid employee ID.")
            return
        }
        if (!validateId(id)) {
            println("Employee ID does not exist.")
            return
        }

        if (hasCheckedIn(id, date)) {
            println("Already checked in on $date.")
            return
        }
        checkedInDetails.getOrPut(date) { mutableListOf() }.add(id)
        attendanceLog.add(EmployeeAttendance(id, date, time))
        println("Check-in successful at $time on $date.")
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
                print("Do you want to enter date and time manually? (y/n): ")
                val manual = readln().lowercase() == "y"
                if (manual) {
                    try {
                        print("Enter Date (yyyy-mm-dd): ")
                        val date = LocalDate.parse(readln())
                        print("Enter Time (hh:mm): ")
                        val time = LocalTime.parse(readln())
                        EmployeeSystem.createCheckIn(date, time)
                    } catch (e: Exception) {
                        println("Invalid date or time format.")
                    }
                } else {
                    EmployeeSystem.createCheckIn()
                }
            }
            "4" -> {
                println("Exiting. Thank you!")
                break
            }
            else -> println("Invalid choice.")
        }
    }
}
