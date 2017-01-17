package net.lc.adapters.search


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.RealmResults
import net.lc.R
import net.lc.holders.BaseViewHolder
import net.lc.holders.LoadingViewHolder
import net.lc.holders.search.HistoryItemViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.SearchQueryRealm
import net.lc.utils.Constants

/**
 * Created by mrvu on 1/6/17.
 */
class SearchHistoryRvAdapter(val mContext: Context,
                             var data: RealmResults<SearchQueryRealm>?,
                             val mCallbackClick: ICallbackOnClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: BaseViewHolder
        val view: View?
        when (viewType) {
            Constants.TYPE_LOADING -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.layout_progress_view, parent, false)
                return LoadingViewHolder(view)
            }
            else//result item
            -> {
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_search_history, parent, false)
                viewHolder = HistoryItemViewHolder(mContext, view, mCallbackClick)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is HistoryItemViewHolder) {
            holder.bindView(data!![position], position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = data!![position]
        return if (item.query == null) Constants.TYPE_LOADING
        else Constants.TYPE_ITEM
    }

    override fun getItemCount() = data?.size ?: 0
}