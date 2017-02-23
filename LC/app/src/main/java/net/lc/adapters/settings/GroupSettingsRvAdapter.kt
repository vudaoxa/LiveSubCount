package net.lc.adapters.settings

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.lc.holders.BaseViewHolder
import net.lc.holders.settings.GroupSettingsItemViewHolder
import net.lc.models.GroupSettings
import net.lc.models.ICallbackOnClick
import net.lc.utils.Constants
import net.live.sub.R

/**
 * Created by mrvu on 1/22/17.
 */
class GroupSettingsRvAdapter(val mContext: Context, val data: MutableList<GroupSettings>, val mICallbackOnClick: ICallbackOnClick)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: BaseViewHolder? = null
        var view: View
        when (viewType) {
            Constants.TYPE_ITEM -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_group_settings, parent, false)
                viewHolder = GroupSettingsItemViewHolder(mContext, view, mICallbackOnClick)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is GroupSettingsItemViewHolder) {
            holder.bindView(data[position], position)
        }
    }

    override fun getItemViewType(position: Int) = Constants.TYPE_ITEM
    override fun getItemCount() = data.count()
}