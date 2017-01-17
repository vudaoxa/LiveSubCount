package net.lc.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by mrvu on 1/9/17.
 */
object MDateUtils {

    fun currentDate(): String {
        val sdf = SimpleDateFormat("M-d-yy")
        val date = Date()
        return sdf.format(date) + ".txt"
    }
}