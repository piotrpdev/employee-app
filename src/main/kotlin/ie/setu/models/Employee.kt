package ie.setu.models

import com.jakewharton.picnic.BorderStyle
import com.jakewharton.picnic.Table
import com.jakewharton.picnic.TextAlignment
import java.math.RoundingMode
import java.text.DecimalFormat
import com.jakewharton.picnic.table

// Taken from https://stackoverflow.com/a/54665180
fun roundOff(number: Double): String {
    val df = DecimalFormat("#.00")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(number)
}

class Employee(
    private var firstName: String, private var surname: String, private var gender: Char, var employeeID: Int,
    private var grossSalary: Double, private var payePercentage: Double, private var prsiPercentage: Double,
    private var annualBonus: Double, private var cycleToWorkSchemeMonthlyDeduction: Double
) {

    private fun getFullName(): String {
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

    fun getNicerPayslip(): Table {
        return table {
            style {
                borderStyle = BorderStyle.Hidden
            }
            cellStyle {
                alignment = TextAlignment.MiddleRight
                paddingLeft = 1
                paddingRight = 1
                borderLeft = true
                borderRight = true
            }
            header {
                cellStyle {
                    border = true
                    alignment = TextAlignment.BottomLeft
                }
                row {
                    cell("Name") {
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
                    cell("${getFullName()} (ID: ${employeeID})") {
                        rowSpan = 3
                    }
                    cell("Salary") {}
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
                    cell("Cycle To Work") {}
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

    fun getPayslip(): String {
        return """
            ------------------------------------------------------------------
               Monthly Payslip for ${getFullName()} (ID: ${employeeID})               
            __________________________________________________________________ 
               PAYMENT DETAILS (TOTAL: ${roundOff(getGrossMonthlyPay())})
            __________________________________________________________________
                    Salary: ${roundOff(getMonthlySalary())}
                    Bonus: ${roundOff(getMonthlyBonus())}
            __________________________________________________________________                                   
               DEDUCTION DETAILS (TOTAL: ${roundOff(getTotalMonthlyDeductions())})
            __________________________________________________________________
                    PAYE: ${roundOff(getMonthlyPAYE())}
                    PRSI: ${roundOff(getMonthlyPRSI())}
                    Cycle To Work: $cycleToWorkSchemeMonthlyDeduction
            __________________________________________________________________
                NET PAY: ${roundOff(getNetMonthlyPay())}                        
            __________________________________________________________________ 
        """.trimIndent()
    }

    override fun toString(): String {
        return "Employee(firstName='$firstName', surname='$surname', gender=$gender, employeeID=$employeeID, grossSalary=$grossSalary, payePercentage=$payePercentage, prsiPercentage=$prsiPercentage, annualBonus=$annualBonus, cycleToWorkSchemeMonthlyDeduction=$cycleToWorkSchemeMonthlyDeduction)"
    }

}