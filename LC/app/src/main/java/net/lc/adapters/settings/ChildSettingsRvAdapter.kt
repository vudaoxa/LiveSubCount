package net.lc.adapters.settings

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.lc.R
import net.lc.holders.BaseViewHolder
import net.lc.holders.settings.ChildSettingsItemViewHolder
import net.lc.models.ChildSetting
import net.lc.models.ICallbackOnClick
import net.lc.utils.Constants

/**
 * Created by mrvu on 1/22/17.
 */
class ChildSettingsRvAdapter(val mContext: Context, var mData: MutableList<ChildSetting>, val mICallbackOnClick: ICallbackOnClick)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount() = mData.size
    override fun getItemViewType(position: Int) = Constants.TYPE_ITEM
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: BaseViewHolder? = null
        val view: View
        when (viewType) {
            Constants.TYPE_ITEM -> {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_child_settings, parent, false)
                viewHolder = ChildSettingsItemViewHolder(mContext, view, mICallbackOnClick)
            }
        }
        return viewHolder!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is BaseViewHolder) {
            holder.bindView(mData[position], position)
        }
    }
}