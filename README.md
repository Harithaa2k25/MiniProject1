# EMPLOYEE CHECK-IN & CHECK-OUT 

> OVERVIEW

This is a Kotlin-based Command Line Interface (CLI) application that manages employee check-in and check-out. It allows admins to:
    - Add new employees.
    - Record daily check-ins and check-outs.
    - View attendance logs with working hours per employee.
    - Track who checked in or checked out for any specific date.

The system ensures only one check-in and one check-out per employee per day.

>  DATA CLASSES

**1.DataEmployee**
  - id :String
  - firstName :String 
  - lastName :String
  - role :String
  - contactNumber :Long
  - reportingTo: String
    
**2.DataAttendance**
  - employeeId :String
  - checkIn: LocalDateTime
  - checkOut: LocalDateTime? (nullable)
  - workingHours: String? = null

> CORE FUNCTIONS

**1. addEmployee()**
 - Takes user input (first name, last name, role, contact number, reporting manager ID).
  - Generates a new unique employee ID.
  - Adds the employee to the employeeDetails map.

**2. listEmployees()**
  - Lists all the employees with their full details.

**3. createCheckIn()**
  - Asks for Employee ID and optional date/time input.
  - Validates the ID.
  - Checks whether the employee already checked in for the selected date.
  - If valid, saves the check-in timestamp in attendanceRecords.

**4. createCheckOut()**
  - Asks for Employee ID and optional date/time input.
  - Validates the ID.
  - Checks if the employee has checked in but not yet checked out.
  - Validates that check-in and check-out times are not the same.
  - Updates the check-out timestamp and calculates working hours.

**5. attendanceLogWithWorkingHours()**
  - Displays all employee attendance records for a given date.
  - Includes check-in time, check-out time, and total working duration (e.g., 03:45:00).

**6. validateEmployeeId(id: String): Boolean**
  - Verifies whether an employee ID exists in employeeDetails.

**7. hasCheckedInToday(id: String, date: LocalDate): Boolean**
  - Checks whether the employee has already checked in for the given date.

**8. hasCheckedOutToday(id: String, date: LocalDate): Boolean**
  - Checks whether the employee has already checked out for the given date.

There are two classes : Employee and AttendanceLog class

> MAP

**1.employeeDetails**
employeeDetails={
  id:{
    firstName:"    ",
    lastName:"     ",
    role:"         ",
    contactNumber:XXXXXXXXXX,
    reportTo:XXX
  }
}

**2.checkedInDetails**
checkedInDetails={
  date1:[id1, id2, ...],
  date2: [id1, id2, id3, ...],
}
