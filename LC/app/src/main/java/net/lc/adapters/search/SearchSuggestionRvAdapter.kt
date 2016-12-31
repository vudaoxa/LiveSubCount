package net.lc.adapters.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.api.services.youtube.model.SearchResult
import net.lc.R
import net.lc.fragments.main.LoadingViewHolder
import net.lc.holders.BaseSearchViewHolder
import net.lc.holders.SearchSuggestionItemViewHolder
import net.lc.interfaces.ICallbackOnClick
import net.lc.utils.Constants

/**
 * Created by mrvu on 12/28/16.
 */
class SearchSuggestionRvAdapter(val mContext: Context,
                                val data: MutableList<SearchResult>?,
                                val mCallbackClick: ICallbackOnClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var isLoading: Boolean = false
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: BaseSearchViewHolder?
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
                viewHolder = SearchSuggestionItemViewHolder(mContext, view, mCallbackClick)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is SearchSuggestionItemViewHolder) {
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

    override fun getItemCount(): Int {
        return if (data == null) 0 else data.size
    }

    fun addItem(item: SearchResult) {
        data!!.add(item)
    }

    fun addAllItems(items: List<SearchResult>) {
        data!!.addAll(items)
    }
}