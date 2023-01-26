package ie.setu

import java.math.RoundingMode
import java.text.DecimalFormat

var employee =  Employee("Joe", "Soap", 'm', 6143, 67543.21, 38.5, 5.2, 1450.50, 54.33)

// Taken from https://stackoverflow.com/a/54665180
fun roundOff(number: Double): Double {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.HALF_UP
    return df.format(number).toDouble()
}

fun getPayslip(): String {
    return """
            ------------------------------------------------------------------
               Monthly Payslip for ${employee.getFullName()} (ID: ${employee.employeeID})               
            __________________________________________________________________ 
               PAYMENT DETAILS (TOTAL: ${roundOff(employee.getGrossMonthlyPay())})
            __________________________________________________________________
                    Salary: ${roundOff(employee.getMonthlySalary())}
                    Bonus: ${roundOff(employee.getMonthlyBonus())}
            __________________________________________________________________                                   
               DEDUCTION DETAILS (TOTAL: ${roundOff(employee.getTotalMonthlyDeductions())})
            __________________________________________________________________
                    PAYE: ${roundOff(employee.getMonthlyPAYE())}
                    PRSI: ${roundOff(employee.getMonthlyPRSI())}
                    Cycle To Work: ${employee.cycleToWorkSchemeMonthlyDeduction}
            __________________________________________________________________
                NET PAY: ${roundOff(employee.getNetMonthlyPay())}                        
            __________________________________________________________________ 
        """.trimIndent()
}

fun add() {
    print("Enter first name: ")
    val firstName = readLine().toString()
    print("Enter surname: ")
    val surname = readLine().toString()
    print("Enter gender (m/f): ")
    val gender = readLine()!!.toCharArray()[0]
    print("Enter employee ID: ")
    val employeeID = readLine()!!.toInt()
    print("Enter gross salary: ")
    val grossSalary = readLine()!!.toDouble()
    print("Enter PAYE %: ")
    val payePercentage = readLine()!!.toDouble()
    print("Enter PRSI %: ")
    val prsiPercentage = readLine()!!.toDouble()
    print("Enter Annual Bonus: ")
    val annualBonus= readLine()!!.toDouble()
    print("Enter Cycle to Work Deduction: ")
    val cycleToWorkMonthlyDeduction= readLine()!!.toDouble()

    employee = Employee(firstName, surname, gender, employeeID, grossSalary, payePercentage, prsiPercentage, annualBonus, cycleToWorkMonthlyDeduction)
}


fun menu() : Int {
    print("""
         Employee Menu for ${employee.getFullName()}
           1. Monthly Salary
           2. Monthly PRSI
           3. Monthly PAYE
           4. Monthly Gross Pay
           5. Monthly Total Deductions
           6. Monthly Net Pay
           7. Full Payslip
           --------------------
           8. Add Employee
           --------------------
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
            1 -> println("Monthly Salary: ${employee.getMonthlySalary()}")
            2 -> println("Monthly PRSI: ${employee.getMonthlyPRSI()}")
            3 ->println("Monthly PAYE: ${employee.getMonthlyPAYE()}")
            4 -> println("Monthly Gross Pay: ${employee.getGrossMonthlyPay()}")
            5 -> println("Monthly Total Deductions: ${employee.getTotalMonthlyDeductions()}")
            6 -> println("Monthly Net Pay: ${employee.getNetMonthlyPay()}")
            7 -> println(getPayslip())
            8 -> add()
            -1 -> println("Exiting App")
            else -> println("Invalid Option")
        }

        println()

    } while (input != -1)
}
