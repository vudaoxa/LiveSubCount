package net.lc.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.lc.holders.BaseViewHolder
import net.lc.holders.notis_init.NotisInputItemViewHolder
import net.lc.holders.notis_init.NotisToggleItemViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.RSearchResult
import net.lc.models.ToggleNotis
import net.live.sub.R

/**
 * Created by tusi on 2/2/17.
 */
class InitialNotificationsRvAdapter(val mContext: Context, val rSearchResult: RSearchResult, val data: List<ToggleNotis>, val mICallbackOnClick: ICallbackOnClick)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: BaseViewHolder? = null
        val view: View
        when (viewType) {
            0, 1 -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_notis_toggle, parent, false)
                viewHolder = NotisToggleItemViewHolder(mContext, view, mICallbackOnClick)
            }
            2, 3 -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_notis_input, parent, false)
                viewHolder = NotisInputItemViewHolder(mContext, view, mICallbackOnClick)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is NotisToggleItemViewHolder) {
            holder.bindView(data[position], position)
        } else if (holder is NotisInputItemViewHolder) {
            holder.bindView(data[position], position)
        }
    }

    override fun getItemCount() = 4
    override fun getItemViewType(position: Int) = position
}