package ie.setu.controllers

import ie.setu.logger
import ie.setu.models.Employee

var lastId = 0

internal fun getId(): Int {
    return lastId++
}

internal fun logWait(string: String) {
    logger.debug { string }
    Thread.sleep(50)
}

class EmployeeAPI {

    private val employees = ArrayList<Employee>()

    fun create(employee: Employee) {
        employee.employeeID = getId()
        employees.add(employee)
        logWait("Employee created (ID: ${employee.employeeID})")
    }

    fun update(id: Int, updatedEmployee: Employee) {
        employees.replaceAll { if (it.employeeID == id) updatedEmployee else it }
        logWait("Employee updated (ID: $id)")
    }

    fun delete(id: Int) {
        employees.removeIf { it.employeeID == id }
        logWait("Employee deleted (ID: $id)")
    }

    fun findAll(): List<Employee> {
        return employees
    }

    fun findOne(id: Int): Employee? {
        return employees.find { p -> p.employeeID == id }
    }
}