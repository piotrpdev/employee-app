package ie.setu.models

import com.jakewharton.picnic.BorderStyle
import com.jakewharton.picnic.Table
import com.jakewharton.picnic.TextAlignment
import java.math.RoundingMode
import java.text.DecimalFormat
import com.jakewharton.picnic.table
import ie.setu.controllers.roundOff

// TODO: Maybe add timestamps e.g. when the employee was created, last updated, etc.
data class Employee(
    val firstName: String, val surname: String, val gender: Char, var employeeID: Int,
    val grossSalary: Double, val payePercentage: Double, val prsiPercentage: Double,
    val annualBonus: Double, val cycleToWorkSchemeMonthlyDeduction: Double
) {

    fun getFullName(): String {
        val fullName = "$firstName $surname"

        return when (gender) {
            'm', 'M' -> "Mr. $fullName"
            'f', 'F' -> "Ms. $fullName"
            else -> fullName
        }
    }

    private fun getMonthlySalary() = grossSalary / 12
    private fun getMonthlyBonus() = annualBonus / 12
    private fun getMonthlyPRSI() = getMonthlySalary() * (prsiPercentage / 100)
    private fun getMonthlyPAYE() = getMonthlySalary() * (payePercentage / 100)
    private fun getGrossMonthlyPay() = getMonthlySalary() + getMonthlyBonus()
    private fun getTotalMonthlyDeductions() = getMonthlyPAYE() + getMonthlyPRSI() + cycleToWorkSchemeMonthlyDeduction
    private fun getNetMonthlyPay() = getGrossMonthlyPay() - getTotalMonthlyDeductions()

    private fun getTotalYearlyDeductions() = grossSalary * (payePercentage / 100) + grossSalary * (prsiPercentage / 100) + cycleToWorkSchemeMonthlyDeduction * 12

    fun getNicerPayslip(): Table {
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
                    cell("Monthly Payslip") {
                        columnSpan = 5
                        alignment = TextAlignment.MiddleCenter
                        border = true
                    }
                }
                row {
                    cellStyle {
                        border = true
                        alignment = TextAlignment.BottomLeft
                    }
                    cell("Employee") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Income") {
                        columnSpan = 2
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Deductions") {
                        columnSpan = 2
                        alignment = TextAlignment.MiddleCenter
                    }
                }
            }
            body {
                row {
                    cell("${getFullName()} (ID: $employeeID)") {
                        rowSpan = 3
                        paddingLeft = 2
                        paddingRight = 2
                    }
                    cell("Salary") {
                        paddingLeft = 3
                    }
                    cell(roundOff(getMonthlySalary())) {}
                    cell("PAYE") {}
                    cell(roundOff(getMonthlyPAYE())) {}
                }
                row {
                    cell("Bonus") {}
                    cell(roundOff(getMonthlyBonus())) {}
                    cell("PRSI") {}
                    cell(roundOff(getMonthlyPRSI())) {}
                }
                row {
                    cell("") {}
                    cell("") {}
                    cell("Cycle To Work") {
                        paddingLeft = 3
                    }
                    cell(roundOff(cycleToWorkSchemeMonthlyDeduction)) {}
                }
                row {
                    cell("") {}
                    cell("") {}
                    cell("") {}
                    cell("") {}
                    cell("") {}
                }
            }
            footer {
                cellStyle {
                    border = true
                }
                row {
                    cell("Total") {
                        columnSpan = 2
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell(roundOff(getGrossMonthlyPay())) {}
                    cell("") {}
                    cell(roundOff(getTotalMonthlyDeductions())) {}
                }
                row {
                    cell("Net Pay") {
                        columnSpan = 4
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell(roundOff(getNetMonthlyPay())) {}
                }
            }
        }
    }

    override fun toString(): String {
        return "Employee(firstName='$firstName', surname='$surname', gender=$gender, employeeID=$employeeID, grossSalary=$grossSalary, payePercentage=$payePercentage, prsiPercentage=$prsiPercentage, annualBonus=$annualBonus, cycleToWorkSchemeMonthlyDeduction=$cycleToWorkSchemeMonthlyDeduction)"
    }

    fun getYearlyInfo(): Table {
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
                    cell("Yearly Information") {
                        columnSpan = 5
                        alignment = TextAlignment.MiddleCenter
                        border = true
                    }
                }
                row {
                    cellStyle {
                        border = true
                        alignment = TextAlignment.BottomLeft
                    }
                    cell("Employee") {
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Income") {
                        columnSpan = 2
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell("Deductions") {
                        columnSpan = 2
                        alignment = TextAlignment.MiddleCenter
                    }
                }
            }
            body {
                row {
                    cell("${getFullName()} (ID: $employeeID)") {
                        rowSpan = 3
                        paddingLeft = 2
                        paddingRight = 2
                    }
                    cell("Salary") {
                        paddingLeft = 3
                    }
                    cell(roundOff(grossSalary)) {}
                    cell("PAYE") {}
                    cell(roundOff(payePercentage) + "%") {}
                }
                row {
                    cell("Bonus") {}
                    cell(roundOff(annualBonus)) {}
                    cell("PRSI") {}
                    cell(roundOff(prsiPercentage) + "%") {}
                }
                row {
                    cell("") {}
                    cell("") {}
                    cell("Cycle To Work") {
                        paddingLeft = 3
                    }
                    cell(roundOff(cycleToWorkSchemeMonthlyDeduction * 12)) {}
                }
                row {
                    cell("") {}
                    cell("") {}
                    cell("") {}
                    cell("") {}
                    cell("") {}
                }
            }
            footer {
                cellStyle {
                    border = true
                }
                row {
                    cell("Total") {
                        columnSpan = 2
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell(roundOff(grossSalary + annualBonus)) {}
                    cell("") {}
                    cell(roundOff(getTotalYearlyDeductions())) {}
                }
                row {
                    cell("Net Pay") {
                        columnSpan = 4
                        alignment = TextAlignment.MiddleCenter
                    }
                    cell(roundOff((grossSalary + annualBonus) - getTotalYearlyDeductions())) {}
                }
            }
        }
    }
}