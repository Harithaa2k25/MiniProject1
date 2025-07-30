# EMPLOYEE CHECK-IN & CHECK-OUT 

> OVERVIEW

This is a general architecture and workflow approach for building a Command Line Interface (CLI) based Employee Check-In and Check-Out System using Kotlin. The system is designed to track employee check-ins and check-outs per day and ensure each employee checks-in and check-outs only once per day.

>  DATA CLASSES

**1.DataEmployee**
  - id :Int
  - firstName :String 
  - lastName :String
  - role :String
  - contactNumber :Long
  - reportingTo: Int
    
**2.DataAttendance**
  - employeeId :Int
  - checkIn: LocalDateTime
  - checkOut: LocalDateTime? (nullable)

> FUNCTIONS

**1.addEmployee()**
  - gets firstName,lastName,role,contactNumber and reportingTo from user and creates an id automatically.
  - Stores in employeeDetails map using DataEmployee.

**2.listEmployee()**
  - employee details will be returned.
    
**3.createCheckIn()**
  - gets user id and takes current date and current time and validates id using validateId() and hasCheckedInToday().
  - checks if the employee exists and already checked in for the day.
  - If valid, stores the check-in in checkedInDetails.

**4.createCheckOut()**
  - gets user id and takes current date and current time and validates id using validateId() and hasCheckedInToday().
  - checks if the employee exists , already checked in for the day and not already checked out.
  - If valid, updates the check-out time in checkedInDetails.
    
**5.validateId()**
  - checks whether id is present in employeeDetails map.

**6.hasCheckedIn()**
  - checks whether id is present in checkedInDetails map.

**7.listCheckedInEmployees(forDate: LocalDate): List<DataEmployee>**
  - Returns employees who have checked in today.

**8.listCheckedOutEmployees(forDate: LocalDate): List<DataEmployee>**
  - Returns employees who have checked out for a given date (default = today).

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
