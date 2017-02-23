package net.lc.models

import android.support.v7.widget.RecyclerView
import io.realm.RealmObject
import io.realm.RealmResults
import net.lc.utils.CallbackBottomMessage

/**
 * Created by mrvu on 1/5/17.
 */
interface ILoadData {
    fun getMinSize(): Int
    fun getMaxSize(): Int
    fun <T> isEmptyData(list: MutableList<T>?): Boolean
    fun isEmptyData()
    fun hideLoadMoreIndicator()
}

interface ICallbackFollowing {
    fun onFollowing()
}

interface ICallbackSharing {
    fun onSharing()
}

interface ICallbackCheckFollowing {
    fun isFollowing(): Boolean
}
interface ICallbackChannelListResponse {
    fun update(channelListResponse: ChannelListResponse, part: String)
}

interface IFields {
    fun initFields()
}

interface IConnect {
    fun onLoadDataFailure(callbackBottomMessage: CallbackBottomMessage)
    fun onLoadDataFailure()
    fun onNoInternetConnection(callbackBottomMessage: CallbackBottomMessage)
    fun onNoInternetConnection()
}

interface IRandomChannel {
    fun onLoadRandomChannelFailure()
}

interface ICallbackSearchResult {
    fun onSearchResultReceived(searchResult: SearchResult)
}

interface ICallbackSearchResultRealm {
    fun onSearchResultRealmReceived(searchResultRealm: RSearchResult)
}

interface ICallbackTabMove {
    fun onTabMove(i: Int)
}

interface ICallbackEditFav {
    fun onEditClicked(edit: Int)
}

interface ICallbackClear {
    fun onClear()
}
interface ICallbackHistory {
    fun onSearchHistoryClicked(searchQueryRealm: SearchQueryRealm)
}

interface ICallbackRealmResultChange {
    fun <T : RealmObject> onChange(realmResult: RealmResults<T>)
}

interface ICallbackShowRv {
    fun onShow(rv: RecyclerView, isShow: Boolean)
}

interface ICallbackSearch {
    fun onSearch(query: String, pageToken: String?)
}

interface ICallbackOnClick {
    fun onClick(position: Int, event: Int)
}

abstract class ICallbackLoadMore {
    var countLoadmore: Int = 0
    abstract fun onLoadMore()
}

interface IBackListener {
    fun onBackPressed()
}

interface ICallbackSubmit {
    fun onSubmit()
}

interface ICallBackBadge {
    fun updateBadge()
}

interface ICallbackNotis {
    fun onNotisCheck(rSearchResult: RSearchResult)
}

interface ICallbackToggleSettings {
    fun onToggleSettings()
}

interface ICallbackRefresh {
    fun setRefreshing(refreshing: Boolean)
    fun pullToRefreshEnabled(): Boolean
    val pullToRefreshColorResources: IntArray
    fun onRefresh()
}