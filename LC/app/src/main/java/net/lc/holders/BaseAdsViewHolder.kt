package net.lc.holders

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by tusi on 2/20/17.
 */
abstract class BaseAdsViewHolder(context: Context,
                                 itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bindView(obj: Any, position: Int)
}