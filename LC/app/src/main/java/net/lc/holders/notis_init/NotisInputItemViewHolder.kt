package net.lc.holders.notis_init

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_notis_input.view.*
import net.lc.holders.BaseViewHolder
import net.lc.holders.TYPE_MILESTONE
import net.lc.holders.TYPE_SUBSCRIBER_AWAY
import net.lc.models.ICallbackOnClick
import net.lc.models.ToggleNotis
import net.lc.utils.anim

/**
 * Created by tusi on 2/2/17.
 */
class NotisInputItemViewHolder(mContext: Context, itemView: View, mICallbackOnClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mICallbackOnClick) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is ToggleNotis) {
            obj.apply {
                rSearchResult.apply {
                    itemView.apply {
                        editText = tv_input
                        layout_input.apply {
                            hint = mContext.getString(labelResId)
                            if (!checked)
                                visibility = View.GONE
                            else {
                                visibility = View.VISIBLE
                                startAnimation(anim)
                            }
                        }
                        when (type) {
                            TYPE_MILESTONE -> {
                                tv_input.apply {
                                    if (text.trim().isEmpty())
                                        setText("${getMilestone()}")
                                }
                            }
                            TYPE_SUBSCRIBER_AWAY -> {
                                tv_input.apply {
                                    if (text.trim().isEmpty()) {
                                        if (subscriberAway != 0L) {
                                            setText("${subscriberAway}")
                                        } else setText("100")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}