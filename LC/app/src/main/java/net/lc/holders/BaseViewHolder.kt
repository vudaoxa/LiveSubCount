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
    abstract fun bindView(obj: Any, position: Int)
    var mPosition: Int = 0
    companion object {
        val ACTION_CLICK_CHANNEL = 0
        val ACTION_CLICK_SEARCH_HISTORY = 1
        val ACTION_CLICK_FOLLOWING_CHANNEL = 2
        val ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL = -2
        val ACTION_CLICK_FOLLOWING_CHANNEL_BELL = 3
        val ACTION_CLICK_FOLLOWING_CHANNEL_PLAY = 4
    }
}