package net.lc.holders.notis_init

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_child_settings.view.*
import net.lc.holders.BaseViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.ToggleNotis

/**
 * Created by tusi on 2/2/17.
 */
class NotisToggleItemViewHolder(mContext: Context, itemView: View, val mICallbackOnClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mICallbackOnClick) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is ToggleNotis) {
            obj.apply {
                val label = context.getString(labelResId, rSearchResult.channelTitle)
                itemView.tv_name.text = label
//                when (type) {
//                    TYPE_NOTI_APPROACH -> {
//                        checked = rSearchResult.notiApproachMilestone == 1
//                    }
//                    TYPE_NOTI_REACH -> {
//                        checked = rSearchResult.notiReachMilestone == 1
//                    }
//                }
                itemView.sw_checked.apply {
                    isChecked = checked
                    setOnCheckedChangeListener { compoundButton, b ->
                        checked = isChecked
                        mICallbackOnClick.onClick(position, ACTION_CLICK_CHANNEL)
                    }
                }
            }
        }
    }
}