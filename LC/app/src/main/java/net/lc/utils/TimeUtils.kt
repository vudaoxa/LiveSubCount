package vn.mycare.member.utils

import android.content.Context
import com.tieudieu.util.DebugLog
import net.live.sub.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by mrvu on 1/20/17.
 */
object TimeUtils {
    var weekDatesMaps = HashMap<Int, Int>()
    var monthsMaps = HashMap<Int, Int>()

    init {
        initWeekDatesMaps()
        initMonthMaps()
    }

    fun initWeekDatesMaps() {
        weekDatesMaps.put(2, R.string.mon)
        weekDatesMaps.put(3, R.string.tue)
        weekDatesMaps.put(4, R.string.wed)
        weekDatesMaps.put(5, R.string.thu)
        weekDatesMaps.put(6, R.string.fri)
        weekDatesMaps.put(7, R.string.sat)
        weekDatesMaps.put(1, R.string.sun)
    }

    fun initMonthMaps() {
        monthsMaps.put(1, R.string.jan)
        monthsMaps.put(2, R.string.feb)
        monthsMaps.put(3, R.string.mar)
        monthsMaps.put(4, R.string.apr)
        monthsMaps.put(5, R.string.may)
        monthsMaps.put(6, R.string.jun)
        monthsMaps.put(7, R.string.jul)
        monthsMaps.put(8, R.string.aug)
        monthsMaps.put(9, R.string.sep)
        monthsMaps.put(10, R.string.oct)
        monthsMaps.put(11, R.string.nov)
        monthsMaps.put(12, R.string.dec)
    }

    fun toFbFormatTime(context: Context, dt: String): String {
        val now = Date()
        val dateFormat = SimpleDateFormat("hh:mm dd/MM/yyyy")

        val timeFactors = dt.split(" ")
        val h = timeFactors[1].split(":")
        val hour = h[0].toInt()
        val minute = h[1].toInt()
        val d = timeFactors[0].split("-")
        val date = d[2].toInt()
        val month = d[1].toInt()
        val year = d[0].toInt()
        val mDate = dateFormat.parse("$hour:$minute $date/$month/$year")

        //to get day of week
        val cal = Calendar.getInstance()
        cal.set(Calendar.DATE, date)
        cal.set(Calendar.MONTH, month - 1)
        cal.set(Calendar.YEAR, year)
        val time = String.format("%02d:%02d", hour, minute)
        var res = ""
        val yxx = now.year + 1900
        if (year < yxx - 1) {
            res = context.getString(R.string.date1, date, month, year)
        } else {
            val tt = if (year == yxx - 1) 12 else 0
            val countMonth = tt + now.month + 1 - month
            if (countMonth > 2) {
                res = context.getString(R.string.date1, date, month, year)
            } else {
                val countDate = TimeUnit.DAYS.convert(now.time - mDate.time, TimeUnit.MILLISECONDS)
                if (countDate > 7) {
                    res = context.getString(R.string.date2, date, month, time)
                } else {
                    if (countDate < 2) {
                        val countHours = TimeUnit.HOURS.convert(now.time - mDate.time, TimeUnit.MILLISECONDS)
                        if (countHours < 24) {
                            if (countHours < 1) {
                                val countMinute = TimeUnit.MINUTES.convert(now.time - mDate.time, TimeUnit.MILLISECONDS)
                                if (countMinute < 1) {
                                    res = context.getString(R.string.just_now)
                                } else {
                                    res = context.getString(R.string.mins_ago, countMinute)
                                }
                            } else {
                                res = context.getString(R.string.hours_ago, countHours)
                            }
                        } else {
                            res = context.getString(R.string.yesterday, time)
                        }
                    } else {
                        val weekDay = cal.get(Calendar.DAY_OF_WEEK)
                        res = context.getString(R.string.week_date, context.getString(weekDatesMaps.get(weekDay)!!), time)
                    }
                }
            }
        }
        return res
    }

    fun toFbFormatTime(context: Context, dt: Long): String {
        val now = Date()
        val dateFormat = SimpleDateFormat("hh:mm dd/MM/yyyy")

        val cal = Calendar.getInstance()
        cal.timeInMillis = dt
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val date = cal.get(Calendar.DATE)
        val month = cal.get(Calendar.MONTH) + 1
        val year = cal.get(Calendar.YEAR)
        val mDate = dateFormat.parse("$hour:$minute $date/$month/$year")

        //to get day of week
//        cal.set(Calendar.DATE, date)
//        cal.set(Calendar.MONTH, month-1)
//        cal.set(Calendar.YEAR, year)
        val time = String.format("%02d:%02d", hour, minute)
        var res = ""
        DebugLog.e("toFbFormatTime ${now.year} ${now.month} ${now.date} ${now.hours} ${now.minutes} ${now.seconds} -----${now.time - System.currentTimeMillis()}" +
                "---- ${cal.get(Calendar.DAY_OF_WEEK)}")
        DebugLog.e("toFbFormatTime ${year} ${month} ${date} ${hour} ${minute}  ")
        val yxx = now.year + 1900
        val sMonth = context.getString(monthsMaps.get(month) as Int)
        if (year < yxx - 1) {
            //date1
            DebugLog.e("oooooooo")
            res = context.getString(R.string.date1, date, sMonth, year)
        } else {
            val tt = if (year == yxx - 1) 12 else 0
            val countMonth = tt + now.month + 1 - month
            DebugLog.e("countMonth --- $countMonth")
            if (countMonth > 2) {
                res = context.getString(R.string.date1, date, sMonth, year)
            } else {
                val countDate = TimeUnit.DAYS.convert(now.time - mDate.time, TimeUnit.MILLISECONDS)
                DebugLog.e("countDate--------$countDate")
                if (countDate > 7) {
                    res = context.getString(R.string.date2, date, sMonth, time)
                } else {
                    if (countDate < 2) {
                        val countHours = TimeUnit.HOURS.convert(now.time - mDate.time, TimeUnit.MILLISECONDS)
                        if (countHours < 24) {
                            if (countHours < 1) {
                                val countMinute = TimeUnit.MINUTES.convert(now.time - mDate.time, TimeUnit.MILLISECONDS)
                                if (countMinute < 1) {
                                    res = context.getString(R.string.just_now)
                                } else {
                                    res = context.getString(R.string.mins_ago, countMinute)
                                }
                            } else {
                                res = context.getString(R.string.hours_ago, countHours)
                            }
                        } else {
                            res = context.getString(R.string.yesterday, time)
                        }
                    } else {
                        val weekDay = cal.get(Calendar.DAY_OF_WEEK)
                        DebugLog.e("weekDay-------------$weekDay")
                        res = context.getString(R.string.week_date, context.getString(weekDatesMaps.get(weekDay)!!), time)
                    }
                }
            }
        }
        return res
    }
}