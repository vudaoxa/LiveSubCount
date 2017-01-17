package net.lc.holders.search

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_search_history.view.*
import net.lc.holders.BaseViewHolder
import net.lc.models.ICallbackOnClick

import net.lc.models.SearchQueryRealm

/**
 * Created by mrvu on 1/6/17.
 */
class HistoryItemViewHolder(mContext: Context, itemView: View, mCallbackClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mCallbackClick) {
    override fun bindView(obj: Any, position: Int) {
        mPosition = position
        if (obj is SearchQueryRealm) {
            itemView.tv_name.text = obj.query
            itemView.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_SEARCH_HISTORY) }
        }

    }
}