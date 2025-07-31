import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.Duration

data class DataEmployee(
    val id: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val contactNumber: Long,
    val reportingTo: String
)

data class DataAttendance(
    val employeeId: String,
    val checkIn: LocalDateTime,
    var checkOut: LocalDateTime? = null,
    var workingHours: String? = null
)

class Employee {
    private val employeeList = mutableMapOf<String, DataEmployee>()
    private var employeeIdCounter = 1

    fun generateNextEmployeeId(): String {
        return "E" + employeeIdCounter++.toString().padStart(3, '0')
    }

    fun addEmployee(employee: DataEmployee) {
        employeeList[employee.id] = employee
    }

    fun isValidEmployeeId(id: String): Boolean {
        return employeeList.containsKey(id)
    }

    fun getEmployeeById(id: String): DataEmployee? {
        return employeeList[id]
    }

    fun listEmployees(): List<DataEmployee> {
        return employeeList.values.toList()
    }
}

class AttendanceLog(private val employee: Employee) {
    private val attendanceRecords = mutableMapOf<LocalDate, MutableList<DataAttendance>>()

    fun hasActiveCheckIn(id: String, date: LocalDate): Boolean {
        val records = attendanceRecords[date]
        if (records != null) {
            for (att in records) {
                if (att.employeeId == id) return true
            }
        }
        return false
    }

    fun createCheckIn(id: String, dateTime: LocalDateTime): Boolean {
        val date = dateTime.toLocalDate()
        if (hasActiveCheckIn(id, date)) return false

        val attendance = DataAttendance(id, dateTime)
        if (!attendanceRecords.containsKey(date)) {
            attendanceRecords[date] = mutableListOf()
        }
        attendanceRecords[date]?.add(attendance)
        return true
    }

    fun createCheckOut(id: String, dateTime: LocalDateTime): Boolean {
        val date = dateTime.toLocalDate()
        val records = attendanceRecords[date] ?: return false

        for (att in records) {
            if (att.employeeId == id) {
                if (att.checkOut != null) return false
                if (!dateTime.isAfter(att.checkIn)) return false

                att.checkOut = dateTime
                val duration = Duration.between(att.checkIn, dateTime)
                val hours = duration.toHours()
                val minutes = duration.toMinutes() % 60
                att.workingHours = String.format("%02d:%02d", hours, minutes)
                return true
            }
        }
        return false
    }

    fun getDailyAttendanceReport(forDate: LocalDate = LocalDate.now()): List<Pair<DataEmployee, DataAttendance>> {
        val list = mutableListOf<Pair<DataEmployee, DataAttendance>>()
        val records = attendanceRecords[forDate]
        if (records != null) {
            for (record in records) {
                val emp = employee.getEmployeeById(record.employeeId)
                if (emp != null) {
                    list.add(Pair(emp, record))
                }
            }
        }
        return list.sortedBy { it.first.firstName }
    }
}

fun getValidDateTimeInput(input: String): LocalDateTime? {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    return try {
        val parsed = LocalDateTime.parse(input, formatter)
        if (parsed.isAfter(LocalDateTime.now())) {
            println("Cannot enter future date/time.")
            null
        } else parsed
    } catch (_: DateTimeParseException) {
        println("Invalid format. Use yyyy-MM-dd HH:mm.")
        null
    }
}

fun main() {
    val employee = Employee()
    val attendance = AttendanceLog(employee)

    // Add one default employee
    val defaultEmp = DataEmployee(
        id = employee.generateNextEmployeeId(),
        firstName = "Emma",
        lastName = "Watson",
        role = "Manager",
        contactNumber = 1234567890,
        reportingTo = "0"
    )
    employee.addEmployee(defaultEmp)

    while (true) {
        println("""
             Employee Attendance CLI
            1. Add Employee
            2. List Employees
            3. Check-In
            4. Check-Out
            5. View Daily Attendance Log
            6. Exit
        """.trimMargin())
        print("Enter your choice: ")
        val choice = readln().trim()

        if (choice == "1") {
            print("First Name: ")
            val f = readln().trim()
            if (f.isEmpty()) {
                println("First name required.")
                continue
            }

            print("Last Name: ")
            val l = readln().trim()
            if (l.isEmpty()) {
                println("Last name required.")
                continue
            }

            print("Role: ")
            val r = readln().trim()
            if (r.isEmpty()) {
                println("Role required.")
                continue
            }

            var contact: Long
            print("Contact Number: ")
            val c = readln().trim()
            try {
                contact = c.toLong()
            } catch (_: NumberFormatException) {
                println("Invalid contact number.")
                continue
            }

            print("Reporting To (0 if none): ")
            val report = readln().trim().uppercase()
            if (report != "0" && !employee.isValidEmployeeId(report)) {
                println("Reporting manager not found.")
                continue
            }

            val newId = employee.generateNextEmployeeId()
            val newEmp = DataEmployee(newId, f, l, r, contact, report)
            employee.addEmployee(newEmp)
            println("Employee added with ID: $newId")

        } else if (choice == "2") {
            val list = employee.listEmployees()
            if (list.isEmpty()) {
                println("No employees found.")
            } else {
                for (e in list) {
                    println("${e.id} - ${e.firstName} ${e.lastName}, Role: ${e.role}, Contact: ${e.contactNumber}, Reports to: ${e.reportingTo}")
                }
            }

        } else if (choice == "3") {
            print("Enter Employee ID: ")
            val id = readln().trim().uppercase()
            if (!employee.isValidEmployeeId(id)) {
                println("Invalid ID.")
                continue
            }

            print("Date & Time (yyyy-MM-dd HH:mm) or leave blank: ")
            val input = readln()
            val dt = if (input.isBlank()) LocalDateTime.now() else getValidDateTimeInput(input)
            if (dt != null) {
                if (attendance.createCheckIn(id, dt)) {
                    println("Check-in successful.")
                } else {
                    println("Already checked in for ${dt.toLocalDate()}")
                }
            }

        } else if (choice == "4") {
            print("Enter Employee ID: ")
            val id = readln().trim().uppercase()
            if (!employee.isValidEmployeeId(id)) {
                println("Invalid ID.")
                continue
            }

            print("Date & Time (yyyy-MM-dd HH:mm) or leave blank: ")
            val input = readln()
            val dt = if (input.isBlank()) LocalDateTime.now() else getValidDateTimeInput(input)
            if (dt != null) {
                if (attendance.createCheckOut(id, dt)) {
                    println("Check-out successful.")
                } else {
                    println("Either not checked in or already checked out.")
                }
            }

        } else if (choice == "5") {
            print("Enter date (yyyy-MM-dd) or leave blank: ")
            val input = readln()
            val date = if (input.isBlank()) LocalDate.now() else try {
                LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            } catch (_: DateTimeParseException) {
                println("Invalid date format.")
                continue
            }

            val report = attendance.getDailyAttendanceReport(date)
            if (report.isEmpty()) {
                println("No attendance for $date")
            } else {
                println("Attendance Report for $date:")
                for ((emp, rec) in report) {
                    val ci = rec.checkIn.toLocalTime()
                    val co = rec.checkOut?.toLocalTime() ?: "N/A"
                    val hrs = rec.workingHours ?: "N/A"
                    println("${emp.id} - ${emp.firstName} ${emp.lastName}: Check-In $ci, Check-Out $co, Hours $hrs")
                }
            }

        } else if (choice == "6") {
            println("Exiting.")
            break

        } else {
            println("Invalid option.")
        }
    }
}
