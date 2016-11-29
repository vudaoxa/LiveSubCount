package net.lc.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil

import java.text.Normalizer
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Dieu on 05/09/2016.
 */
object InputUtil {
//    fun isEmailValid(email: String): Boolean {
//        var isValid = false
//
//        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
//        val inputStr = email
//
//        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
//        val matcher = pattern.matcher(inputStr)
//        if (matcher.matches()) {
//            isValid = true
//        }
//        return isValid
//    }
//
//    fun isValidMobile(phone: String): Boolean {
//        return android.util.Patterns.PHONE.matcher(phone).matches()
//    }
//
//    fun isValidPassword(pass: String): Boolean {
//        return pass.length > 3
//    }
//
//    fun isValidLinkUrl(url: String): Boolean {
//        return URLUtil.isValidUrl(url)
//    }
//
//    fun removeAccent(s: String): String {
//        val temp = Normalizer.normalize(s, Normalizer.Form.NFD)
//        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
//        return pattern.matcher(temp).replaceAll("").replace("Đ".toRegex(), "D").replace("đ", "d")
//    }
//
//    fun capitalFristCharacters(s: String): String {
//        if (s.length > 0) {
//            val output = s.substring(0, 1).toUpperCase() + s.substring(1)
//            return output
//        }
//        return s
//    }
//
//    fun isNumeric(str: String): Boolean {
//        return str.matches("-?\\d+(.\\d+)?".toRegex())
//    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
