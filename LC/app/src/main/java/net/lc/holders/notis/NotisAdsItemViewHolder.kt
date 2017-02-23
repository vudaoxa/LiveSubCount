package net.lc.holders.notis

import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.layout_ads_smart_banner.view.*
import net.lc.holders.BaseAdsViewHolder
import net.lc.utils.loadBannerAds

/**
 * Created by tusi on 2/20/17.
 */
class NotisAdsItemViewHolder(val mContext: Context, itemView: View) : BaseAdsViewHolder(mContext, itemView) {
    override fun bindView(obj: Any, position: Int) {
        loadBannerAds(itemView.ad_view)
    }
}