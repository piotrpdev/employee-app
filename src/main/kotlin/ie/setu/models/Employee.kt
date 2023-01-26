package ie.setu.models

import java.math.RoundingMode
import java.text.DecimalFormat

class Employee(
    private var firstName: String, private var surname: String, private var gender: Char, var employeeID: Int,
    private var grossSalary: Double, private var payePercentage: Double, private var prsiPercentage: Double,
    private var annualBonus: Double, var cycleToWorkSchemeMonthlyDeduction: Double
) {

    fun getFullName(): String {
        val fullName = "$firstName $surname"

        return when (gender) {
            'm', 'M' -> "Mr. $fullName"
            'f', 'F' -> "Ms. $fullName"
            else -> fullName
        }
    }

    fun getMonthlySalary() = grossSalary / 12
    fun getMonthlyBonus() = annualBonus / 12
    fun getMonthlyPRSI() = getMonthlySalary() * (prsiPercentage / 100)
    fun getMonthlyPAYE() = getMonthlySalary() * (payePercentage / 100)
    fun getGrossMonthlyPay() = getMonthlySalary() + getMonthlyBonus()
    fun getTotalMonthlyDeductions() = getMonthlyPAYE() + getMonthlyPRSI() + cycleToWorkSchemeMonthlyDeduction
    fun getNetMonthlyPay() = getGrossMonthlyPay() - getTotalMonthlyDeductions()

    // Taken from https://stackoverflow.com/a/54665180
    fun roundOff(number: Double): Double {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP
        return df.format(number).toDouble()
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