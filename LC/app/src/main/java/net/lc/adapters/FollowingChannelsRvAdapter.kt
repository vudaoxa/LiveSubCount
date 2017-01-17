package net.lc.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import com.danil.recyclerbindableadapter.library.RecyclerBindableAdapter
import io.realm.RealmResults
import net.lc.R
import net.lc.holders.FollowingChannelsItemViewHolder
import net.lc.models.ICallbackOnClick
import net.lc.models.SearchResultRealm
import net.lc.utils.Constants

/**
 * Created by mrvu on 1/9/17.
 */
class FollowingChannelsRvAdapter(val mContext: Context,
                                 var data: RealmResults<SearchResultRealm>?,
                                 val mCallbackClick: ICallbackOnClick)
    : RecyclerBindableAdapter<SearchResultRealm, RecyclerView.ViewHolder>() {
    override fun onBindItemViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int, type: Int) {
        if (viewHolder is FollowingChannelsItemViewHolder) {
            viewHolder.bindView(data?.get(position)!!, position)
        }
    }

    override fun viewHolder(view: View?, type: Int) = FollowingChannelsItemViewHolder(mContext, view!!, mCallbackClick)
    override fun layoutId(type: Int) = R.layout.item_following_channel
    override fun getItemType(position: Int) = Constants.TYPE_ITEM
    override fun getItemCount() = data?.size ?: 0

}