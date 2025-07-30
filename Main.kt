import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class DataEmployee(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val role: String,
    val contactNumber: Long,
    val reportingTo: Int
)

data class DataAttendance(
    val employeeId: Int,
    val checkInDate: LocalDate,
    val checkInTime: LocalTime
)

object EmployeeSystem {
    private val employeeDetails = mutableMapOf<Int, DataEmployee>()
    private val checkedInDetails = mutableMapOf<LocalDate, MutableList<DataAttendance>>()
    private var employeeIdCounter = 1

    fun addEmployee(employee: DataEmployee) {
        employeeDetails[employee.id] = employee
    }

    fun listEmployees(): List<DataEmployee> = employeeDetails.values.toList()

    fun generateNextEmployeeId(): Int = employeeIdCounter++

    fun isValidEmployeeId(id: Int): Boolean = employeeDetails.containsKey(id)

    fun hasCheckedIn(id: Int, date: LocalDate): Boolean =
        checkedInDetails[date]?.any { it.employeeId == id } == true

    fun createCheckIn(id: Int, dateTime: LocalDateTime): Boolean {
        val date = dateTime.toLocalDate()
        val time = dateTime.toLocalTime()

        if (hasCheckedIn(id, date)) return false

        val attendance = DataAttendance(id, date, time)
        checkedInDetails.getOrPut(date) { mutableListOf() }.add(attendance)
        return true
    }

    fun listCheckedInEmployees(forDate: LocalDate = LocalDate.now()): List<DataEmployee> {
        val attendanceList = checkedInDetails[forDate] ?: return emptyList()
        val result = mutableListOf<DataEmployee>()
        for (attendance in attendanceList) {
            val emp = employeeDetails[attendance.employeeId]
            if (emp != null) result.add(emp)
        }
        return result
    }
}

fun getValidDateTimeInput(input: String): LocalDateTime? {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return try {
        val parsed = LocalDateTime.parse(input, formatter)
        if (parsed.isAfter(LocalDateTime.now())) {
            println("Cannot enter a future date/time.")
            null
        } else {
            parsed
        }
    } catch (e: Exception) {
        println("Invalid format. Please use yyyy-MM-dd HH:mm.")
        null
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
            4. List Today's Checked-in Employees
            5. Exit
            Enter your choice:
        """.trimIndent()
        )

        when (readln()) {
            "1" -> {
                print("Enter First Name: ")
                val firstName = readln()

                print("Enter Last Name: ")
                val lastName = readln()

                print("Enter Role: ")
                val role = readln()

                var contactNumber: Long
                while (true) {
                    print("Enter Contact Number: ")
                    val input = readln()
                    contactNumber = try {
                        input.toLong()
                    } catch (e: NumberFormatException) {
                        println("Invalid contact number.")
                        continue
                    }
                    break
                }

                var reportingTo: Int
                while (true) {
                    print("Enter Reporting To ID (0 if none): ")
                    val input = readln()
                    reportingTo = try {
                        input.toInt()
                    } catch (e: NumberFormatException) {
                        println("Invalid manager ID.")
                        continue
                    }

                    if (reportingTo == 0 || EmployeeSystem.isValidEmployeeId(reportingTo)) {
                        break
                    } else {
                        println("Reporting manager ID not found.")
                    }
                }

                val newEmployee = DataEmployee(
                    id = EmployeeSystem.generateNextEmployeeId(),
                    firstName = firstName,
                    lastName = lastName,
                    role = role,
                    contactNumber = contactNumber,
                    reportingTo = reportingTo
                )

                EmployeeSystem.addEmployee(newEmployee)
                println("Employee added successfully with ID: ${newEmployee.id}")
            }

            "2" -> {
                val listOfEmployees = EmployeeSystem.listEmployees()
                if (listOfEmployees.isEmpty()) {
                    println("No employees in the list.")
                } else {
                    println("Employee list:")
                    for (emp in listOfEmployees) {
                        println("ID: ${emp.id}, Name: ${emp.firstName} ${emp.lastName}, Role: ${emp.role}, Contact: ${emp.contactNumber}, Reporting To: ${emp.reportingTo}")
                    }
                }
            }

            "3" -> {
                var id: Int
                while (true) {
                    print("Enter your Employee ID: ")
                    val input = readln()
                    id = try {
                        input.toInt()
                    } catch (e: NumberFormatException) {
                        println("Invalid employee ID.")
                        continue
                    }
                    if (!EmployeeSystem.isValidEmployeeId(id)) {
                        println("Employee ID not found.")
                        continue
                    }
                    break
                }

                var dateTime: LocalDateTime? = null
                while (dateTime == null) {
                    print("Enter Date and Time (yyyy-MM-dd HH:mm) or press Enter for now: ")
                    val input = readln()
                    dateTime = if (input.isBlank()) LocalDateTime.now()
                    else getValidDateTimeInput(input)
                }

                val success = EmployeeSystem.createCheckIn(id, dateTime)
                if (success) {
                    println("Check-in successful at ${dateTime.toLocalTime()} on ${dateTime.toLocalDate()}")
                } else {
                    println("Already checked in for ${dateTime.toLocalDate()}")
                }
            }

            "4" -> {
                val checkedInEmployees = EmployeeSystem.listCheckedInEmployees()
                if (checkedInEmployees.isEmpty()) {
                    println("No employees have checked in today.")
                } else {
                    println("Checked-in Employees for Today:")
                    for (emp in checkedInEmployees) {
                        println("ID: ${emp.id}, Name: ${emp.firstName} ${emp.lastName}")
                    }
                }
            }

            "5" -> {
                println("Exiting, Thank you!")
                break
            }

            else -> println("Invalid choice.")
        }
    }
}
