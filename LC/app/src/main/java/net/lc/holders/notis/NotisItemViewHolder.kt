package net.lc.holders.notis

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.View
import kotlinx.android.synthetic.main.item_notis.view.*
import kotlinx.android.synthetic.main.item_notis_content.view.*
import net.lc.holders.BaseViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.RSearchResult
import net.lc.presenters.SettingsPresenter
import vn.mycare.member.utils.TimeUtils

/**
 * Created by tusi on 2/4/17.
 */
class NotisItemViewHolder(val mContext: Context, itemView: View, mCallbackClick: ICallbackOnClick)
    : BaseViewHolder(mContext, itemView, mCallbackClick) {
    override fun bindView(obj: Any, position: Int) {
        if (obj is RSearchResult) {
            obj.apply {
                if (noti == 1) {
                    val settingsItem = SettingsPresenter.getSettingsMapItem(7)
                    if (settingsItem.checked) {
                        itemView.img_avatar.visibility = View.VISIBLE
                        itemView.img_avatar.setImageURI(thumbnailUrl)
                    } else itemView.img_avatar.visibility = View.GONE
                    itemView.tvTitle.text = channelTitle
                    itemView.tv_des.text = getTitleNotis(this)
                    itemView.tv_time.text = TimeUtils.toFbFormatTime(mContext, notiTime!!)
                    itemView.layout_content.setOnClickListener { mCallbackClick.onClick(position, ACTION_CLICK_FOLLOWING_CHANNEL) }
                    itemView.delete.setOnClickListener { mCallbackClick.onClick(position, ACTION_CLICK_FOLLOWING_CHANNEL_REMOVAL) }
//                    itemView.setOnTouchListener { view, motionEvent ->
//                        when(motionEvent.action){
//
//                        }
//                    }
                }
            }
        }
    }

    fun getTitleNotis(rSearchResult: RSearchResult): SpannableString {
        var res: SpannableString? = null
        rSearchResult.apply {
            if (noti == 1) {
                res = SpannableString(notiDes)
                res?.apply {
                    setSpan(StyleSpan(Typeface.BOLD), 0,
                            channelTitle!!.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        return res!!
    }
}