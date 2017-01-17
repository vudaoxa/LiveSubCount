package net.lc.holders

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_following_channel.view.*
import kotlinx.android.synthetic.main.item_following_channel_btns.view.*
import kotlinx.android.synthetic.main.item_search_content.view.*
import net.lc.models.ICallbackOnClick
import net.lc.models.SearchResultRealm

/**
 * Created by mrvu on 1/9/17.
 */
class FollowingChannelsItemViewHolder(mContext: Context, itemView: View, mCallbackClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mCallbackClick) {
    override fun bindView(obj: Any, position: Int) {
        mPosition = position
        if (obj is SearchResultRealm) {
            itemView.tvTitle.text = obj.channelTitle
            //real time implement later
//            itemView.tvSubscribers.text= obj.
            itemView.layout_content.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL) }
            itemView.delete.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL) }
            itemView.btn_bell.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL_BELL) }
            itemView.btn_play.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL_PLAY) }

        }
    }
}