package net.lc.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.danil.recyclerbindableadapter.library.RecyclerBindableAdapter
import net.lc.holders.notis.NotisItemViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.RSearchResult
import net.lc.models.SearchResultRealm
import net.lc.utils.Constants
import net.live.sub.R

/**
 * Created by tusi on 2/4/17.
 */
class NotisRvAdapter(val mContext: Context,
                     var data: MutableList<RSearchResult>?,
                     val mCallbackClick: ICallbackOnClick)
    : RecyclerBindableAdapter<SearchResultRealm, RecyclerView.ViewHolder>() {
    override fun onBindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int, type: Int) {
        viewHolder?.apply {
            when (this) {
                is NotisItemViewHolder -> {
                    bindView(data?.get(position)!!, position)
                }
            }
        }
    }

    override fun viewHolder(view: View?, type: Int): RecyclerView.ViewHolder? = NotisItemViewHolder(mContext, view!!, mCallbackClick)
    override fun layoutId(type: Int) = R.layout.item_notis
    override fun getItemType(position: Int) = Constants.TYPE_ITEM
    override fun getItemCount() = data?.size ?: 0
}