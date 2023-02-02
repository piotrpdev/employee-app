package ie.setu

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
    employees.findAll()
        .forEach{ println(it) }
}

fun searchEmployees() {
    val employee = getEmployeeById()
    if (employee == null)
        println("No employee found")
    else
        println(employee)
}

fun generateEmployee(): Employee {
    print("Enter first name: ")
    val firstName = readlnOrNull().toString()
    print("Enter surname: ")
    val surname = readlnOrNull().toString()
    print("Enter gender (m/f): ")
    val gender = readln().toCharArray()[0]
//    print("Enter employee ID: ")
//    val employeeID = readLine()!!.toInt()
    print("Enter gross salary: ")
    val grossSalary = readln().toDouble()
    print("Enter PAYE %: ")
    val payePercentage = readln().toDouble()
    print("Enter PRSI %: ")
    val prsiPercentage = readln().toDouble()
    print("Enter Annual Bonus: ")
    val annualBonus= readln().toDouble()
    print("Enter Cycle to Work Deduction: ")
    val cycleToWorkMonthlyDeduction= readln().toDouble()

    return Employee(firstName, surname, gender, 0, grossSalary, payePercentage, prsiPercentage, annualBonus, cycleToWorkMonthlyDeduction)
}

fun addEmployee() {
    val employee = generateEmployee()

    employees.create(employee)
}

fun updateEmployee() {
    print("Enter the employee id: ")
    val employeeID = readln().toInt()
    val employee = employees.findOne(employeeID)

    if (employee == null) {
        println("Employee not found")
        return
    }

    val updatedEmployee = generateEmployee()

    employees.update(employeeID, updatedEmployee)
}

fun deleteEmployee() {
    TODO("Not yet implemented")
}

internal fun getEmployeeById(): Employee? {
    print("Enter the employee id to search by: ")
    val employeeID = readln().toInt()
    return employees.findOne(employeeID)
}

fun paySlip(){
    val employee = getEmployeeById()
    if (employee != null) {
        println();
        println(employee.getNicerPayslip())
    }
}

fun dummyData() {
    employees.create(Employee("Joe", "Soap", 'm', 0, 35655.43, 31.0, 7.5, 2000.0, 25.6))
    employees.create(Employee("Joan", "Murphy", 'f', 0, 54255.13, 32.5, 7.0, 1500.0, 55.3))
    employees.create(Employee("Mary", "Quinn", 'f', 0, 75685.41, 40.0, 8.5, 4500.0, 0.0))
}

fun menu() : Int {
    print(""" 
         |Employee Menu
         |   1. Add Employee
         |   2. View Employee
         |   3. Update Employee
         |   4. Delete Employee
         |   5. List All Employees
         |   6. Search Employees 
         |   7. Print Payslip for Employee
         |  -1. Exit
         |       
         |Enter Option : """.trimMargin())
    return readln().toInt()
}

fun start() {
    var input: Int

    do {
        logWait("Waiting for user choice")

        input = menu()

        logWait("User choice made")

        when (input) {
            1 -> addEmployee()
            2 -> getEmployeeById()
            3 -> updateEmployee()
            4 -> deleteEmployee()
            5 -> listEmployees()
            6 -> searchEmployees()
            7 -> paySlip()
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
}
