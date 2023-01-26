package ie.setu

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
}