package net.lc.holders.search

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_search_content.view.*
import kotlinx.android.synthetic.main.item_search_suggestion.view.*
import net.lc.R
import net.lc.holders.BaseViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.SearchResult


/**
 * Created by mrvu on 12/29/16.
 */
class SuggestionItemViewHolder(val mContext: Context, itemView: View, mCallbackClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mCallbackClick) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is SearchResult) {
            mPosition = position
            itemView.tvTitle.text = obj.snippet?.channelTitle
            itemView.tvSubscribers.visibility = View.VISIBLE
            itemView.tvSubscribers.text = mContext.getString(R.string.subscribers, obj.statistics?.subscriberCount)
            itemView.img_avatar.setImageURI(obj.snippet?.thumbnails?.medium?.url)
            itemView.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_CHANNEL) }
        }
    }
}