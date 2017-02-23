package net.lc.holders

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.item_following_channel.view.*
import kotlinx.android.synthetic.main.item_following_channel_btns.view.*
import kotlinx.android.synthetic.main.item_search_content.view.*
import net.lc.fragments.main.FollowingChannelsFragment
import net.lc.models.ICallbackOnClick
import net.lc.models.RSearchResult
import net.lc.presenters.SettingsPresenter
import net.lc.utils.*
import net.lc.views.StatesIconBtn
import net.live.sub.R

/**
 * Created by mrvu on 1/9/17.
 */
class FollowingChannelsItemViewHolder(val mContext: Context, itemView: View, mCallbackClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mCallbackClick) {
    var statesBtnPlay: StatesIconBtn? = null
    var statesBtnNoti: StatesIconBtn? = null
    override fun bindView(obj: Any, position: Int) {
        mPosition = position
        if (obj is RSearchResult) {
            val settingsItem = SettingsPresenter.getSettingsMapItem(6)
            statesBtnNoti = StatesIconBtn(mContext, itemView.btn_bell, listOf(icon_noti!!, icon_noti_enabled!!), if (obj.notiEnabled()) 1 else 0)
            statesBtnPlay = StatesIconBtn(mContext, itemView.btn_play, listOf(icon_play!!, icon_play_enabled!!), obj.autoLC!!)
            if (mCallbackClick is FollowingChannelsFragment) {
                if ((mCallbackClick as FollowingChannelsFragment).editClicked) {
                    itemView.btn_del.visibility = View.VISIBLE
                    itemView.layout_following_btns.visibility = View.GONE
                    itemView.delete.visibility = View.GONE
                } else {
                    itemView.btn_del.visibility = View.GONE
                    itemView.layout_following_btns.visibility = View.VISIBLE
                    itemView.delete.visibility = View.VISIBLE
                }
            }
            itemView.btn_del.setImageDrawable(icon_del)
            if (settingsItem.checked) {
                itemView.img_avatar.visibility = View.VISIBLE
                itemView.img_avatar.setImageURI(obj.thumbnailUrl)
            } else itemView.img_avatar.visibility = View.GONE

            itemView.tvTitle.text = obj.channelTitle
            obj.subscriberCount?.apply {
                itemView.tvSubscribers.text = mContext.getString(R.string.subscribers, numberFormat.format(this.toLong()))
            }

            itemView.layout_content.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL) }
            itemView.delete.setOnClickListener { mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL) }
            itemView.btn_bell.setOnClickListener {
                if (obj.subscriberCount == null) return@setOnClickListener
                mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL_BELL)
            }
            itemView.btn_play.setOnClickListener {
                statesBtnPlay?.apply {
                    onClicked()
                }
                mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL_PLAY)
            }
            itemView.btn_del.setOnClickListener {
                mCallbackClick.onClick(mPosition, ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL)
            }
        }
    }
}