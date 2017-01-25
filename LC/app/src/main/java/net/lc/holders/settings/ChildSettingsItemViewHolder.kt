package net.lc.holders.settings

import android.content.Context
import android.view.View
import com.tieudieu.util.DebugLog
import kotlinx.android.synthetic.main.item_child_settings.view.*
import net.lc.holders.BaseViewHolder
import net.lc.models.ChildSetting
import net.lc.models.ICallbackOnClick

/**
 * Created by mrvu on 1/22/17.
 */
class ChildSettingsItemViewHolder(mContext: Context, itemView: View, mICallbackOnClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mICallbackOnClick) {
    override fun bindView(obj: Any, position: Int) {
        mPosition = position
        if (obj is ChildSetting) {
            itemView.tv_name.setText(obj.name)
            itemView.sw_checked.isChecked = obj.checked
            itemView.sw_checked.setOnClickListener { changeSetting(obj.name) }
        }
    }

    fun changeSetting(event: Int) {
        DebugLog.e("changeSetting------------")
        mCallbackClick.onClick(mPosition, event)
    }
}