package net.lc.models

import android.content.Context
import com.tieudieu.util.DebugLog
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import net.lc.utils.numberFormat
import net.live.sub.R

/**
 * Created by mrvu on 1/5/17.
 */
open class SearchResultRealm(
        @PrimaryKey
        open var channelId: String? = null,
        open var channelTitle: String? = null,
        open var subscriberCount: String? = null,
        open var description: String? = null,
        open var thumbnailUrl: String? = null,
        open var time: Long? = 0,
        open var following: Int? = 0,
        open var autoLC: Int? = 0,
        open var notiApproachMilestone: Int? = 0,
        open var notiReachMilestone: Int? = 0,
        open var milestone: Long? = 0,
        open var subscriberAway: Long? = 0,
        open var noti: Int? = 0,
        open var notiDes: String? = null,
        open var notiTime: Long? = 0,
        open var reached: Int? = 0,
        open var approached: Int? = 0
) : RealmObject(), Cloneable {
    override public fun clone(): RSearchResult {
        return RSearchResult(channelId, channelTitle, subscriberCount, description, thumbnailUrl,
                time, following, autoLC, notiApproachMilestone, notiReachMilestone, milestone, subscriberAway,
                noti, notiDes, notiTime, reached, approached)
    }
}

class RSearchResult(
        var channelId: String? = null,
        var channelTitle: String? = null,
        var subscriberCount: String? = null,
        var description: String? = null,
        var thumbnailUrl: String? = null,
        var time: Long? = 0,
        var following: Int? = 0,
        var autoLC: Int? = 0,
        var notiApproachMilestone: Int? = 0,
        var notiReachMilestone: Int? = 0,
        var milestone: Long? = 0,
        var subscriberAway: Long? = 0,
        var noti: Int? = 0,
        var notiDes: String? = null,
        var notiTime: Long? = 0,
        var reached: Int? = 0,
        var approached: Int? = 0
) {
    fun notiEnabled() = notiApproachMilestone == 1 || notiReachMilestone == 1

    fun onNotisCheck(context: Context): Boolean {
        if (milestone == 0L && subscriberAway == 0L) return false
        var ok = false
        val currentTime = System.currentTimeMillis()
        val sCount = subscriberCount!!.toLong()
        if (notiApproachMilestone == 1) {
            if (approached == 0 && sCount + subscriberAway!! >= milestone!!) {
                DebugLog.e("approach---------")
                noti = 1
                ok = true
                approached = 1
                notiDes = context.getString(R.string.noti_approach_milestone, channelTitle,
                        numberFormat.format(subscriberAway!!), numberFormat.format(milestone!!))
                notiTime = currentTime
                notiApproachMilestone = 0
            }
        }
        if (notiReachMilestone == 1) {
            if (reached == 0 && sCount >= milestone!!) {
                DebugLog.e("reach-------------")
                noti = 1
                ok = true
                reached = 1
                notiDes = context.getString(R.string.noti_reached_milestone,
                        channelTitle, numberFormat.format(milestone!!))
                notiTime = currentTime
                notiApproachMilestone = 0
                notiReachMilestone = 0
            }
        }
        return ok
    }

    fun getMilestone(): Long {
        var res = milestone
        val s = subscriberCount!!.toLong()
        if (res!! <= s) res = s + 100
        return res
    }
}
open class SearchQueryRealm(@PrimaryKey open var query: String? = null,
                            open var time: Long? = 0) : RealmObject()

