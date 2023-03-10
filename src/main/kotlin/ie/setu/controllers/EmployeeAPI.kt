package ie.setu.controllers

import com.jakewharton.picnic.Table
import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import ie.setu.logger
import ie.setu.models.Employee
import java.io.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// Taken from https://stackoverflow.com/a/54665180
fun roundOff(number: Double): String {
    val df = DecimalFormat("#,###,###.00")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(number)
}

fun formatDate(date: LocalDateTime): String {
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(date)
}

class EmployeeAPI {

    var lastId = 0

    private fun getId(): Int {
        return lastId++
    }

    private var employees = ArrayList<Employee>()

    fun create(employee: Employee): Int {
        employee.employeeID = getId()
        employees.add(employee)
        logger.debug {"Employee created (ID: ${employee.employeeID})"}
        return employee.employeeID
    }

    fun update(id: Int, updatedEmployee: Employee) {
        updatedEmployee.updatedAt = LocalDateTime.now()
        employees.replaceAll { if (it.employeeID == id) updatedEmployee else it }
        logger.debug {"Employee updated (ID: $id)"}
    }

    fun delete(id: Int) {
        employees.removeIf { it.employeeID == id }
        logger.debug {"Employee deleted (ID: $id)"}
    }

    fun deleteMultiple(ids: List<Int>) {
        employees.removeIf { ids.contains(it.employeeID) }
        logger.debug {"Employees deleted (Multiple) (IDs: $ids)"}
    }

    fun findOne(id: Int): Employee? {
        return employees.find { p -> p.employeeID == id }
    }

    fun findAll(): MutableList<Employee> {
        return employees.toMutableList()
    }

    private fun employeeInfoTemplate(title: String, data: List<Employee>): Table {
        return table {
            cellStyle {
                alignment = TextAlignment.MiddleRight
                paddingLeft = 1
                paddingRight = 1
                borderLeft = true
                borderRight = true
            }
            header {
                row {
                    cell(title) {
                        columnSpan = 8
                        alignment = TextAlignment.MiddleCenter
                        border = true
                    }
                }
                row {
                    cellStyle {
                        border = true
                        alignment = TextAlignment.BottomLeft
                    }
                    cell("ID") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Name") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Gender") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Gross Salary") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Annual Bonus") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("PAYE %") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("PRSI %") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Cycle to Work Scheme Monthly Deduction") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Updated At") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Created At") {
                        alignment = TextAlignment.MiddleCenter
                    }
                }
            }
            body {
                    data.forEachIndexed { index, it ->
                        row {
                            cell(it.employeeID.toString()) {}
                            cell("${it.firstName} ${it.surname}") {}
                            cell(if (it.gender == 'm') "Male" else "Female") {}
                            cell(roundOff(it.grossSalary)) {}
                            cell(roundOff(it.annualBonus)) {}
                            cell(roundOff(it.payePercentage) + "%") {}
                            cell(roundOff(it.prsiPercentage) + "%") {}
                            cell(roundOff(it.cycleToWorkSchemeMonthlyDeduction)) {}
                            cell(formatDate(it.updatedAt)) {}
                            cell(formatDate(it.createdAt)) {}
                            if (index == data.size - 1) {
                                cellStyle {
                                    borderBottom = true
                                }
                            }
                        }
                    }
            }
        }
    }

    fun generateEmployeeTable(employee: Employee): Table {
        return employeeInfoTemplate("Employee Information", listOf(employee))
    }

    fun generateMultipleEmployeesTable(employees: List<Employee>): Table {
        return employeeInfoTemplate("Multiple Employee Information", employees)
    }

    fun generateAllEmployeesTable(): Table {
        val sortedEmployees = employees.sortedBy { it.employeeID }

        return employeeInfoTemplate("All Employee Information", sortedEmployees)
    }

    fun saveEmployeesToFile(): Boolean {
        // ? https://stackoverflow.com/questions/57758314/store-custom-kotlin-data-class-to-disk
        logger.debug {"Saving employees to file"}

        try {
            ObjectOutputStream(FileOutputStream("employees.tmp")).use {
                it.writeObject(employees)

                logger.debug {"Saved ${employees.size} employees to file"}
                return true
            }
        } catch (e: Exception) {
            logger.debug {"Error saving employees file: ${e.message}"}
        }

        return false
    }

    fun loadEmployeesFromFile(): Boolean {
        // ? https://stackoverflow.com/questions/57758314/store-custom-kotlin-data-class-to-disk
        logger.debug {"Loading employees from file"}

        try {
            ObjectInputStream(FileInputStream("employees.tmp")).use { it ->
                @Suppress("UNCHECKED_CAST") // https://stackoverflow.com/questions/64041447/in-kotlin-is-there-a-safe-way-to-do-objectinputstream-readobject
                employees = it.readObject() as ArrayList<Employee>
                lastId = employees.maxWith(Comparator.comparingInt {it.employeeID}).employeeID + 1

                logger.debug {"Loaded ${employees.size} employees from file"}
                return true
            }
        } catch (e: FileNotFoundException) {
            logger.debug {"No employees file found"}
        } catch (e: Exception) {
            logger.debug {"Error reading employees file: ${e.message}"}
        }

        return false
    }
}