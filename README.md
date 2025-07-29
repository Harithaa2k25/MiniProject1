# EMPLOYEE CHECK-IN

> OVERVIEW

This is a general architecture and workflow approach for building a Command Line Interface (CLI) based Employee Check-In System using Kotlin. The system is designed to track employee check-ins per day and ensure each employee checks in only once per day.

>  DATA CLASSES

**1.Employee**
  - id :Int
  - firstName :String 
  - lastName :String
  - role :String
  - contactNumber :Long
  - reportingTo: Int
    
**2.EmployeeAttendance**
  - employeeId :Int
  - checkInDate :LocalDate
  - checkInTime :LocalTime

> FUNCTIONS

**1.addEmployee()**
  - gets firstName,lastName,role,contactNumber and reportingTo from user and creates an id automatically.
  - stores in  employeeDetails(Map) using Employee data class.

**2.listEmployee()**
  - id and employeename will be returned.
    
**3.createCheckIn()**
  - gets user id and takes current date and current time and validates id using validateId() and hasCheckedInToday().
  - stores data in checkedInDetails map using CheckIn(Data class).

**4.validateId()**
  - checks whether id is present in employeeDetails map.

**5.hasCheckedIn()**
  - checks whether id is present in checkedInDetails map.

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
