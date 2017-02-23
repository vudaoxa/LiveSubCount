package net.lc.utils

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import net.live.sub.R

/**
 * Created by tusi on 2/17/17.
 */
private var sAnalytics: GoogleAnalytics? = null
private var sTracker: Tracker? = null
fun initTracking(context: Context) {
    sAnalytics = GoogleAnalytics.getInstance(context)
    getDefaultTracker()
}

@Synchronized fun getDefaultTracker(): Tracker {
    // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
    if (sTracker == null) {
        sTracker = sAnalytics!!.newTracker(R.xml.global_tracker)
    }
    return sTracker!!
}

fun sendHit(category: String, action: String) {
    sTracker?.apply {
        send(HitBuilders.EventBuilder().setCategory(category).setAction(action).build())
    }
}

val CATEGORY_ACTION = "ACTION"
val ACTION_SHARE = "SHARE"
val ACTION_GO_SEARCH = "GO_SEARCH"
val ACTION_SEARCH_SUBMIT = "SEARCH_SUBMIT"
val ACTION_SEARCH_SUGGESTION = "SEARCH_SUGGESTION"
val ACTION_QUICK_SEARCH = "QUICK_SEARCH"
val ACTION_FOLLOWING = "FOLLOWING"
val ACTION_GO_YOUTUBE = "GO_YOUTUBE"
val ACTION_MOVE_TAB = "MOVE_TAB"
val ACTION_CLICK_NOTIS_ITEM = "CLICK_NOTIS_ITEM"
val ACTION_REMOVE_NOTIS_ITEM = "REMOVE_NOTIS_ITEM"
val ACTION_CLICK_FOLLOWING_ITEM = "CLICK_FOLLOWING_ITEM"
val ACTION_BELL = "BELL"
val ACTION_PLAY = "PLAY"
val ACTION_REMOVE_FOLLOWING_ITEM = "REMOVE_FOLLOWING_ITEM"
val ACTION_TOGGLE = "TOGGLE_"
val ACTION_EMAIL = "SEND_EMAIL"
val ACTION_INIT_NOTIS_SUBMIT = "INIT_NOTIS_SUBMIT"
val ACTION_REPORT = "REPORT"
val ACTION_CLICK_SEARCH_RESULT_ITEM = "CLICK_SEARCH_RESULT_ITEM"
val ACTION_CLICK_SEARCH_HISTORY_ITEM = "CLICK_SEARCH_HISTORY_ITEM"
val ACTION_onAdClosed = "onAdClosed"
val ACTION_onAdFailedToLoad = "onAdFailedToLoad"
val ACTION_onAdOpened = "onAdOpened"
val ACTION_onAdLoaded = "onAdLoaded"
val ACTION_onAdLeftApplication = "onAdLeftApplication"
//val ACTION_=""
//val ACTION_=""
