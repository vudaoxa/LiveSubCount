package net.lc.holders

import android.content.Context
import android.view.View
import com.google.api.services.youtube.model.SearchResult
import kotlinx.android.synthetic.main.item_search_suggestion.view.*
import net.lc.interfaces.ICallbackOnClick

/**
 * Created by mrvu on 12/29/16.
 */
class SearchSuggestionItemViewHolder(val mContext: Context, itemView: View, mCallbackClick: ICallbackOnClick)
    : BaseSearchViewHolder(mContext, itemView, mCallbackClick) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is SearchResult) {
            mPosition = position
            itemView.tvTitle.text = obj.snippet.channelTitle
            itemView.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_CHANNEL) }
        }
    }

}