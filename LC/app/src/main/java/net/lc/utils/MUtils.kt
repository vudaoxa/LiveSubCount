package net.lc.utils

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.joanzapata.iconify.IconDrawable
import com.joanzapata.iconify.fonts.IoniconsIcons
import net.live.sub.R
import java.text.NumberFormat
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

fun showMessage(context: Context, resId: Int, time: Int) {
    Toast.makeText(context, resId, time).show()
}

fun showMessage(context: Context, resId: String, time: Int) {
    Toast.makeText(context, resId, time).show()
}

val topYtbChannels = listOf<String>("PewDiePie", "HolaSoyGerman", "JustinBieberVEVO",
        "RihannaVEVO", "elrubiusOMG", "Smosh", "OneDirectionVEVO", "TaylorSwiftVEVO", "EminemVEVO", "KatyPerryVEVO")
var anim: Animation? = null
var animIn: Animation? = null
var animOut: Animation? = null
fun initAnimations(context: Context) {
    if (anim != null) {
        return
    }
    anim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    anim?.apply {
        duration = Constants.ANIM_DURATION_SHORT
    }

    animIn = AnimationUtils.loadAnimation(context,
            android.R.anim.fade_in)
    animOut = AnimationUtils.loadAnimation(context,
            android.R.anim.fade_out)
    animIn?.apply {
        duration = Constants.ANIM_DURATION
    }
    animOut?.apply {
        duration = Constants.ANIM_DURATION
    }
}

var icon_search: IconDrawable? = null
var icon_share: IconDrawable? = null
var icon_star: IconDrawable? = null
var icon_send: IconDrawable? = null
var icon_checked: IconDrawable? = null
var icon_del: IconDrawable? = null
var icon_play: IconDrawable? = null
var icon_noti: IconDrawable? = null
var icon_play_enabled: IconDrawable? = null
var icon_noti_enabled: IconDrawable? = null
fun initIcons(context: Context) {
    icon_search = IconDrawable(context,
            IoniconsIcons.ion_ios_search_strong).colorRes(R.color.white).actionBarSize()
    icon_share = IconDrawable(context,
            IoniconsIcons.ion_share).colorRes(R.color.white).actionBarSize()
    icon_star = IconDrawable(context,
            IoniconsIcons.ion_ios_star).colorRes(R.color.white).actionBarSize()
    icon_send = IconDrawable(context,
            IoniconsIcons.ion_android_send).colorRes(R.color.white).actionBarSize()
    icon_checked = IconDrawable(context,
            IoniconsIcons.ion_checkmark_round).colorRes(R.color.green_light).actionBarSize()
    icon_del = IconDrawable(context,
            IoniconsIcons.ion_ios_trash).colorRes(R.color.red).actionBarSize()

    icon_play = IconDrawable(context,
            IoniconsIcons.ion_ios_play).colorRes(R.color.grey_50).actionBarSize()
    icon_noti = IconDrawable(context,
            IoniconsIcons.ion_ios_bell).colorRes(R.color.grey_50).actionBarSize()
    icon_play_enabled = IconDrawable(context,
            IoniconsIcons.ion_ios_play).colorRes(R.color.blue).actionBarSize()
    icon_noti_enabled = IconDrawable(context,
            IoniconsIcons.ion_ios_bell).colorRes(R.color.blue).actionBarSize()
}

val numberFormat = NumberFormat.getNumberInstance(Locale.US)

fun getTitleHeader(context: Context, headerResId: Int, contentResId: Int): SpannableString {
    val header = context.getString(headerResId)
    val content = context.getString(contentResId)
    val r = header + " " + content
    val res = SpannableString(r)
    res.apply {
        setSpan(StyleSpan(Typeface.BOLD), 0,
                header.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return res
}

fun rand(x: Int): Long = Math.round(Math.random() * x)