package ie.setu

import java.math.RoundingMode
import java.text.DecimalFormat

const val firstName = "Joe"
const val surname = "Soap"
const val gender = 'm'
const val employeeId = 6143
const val grossSalary = 67_543.21
const val payePercentage = 0.385
const val prsiPercentage = 0.052
const val annualBonusAmount = 1450.50
const val cycleToWorkSchemeMonthlyDeduction = 54.33

fun getFullName(): String {
    val fullName = "$firstName $surname"

    return when (gender) {
        'm', 'M' -> "Mr. $fullName"
        'f', 'F' -> "Ms. $fullName"
        else -> fullName
    }
}

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
                Monthly Payslip for ${getFullName()} (ID: $employeeId)                   
            __________________________________________________________________
            
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

fun menu() : Int {
    print("""
         Employee Menu for ${getFullName()}
           1. Monthly Salary
           2. Monthly PRSI
           3. Monthly PAYE
           4. Monthly Gross Pay
           5. Monthly Total Deductions
           6. Monthly Net Pay
           7. Full Payslip
          -1. Exit
         Enter Option : """)
    return readln().toInt()
}


fun main(args: Array<String>){
    var input: Int

    do {
        input = menu()

        println()

        when(input) {
            1 -> println("Monthly Salary: ${getMonthlySalary()}")
            2 -> println("Monthly PRSI: ${getMonthlyPRSI()}")
            3 ->println("Monthly PAYE: ${getMonthlyPAYE()}")
            4 -> println("Monthly Gross Pay: ${getGrossMonthlyPay()}")
            5 -> println("Monthly Total Deductions: ${getTotalMonthlyDeductions()}")
            6 -> println("Monthly Net Pay: ${getNetMonthlyPay()}")
            7 -> println(getPayslip())
            -1 -> println("Exiting App")
            else -> println("Invalid Option")
        }

        println()

    } while (input != -1)
}
