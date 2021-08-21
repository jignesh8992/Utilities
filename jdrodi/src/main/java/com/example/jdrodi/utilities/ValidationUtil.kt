@file:Suppress("unused")

package com.example.jdrodi.utilities

import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Toast.kt - A simple class for validations.
 * @author  Jignesh N Patel
 * @date 07-11-2019
 */


/**
 *  Check if enter email address is valid or not
 *
 * @return true if valid otherwise false
 */

@Suppress("unused")
fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}


/**
 *  Check if enter password is match with password criteria
 *
 * @return true if matched otherwise false
 */
@Suppress("unused")
fun String.isValidPassword(): Boolean {
    val pattern: Pattern
    val matcher: Matcher
    val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
    pattern = Pattern.compile(passwordPattern)
    matcher = pattern.matcher(this)
    return matcher.matches()
}


fun EditText.isValidContactInformation(): Boolean {
    val input: String = text.toString()
    return when {
        input.contains("@") -> {
            input.isValidEmail()
        }
        input.isValidPhone() -> {
            return true
        }
        else -> {
            return false
        }
    }
}

fun String.isValidPhone(): Boolean {
    val pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$"
    val r: Pattern = Pattern.compile(pattern)
    return r.matcher(this).matches()
}


// Function to validate the pin code of India.
fun String.isValidPinCode(): Boolean {

    // Regex to check valid pin code of India.
    val regex = "^[1-9][0-9]{2}\\s?[0-9]{3}$"

    // Compile the ReGex
    val p: Pattern = Pattern.compile(regex)

    // Pattern class contains matcher() method
    // to find matching between given pin code
    // and regular expression.
    val m: Matcher = p.matcher(this)

    // Return if the pin code
    // matched the ReGex
    return m.matches()
}