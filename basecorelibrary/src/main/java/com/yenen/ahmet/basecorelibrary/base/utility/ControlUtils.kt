package com.yenen.ahmet.basecorelibrary.base.utility

import java.util.regex.Pattern

object ControlUtils {

    fun isEmailValid(email: String): Boolean {
        if (email.trim().length > 3) {
            return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }
        return false
    }


     fun isValidTCN(value: String): Boolean { // 01234567891
        val charAt10 = Character.digit(value[10], 10)
        if (value.length == 11 && value.substring(0, 1) != "0" && charAt10 % 2 == 0) {
            var singleDigits = 0
            var doubleDigits = 0
            for (i in 0..8) {
                if (i % 2 == 0) { // tekler 13579
                    singleDigits += Character.digit(value[i], 10)
                } else { // çiftler 2468
                    doubleDigits += Character.digit(value[i], 10)
                }
            }
            val totalDigitCalculation = (singleDigits * 7 - doubleDigits) % 10
            val charAt9 = Character.digit(value[9], 10)
            if (totalDigitCalculation == charAt9) {
                return (singleDigits + doubleDigits + charAt9) % 10 == charAt10
            }
        }
        return false
    }

    private fun isValidVKN(value: String): Boolean {
        if (value.length == 10) {
            var totalCalculate = 0
            for (i in 0..8) {
                val charti = Character.digit(value[i], 10)
                var calculate = (charti + 9 - i) % 10
                if (calculate != 9) {
                    val math = Math.pow(2.0, (9 - i).toDouble()).toInt()
                    calculate = calculate * math % 9
                }
                totalCalculate += calculate
            }
            val totalModule = (10 - totalCalculate % 10) % 10
            return totalModule == Character.digit(value[9], 10)
        }
        return false
    }
}