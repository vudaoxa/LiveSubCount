package net.lc.adapters.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.lc.R
import net.lc.holders.BaseViewHolder
import net.lc.holders.LoadingViewHolder
import net.lc.holders.search.SuggestionItemViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.SearchResult
import net.lc.utils.Constants


/**
 * Created by mrvu on 12/28/16.
 */
class SearchSuggestionRvAdapter(val mContext: Context,
                                val data: MutableList<SearchResult>?,
                                val mCallbackClick: ICallbackOnClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isLoading: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: BaseViewHolder?
        val view: View?
        when (viewType) {
            Constants.TYPE_LOADING -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_progress_view, parent, false)
                return LoadingViewHolder(view)
            }

            else//result item
            -> {
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_search_suggestion, parent, false)
                viewHolder = SuggestionItemViewHolder(mContext, view, mCallbackClick)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is SuggestionItemViewHolder) {
            holder.bindView(data!![position], position)
        } else if (holder is LoadingViewHolder) {
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = data!![position]
        if (item.kind == null) {
            return Constants.TYPE_LOADING
        }
        return Constants.TYPE_ITEM
    }

    override fun getItemCount() = if (data == null) 0 else data.size
}