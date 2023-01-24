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

// Taken from https://stackoverflow.com/a/54665180
fun roundOff(number: Double): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(number).toDouble()
}

fun main(args: Array<String>) {
    val fullName = "${firstName.uppercase()} ${surname.uppercase()}(${gender.uppercase()})"

    val monthlySalary = grossSalary / 12
    val bonus = annualBonusAmount / 12

    val paye = monthlySalary * payePercentage
    val prsi = monthlySalary * prsiPercentage

    val gross = monthlySalary + bonus
    val deducts = paye + prsi + cycleToWorkSchemeMonthlyDeduction
    val net = gross - deducts

    printPayslip(monthlySalary, bonus, paye, prsi, gross, deducts, net)
}

fun printPayslip(monthlySalary: Double, bonus: Double, paye: Double, prsi: Double, gross: Double, deducts: Double, net: Double) {
    val fullName = "${firstName.uppercase()} ${surname.uppercase()}(${gender.uppercase()})"

    println(
        """
            ------------------------------------------------------------------
                Monthly Payslip                         
            __________________________________________________________________
                    Employee ID: $employeeId                                                               
                    Employee Name: $fullName                                                            
            __________________________________________________________________                                           
               PAYMENT DETAILS
            __________________________________________________________________
                    Salary: ${roundOff(monthlySalary)}
                    Bonus: ${roundOff(bonus)}
            __________________________________________________________________                                   
               DEDUCTION DETAILS
            __________________________________________________________________
                    PAYE: ${roundOff(paye)}
                    PRSI: ${roundOff(prsi)}
                    Cycle To Work: $cycleToWorkSchemeMonthlyDeduction     
            __________________________________________________________________
                TOTALS
            __________________________________________________________________ 
                    Gross:  ${roundOff(gross)}
                    Total Deductions: ${roundOff(deducts)}
            __________________________________________________________________
                    NET PAY: ${roundOff(net)}                        
            __________________________________________________________________ 
        """.trimIndent()
    )
}