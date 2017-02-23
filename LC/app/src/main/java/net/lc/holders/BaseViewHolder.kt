package net.lc.holders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import net.lc.models.ICallbackOnClick


/**
 * Created by mrvu on 12/29/16.
 */
abstract class BaseViewHolder(protected var context: Context,
                              itemView: View, protected var mCallbackClick: ICallbackOnClick) : RecyclerView.ViewHolder(itemView) {
    //    constructor(protected var context: Context,
//                itemView: View)
    abstract fun bindView(obj: Any, position: Int)
    var mPosition: Int = 0
    companion object {
        val ACTION_CLICK_CHANNEL = 0
        val ACTION_QUICK_SEARCH = -1
        val ACTION_CLICK_SEARCH_HISTORY = 1
        val ACTION_CLICK_FOLLOWING_CHANNEL = 2
        val ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL = -2
        val ACTION_CLICK_FOLLOWING_CHANNEL_BELL = 3
        val ACTION_CLICK_FOLLOWING_CHANNEL_PLAY = 4
    }
}

const val TYPE_PLAY = "PLAY"
const val TYPE_NOTI = "NOTI"
const val TYPE_NOTI_APPROACH = "APPROACH"
const val TYPE_NOTI_REACH = "REACH"
const val TYPE_MILESTONE = "MILESTONE"
const val TYPE_SUBSCRIBER_AWAY = "SUB_AWAY"