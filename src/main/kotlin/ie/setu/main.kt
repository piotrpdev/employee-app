package ie.setu

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.TextBorder
import com.jakewharton.picnic.renderText
import com.jakewharton.picnic.table
import ie.setu.controllers.EmployeeAPI
import ie.setu.models.Employee
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

internal fun logWait(string: String) {
    logger.debug { string }
    Thread.sleep(50)
}

var employees = EmployeeAPI()

fun listEmployees(){
    println(employees.generateAllEmployeesTable().renderText(border=TextBorder.ROUNDED))
}

fun searchEmployees() {
    val employee = getEmployeeById()
    if (employee == null)
        println("No employee found with that ID.")
    else
        println(employees.generateEmployeeTable(employee).renderText(border=TextBorder.ROUNDED))
}

fun generateEmployee(): Employee {
    print("Enter first name: ")
    var firstName = readlnOrNull().toString()
    while (firstName.isBlank()) {
        println("Error: first name cannot be empty. Please enter a valid first name.")
        firstName = readlnOrNull().toString()
    }

    print("Enter surname: ")
    var surname = readlnOrNull().toString()
    while (surname.isBlank()) {
        println("Error: surname cannot be empty. Please enter a valid surname.")
        surname = readlnOrNull().toString()
    }

    print("Enter gender (m/f): ")
    var gender = readln().toCharArray().getOrNull(0)
    while (gender != 'm' && gender != 'f') {
        println("Error: gender must be either 'm' or 'f'. Please enter a valid gender.")
        gender = readln().toCharArray()[0]
    }

    print("Enter gross salary: ")
    var grossSalary = readln().toDoubleOrNull()
    while (grossSalary == null) {
        println("Error: gross salary must be a valid number. Please enter a valid gross salary.")
        grossSalary = readln().toDoubleOrNull()
    }

    print("Enter PAYE %: ")
    var payePercentage = readln().toDoubleOrNull()
    while (payePercentage == null || payePercentage < 0 || payePercentage > 100) {
        println("Error: PAYE must be a valid number between 0 and 100. Please enter a valid PAYE.")
        payePercentage = readln().toDoubleOrNull()
    }

    print("Enter PRSI %: ")
    var prsiPercentage = readln().toDoubleOrNull()
    while (prsiPercentage == null || prsiPercentage < 0 || prsiPercentage > 100) {
        println("Error: PRSI must be a valid number between 0 and 100. Please enter a valid PRSI.")
        prsiPercentage = readln().toDoubleOrNull()
    }

    print("Enter Annual Bonus: ")
    var annualBonus = readln().toDoubleOrNull()
    while (annualBonus == null) {
        println("Error: annual bonus must be a valid number. Please enter a valid annual bonus.")
        annualBonus = readln().toDoubleOrNull()
    }

    print("Enter Cycle to Work Deduction: ")
    var cycleToWorkMonthlyDeduction = readln().toDoubleOrNull()
    while (cycleToWorkMonthlyDeduction == null) {
        println("Error: cycle to work monthly deduction must be a valid number. Please enter a valid cycle to work monthly deduction.")
        cycleToWorkMonthlyDeduction = readln().toDoubleOrNull()
    }

    return Employee(firstName, surname, gender, 0, grossSalary, payePercentage, prsiPercentage, annualBonus, cycleToWorkMonthlyDeduction)
}

fun addEmployee() {
    val employee = generateEmployee()

    employees.create(employee)
    println("\nThe following employee was added successfully:\n")
    println(employees.generateEmployeeTable(employee).renderText(border=TextBorder.ROUNDED))
}

fun updateEmployee() {
    print("Enter the employee id to search by: ")
    var employeeID = readln().toIntOrNull()
    while (employeeID == null) {
        println("Error: employee id must be a valid number. Please enter a valid employee id.")
        employeeID = readln().toIntOrNull()
    }

    val employee = employees.findOne(employeeID)

    if (employee == null) {
        println("Employee not found with that ID.")
        return
    }

    val updatedEmployee = generateEmployee()
    updatedEmployee.employeeID = employee.employeeID

    employees.update(employeeID, updatedEmployee)
    println("\nThe following employee was updated successfully:\n")
    println(employees.generateEmployeeTable(updatedEmployee).renderText(border=TextBorder.ROUNDED))
}

fun deleteEmployee() {
    print("Enter the employee id to search by: ")
    var employeeID = readln().toIntOrNull()
    while (employeeID == null) {
        println("Error: employee id must be a valid number. Please enter a valid employee id.")
        employeeID = readln().toIntOrNull()
    }

    val employee = employees.findOne(employeeID)

    if (employee == null) {
        println("Employee not found with that ID.")
        return
    }

    employees.delete(employeeID)
    println("\nThe following employee was deleted successfully:\n")
    println(employees.generateEmployeeTable(employee).renderText(border=TextBorder.ROUNDED))
}

internal fun getEmployeeById(): Employee? {
    print("Enter the employee id to search by: ")
    var employeeID = readln().toIntOrNull()
    while (employeeID == null) {
        println("Error: employee id must be a valid number. Please enter a valid employee id.")
        employeeID = readln().toIntOrNull()
    }

    return employees.findOne(employeeID)
}

fun paySlip(){
    val employee = getEmployeeById()
    if (employee != null) {
        println();
        println(employee.getNicerPayslip().renderText(border=TextBorder.ROUNDED))
    }
}

fun dummyData() {
    employees.create(Employee("Joe", "Soap", 'm', 0, 35655.43, 31.0, 7.5, 2000.0, 25.6))
    employees.create(Employee("Joan", "Murphy", 'f', 0, 54255.13, 32.5, 7.0, 1500.0, 55.3))
    employees.create(Employee("Mary", "Quinn", 'f', 0, 75685.41, 40.0, 8.5, 4500.0, 0.0))
}

fun menu() : Int {
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
                cell("List All Employees")
            }
            row {
                cell("6")
                cell("Print Payslip for Employee")
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
        logWait("Waiting for user choice")

        input = menu()
        println()

        logWait("User choice made")

        when (input) {
            1 -> addEmployee()
            2 -> searchEmployees()
            3 -> updateEmployee()
            4 -> deleteEmployee()
            5 -> listEmployees()
            6 -> paySlip()
            -99 -> dummyData()
            -1 -> println("Exiting App")
            else -> println("Invalid Option")
        }
        println()
    } while (input != -1)
}

fun main() {
    logWait("Launching Employee App" )
    logWait("Adding dummy data" )
    dummyData()

    logWait("Starting menu loop" )
    start()

//    println()
//    println(employees.findOne(0)?.getNicerToString()?.renderText(border= TextBorder.ROUNDED))
}
