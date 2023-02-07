package ie.setu

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.TextBorder
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import ie.setu.controllers.EmployeeAPI
import ie.setu.models.Employee
import mu.KotlinLogging
import java.time.LocalDateTime

val logger = KotlinLogging.logger {}

var employees = EmployeeAPI()

internal fun dummyData() {
    logger.debug { "Adding dummy data" }
    employees.create(Employee("Joe", "Soap", 'm', 0, 35655.43, 31.0, 7.5, 2000.0, 25.6, updatedAt = LocalDateTime.parse("2019-10-12T12:14:20.43"), createdAt = LocalDateTime.parse("2018-12-30T12:32:50.63")))
    employees.create(Employee("Joan", "Murphy", 'f', 0, 54255.13, 32.5, 7.0, 1500.0, 55.3, updatedAt = LocalDateTime.parse("2021-05-22T19:34:21.33"), createdAt = LocalDateTime.parse("2020-02-12T19:34:20.53")))
    employees.create(Employee("Mary", "Quinn", 'f', 0, 75685.41, 40.0, 8.5, 4500.0, 0.0, updatedAt = LocalDateTime.parse("2023-01-11T15:54:20.23"), createdAt = LocalDateTime.parse("2022-11-22T19:33:52.26")))
}

internal fun getEmployeeById(): Employee? {
    logger.debug { "Trying to get employee by id" }
    print("Enter the employee id to search by: ")
    var employeeID = readln().toIntOrNull()
    while (employeeID == null) {
        println("Error: employee id must be a valid number. Please enter a valid employee id.")
        employeeID = readln().toIntOrNull()
    }

    logger.debug { "Searching for employee with id $employeeID" }
    val employee = employees.findOne(employeeID)

    // TODO: Test this
    if (employee == null) {
        logger.debug { "Employee not found with id $employeeID" }
        println("Employee not found with that ID.")
        return null
    }

    logger.debug { "Employee found: $employee" }
    logger.debug { "Displaying employee" }
    println("\nThe following employee was found:")
    println(employees.generateEmployeeTable(employee).renderText(border=TextBorder.ROUNDED))

    return employee
}

internal fun getMultipleEmployeesByID(): MutableList<Employee>? {
    var searching = true
    val employeeList: MutableList<Employee> = ArrayList()

    while (searching) {
        val employee = getEmployeeById()

        if (employee != null) {
            employeeList.add(employee)
        }

        print("Do you want to add another employee to the list? (y/n): ")
        val searchAgain = readln().toCharArray().getOrNull(0)
        if (searchAgain != 'y') {
            searching = false
        }
        println()
    }

    return employeeList.ifEmpty { null }
}

fun generateEmployee(old: Employee? = null): Employee {
    logger.debug { "Generating employee" }

    // TODO: Test properly to make sure this works
    // ? Trying to update with an invalid value just keeps the old one
    // def = default; shortened to save space
    fun def(prop: Any?) = if (old != null) " (${prop})" else ""

    print("Enter first name${def(old?.firstName)}: ")
    var firstName = readlnOrNull().toString()
    while (firstName.isBlank()) {
        firstName = if (old != null)
            old.firstName
        else {
            println("Error: first name cannot be empty. Please enter a valid first name.")
            readlnOrNull().toString()
        }
    }

    print("Enter surname${def(old?.surname)}: ")
    var surname = readlnOrNull().toString()
    while (surname.isBlank()) {
        surname = if (old != null)
            old.surname
        else {
            println("Error: surname cannot be empty. Please enter a valid surname.")
            readlnOrNull().toString()
        }
    }

    print("Enter gender (m/f)${def(old?.gender)}: ")
    var gender = readln().toCharArray().getOrNull(0)
    while (gender != 'm' && gender != 'f') {
        gender = if (old != null)
            old.gender
        else {
            println("Error: gender must be either 'm' or 'f'. Please enter a valid gender.")
            readln().toCharArray()[0]
        }
    }

    print("Enter gross salary${def(old?.grossSalary)}: ")
    var grossSalary = readln().toDoubleOrNull()
    while (grossSalary == null) {
        grossSalary = if (old != null)
            old.grossSalary
        else {
            println("Error: gross salary must be a valid number. Please enter a valid gross salary.")
            readln().toDoubleOrNull()
        }
    }

    print("Enter PAYE %${def(old?.payePercentage)}: ")
    var payePercentage = readln().toDoubleOrNull()
    while (payePercentage == null || payePercentage < 0 || payePercentage > 100) {
        payePercentage = if (old != null)
            old.payePercentage
        else {
            println("Error: PAYE must be a valid number between 0 and 100. Please enter a valid PAYE.")
            readln().toDoubleOrNull()
        }
    }

    print("Enter PRSI %${def(old?.prsiPercentage)}: ")
    var prsiPercentage = readln().toDoubleOrNull()
    while (prsiPercentage == null || prsiPercentage < 0 || prsiPercentage > 100) {
        prsiPercentage = if (old != null)
            old.prsiPercentage
        else {
            println("Error: PRSI must be a valid number between 0 and 100. Please enter a valid PRSI.")
            readln().toDoubleOrNull()
        }
    }

    print("Enter Annual Bonus${def(old?.annualBonus)}: ")
    var annualBonus = readln().toDoubleOrNull()
    while (annualBonus == null) {
        annualBonus = if (old != null)
            old.annualBonus
        else {
            println("Error: annual bonus must be a valid number. Please enter a valid annual bonus.")
            readln().toDoubleOrNull()
        }
    }

    print("Enter Cycle to Work Deduction${def(old?.cycleToWorkSchemeMonthlyDeduction)}: ")
    var cycleToWorkMonthlyDeduction = readln().toDoubleOrNull()
    while (cycleToWorkMonthlyDeduction == null) {
        cycleToWorkMonthlyDeduction = if (old != null)
            old.cycleToWorkSchemeMonthlyDeduction
        else {
            println("Error: cycle to work monthly deduction must be a valid number. Please enter a valid cycle to work monthly deduction.")
            readln().toDoubleOrNull()
        }
    }

    return Employee(firstName, surname, gender, 0, grossSalary, payePercentage, prsiPercentage, annualBonus, cycleToWorkMonthlyDeduction)
}

fun addEmployee() {
    val employee = generateEmployee()

    logger.debug { "Adding employee: $employee" }
    employees.create(employee)

    println("\nThe following employee was added successfully:\n")
    println(employees.generateEmployeeTable(employee).renderText(border=TextBorder.ROUNDED))
}

fun viewEmployee() {
    logger.debug { "Searching for employees" }

    getEmployeeById() ?: return
}

fun updateEmployee() {
    logger.debug { "Trying to update employee" }

    val employee = getEmployeeById() ?: return

    println("\nPlease enter the new details for the employee (Enter nothing to keep previous value):")

    val updatedEmployee = generateEmployee(employee)
    updatedEmployee.employeeID = employee.employeeID
    updatedEmployee.createdAt = employee.createdAt

    logger.debug { "Employee found, updating employee" }
    employees.update(employee.employeeID, updatedEmployee)

    println("\nThe employee was updated successfully:\n")
    println(employees.generateEmployeeTable(updatedEmployee).renderText(border=TextBorder.ROUNDED))
}

fun deleteEmployee() {
    logger.debug { "Trying to delete employee" }

    val employee = getEmployeeById() ?: return

    logger.debug { "Employee found, deleting employee" }
    employees.delete(employee.employeeID)

    println("\nThe employee was deleted successfully:\n")
}

fun paySlip(){
    logger.debug { "Trying to generate payslip" }
    val employee = getEmployeeById() ?: return

    println()
    logger.debug { "Displaying payslip" }
    println("Here is their payslip:")
    println(employee.getNicerPayslip().renderText(border=TextBorder.ROUNDED))
}

fun viewMultipleEmployees() {
    logger.debug { "Trying to view multiple employees" }

    val employeeList = getMultipleEmployeesByID() ?: return

    println("Here are the employees you wanted to view:")
    println(employees.generateMultipleEmployeesTable(employeeList).renderText(border=TextBorder.ROUNDED))
}

fun deleteMultipleEmployees() {
    logger.debug { "Trying to delete multiple employees" }

    val employeeList = getMultipleEmployeesByID() ?: return

    println("Here are the employees you wanted to delete:")
    println(employees.generateMultipleEmployeesTable(employeeList).renderText(border=TextBorder.ROUNDED))

    print("Are you sure you want to delete these employees? (y/n): ")
    val delete = readln().toCharArray().getOrNull(0)
    if (delete != 'y') {
        println("Employees not deleted.")
        return
    }

    logger.debug { "Deleting multiple employees" }
    employees.deleteMultiple(employeeList.map { it.employeeID })
    println("Employees deleted.")
}

fun listEmployees(){
    logger.debug { "Listing employees" }

    println(employees.generateAllEmployeesTable().renderText(border=TextBorder.ROUNDED))
}

fun loadEmployees() {
    if (employees.loadEmployeesFromFile()) {
        println("Employees loaded successfully:")
        println(employees.generateAllEmployeesTable().renderText(border=TextBorder.ROUNDED))
    } else {
        println("Error loading employees, see debug log for more info")
    }
}

fun saveEmployees() {
    if (employees.saveEmployeesToFile()) {
        println("Employees saved successfully:")
        println(employees.generateAllEmployeesTable().renderText(border=TextBorder.ROUNDED))
    } else {
        println("Error saving employees, see debug log for more info")
    }
}

fun menu() : Int {
    logger.debug { "Displaying menu" }
    // TODO: Maybe extract to a separate file
    println(table {
        cellStyle {
            alignment = TextAlignment.MiddleRight
            paddingLeft = 1
            paddingRight = 1
            borderLeft = true
            borderRight = true
        }
        header {
            row {
                cell("Employee Menu") {
                    columnSpan = 2
                    alignment = TextAlignment.MiddleCenter
                    border = true
                }
            }
        }
        body {
            row {
                cell("1")
                cell("Add Employee")
            }
            row {
                cell("2")
                cell("View Employee")
            }
            row {
                cell("3")
                cell("Update Employee")
            }
            row {
                cell("4")
                cell("Delete Employee")
            }
            row {
                cell("5")
                cell("Print Payslip for Employee")
            }
            row {
                cell("")
                cell("")
            }
            row {
                cell("6")
                cell("View Multiple Employees")
            }
            row {
                cell("7")
                cell("Delete Multiple Employees")
            }
            row {
                cell("")
                cell("")
            }
            row {
                cell("8")
                cell("List All Employees")
            }
            row {
                cell("")
                cell("")
            }
            row {
                cell("9")
                cell("Load Employees from File")
            }
            row {
                cell("10")
                cell("Save Employee to File")
                cellStyle {
                    borderBottom = true
                }
            }
        }
        footer {
            row {
                cell("-1")
                cell("Exit")
                cellStyle {
                    borderBottom = true
                }
            }
        }
    }.renderText(border=TextBorder.ROUNDED))

    print("\nEnter Option: ")
    return readln().toInt()
}

fun start() {
    var input: Int

    do {
        input = menu()
        logger.debug {"Waiting for user choice"}

        println()

        logger.debug {"User choice made"}

        // TODO: Add sorting, filtering, and searching
        // TODO: When features added, update README
        when (input) {
            1 -> addEmployee()
            2 -> viewEmployee()
            3 -> updateEmployee()
            4 -> deleteEmployee()
            5 -> paySlip()
            6 -> viewMultipleEmployees()
            7 -> deleteMultipleEmployees()
            8 -> listEmployees()
            9 -> loadEmployees()
            10 -> saveEmployees()
            -99 -> dummyData()
            -1 -> println("Exiting App")
            else -> println("Invalid Option")
        }
        println()
    } while (input != -1)

    logger.info {"Exiting Employee App"}
}

fun main() {
    // TODO: Finalize README
    logger.info {"Launching Employee App"}
    //dummyData()

    employees.loadEmployeesFromFile()

    logger.debug { "Displaying logo" }
    println("""
          ______                 _                                              
         |  ____|               | |                           /\                
         | |__   _ __ ___  _ __ | | ___  _   _  ___  ___     /  \   _ __  _ __  
         |  __| | '_ ` _ \| '_ \| |/ _ \| | | |/ _ \/ _ \   / /\ \ | '_ \| '_ \ 
         | |____| | | | | | |_) | | (_) | |_| |  __/  __/  / ____ \| |_) | |_) |
         |______|_| |_| |_| .__/|_|\___/ \__, |\___|\___| /_/    \_\ .__/| .__/ 
                          | |             __/ |                    | |   | |    
                          |_|            |___/                     |_|   |_|    
    
    """.trimIndent())

    logger.debug {"Starting menu loop"}
    start()

//    println()
//    println(employees.findOne(0)?.getNicerToString()?.renderText(border= TextBorder.ROUNDED))
}
