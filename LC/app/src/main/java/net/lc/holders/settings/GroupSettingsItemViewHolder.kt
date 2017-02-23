package net.lc.holders.settings

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import kotlinx.android.synthetic.main.item_group_settings.view.*
import net.lc.adapters.settings.ChildSettingsRvAdapter
import net.lc.holders.BaseViewHolder
import net.lc.models.GroupSettings
import net.lc.models.ICallbackOnClick

/**
 * Created by mrvu on 1/22/17.
 */
class GroupSettingsItemViewHolder(val mContext: Context, itemView: View, val mICallbackOnClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mICallbackOnClick) {
    private var childSettingsRvAdapter: ChildSettingsRvAdapter? = null
    override fun bindView(obj: Any, position: Int) {
        mPosition = position
        if (obj is GroupSettings) {
            obj.apply {
                itemView.tv_group_name.setText(obj.label)
                childSettingsRvAdapter = ChildSettingsRvAdapter(mContext, obj.childSettings, mICallbackOnClick)
                itemView.rv_child.layoutManager = LinearLayoutManager(mContext)
                itemView.rv_child.adapter = childSettingsRvAdapter
            }
        }
    }
}