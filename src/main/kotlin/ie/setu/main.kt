package ie.setu

import java.math.RoundingMode
import java.text.DecimalFormat

const val firstName = "Joe"
const val surname = "Soap"
const val gender = "m"
const val employeeId = 6143
const val grossSalary = 67_543.21
const val payePercentage = 0.385
const val prsiPercentage = 0.052
const val annualBonusAmount = 1450.50
const val cycleToWorkSchemeMonthlyDeduction = 54.33

fun getFullName() = "${if (gender == "m") "MR." else "MS."} ${firstName.uppercase()} ${surname.uppercase()}"

fun getMonthlySalary() = grossSalary / 12
fun getMonthlyBonus() = annualBonusAmount / 12
fun getMonthlyPRSI() = getMonthlySalary() * prsiPercentage
fun getMonthlyPAYE() = getMonthlySalary() * payePercentage
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
                Monthly Payslip                         
            __________________________________________________________________
                    Employee ID: $employeeId                                                               
                    Employee Name: ${getFullName()}                                                            
            __________________________________________________________________                                           
               PAYMENT DETAILS
            __________________________________________________________________
                    Salary: ${roundOff(getMonthlySalary())}
                    Bonus: ${roundOff(getMonthlyBonus())}
            __________________________________________________________________                                   
               DEDUCTION DETAILS
            __________________________________________________________________
                    PAYE: ${roundOff(getMonthlyPAYE())}
                    PRSI: ${roundOff(getMonthlyPRSI())}
                    Cycle To Work: $cycleToWorkSchemeMonthlyDeduction     
            __________________________________________________________________
                TOTALS
            __________________________________________________________________ 
                    Gross:  ${roundOff(getGrossMonthlyPay())}
                    Total Deductions: ${roundOff(getTotalMonthlyDeductions())}
            __________________________________________________________________
                    NET PAY: ${roundOff(getNetMonthlyPay())}                        
            __________________________________________________________________ 
        """.trimIndent()
}

fun main(args: Array<String>) {
    println(getPayslip())
}