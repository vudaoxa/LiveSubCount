package net.lc.presenters

import com.tieudieu.util.DebugLog
import io.realm.Realm
import io.realm.RealmObject
import io.realm.Sort
import net.lc.fragments.main.FollowingChannelsFragment
import net.lc.fragments.main.LiveCountFragment
import net.lc.fragments.search.FirstSuggestionFragment
import net.lc.models.SearchQueryRealm
import net.lc.models.SearchResultRealm
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by mrvu on 1/15/17.
 */
class MRealmPresenter {
    var isRequest: Boolean = false
    fun loadSearchHistory(mFirstSuggestionFragment: FirstSuggestionFragment) {
        val realm = Realm.getDefaultInstance()
        val d = realm.where(SearchQueryRealm::class.java)
                .findAllSortedAsync("time", Sort.DESCENDING).asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isLoaded) {
                        mFirstSuggestionFragment.onChange(it)
                        it.addChangeListener {
                            mFirstSuggestionFragment.onChange(it)
                        }
                    }
                }, {
                    it.printStackTrace()
                    realm.close()
                }, {
                    realm.close()
                })
        mFirstSuggestionFragment.mSubscription.add(d)
    }

    fun loadFollowingChannels(mFollowingChannelsFragment: FollowingChannelsFragment) {
        val realm = Realm.getDefaultInstance()
        val d = realm.where(SearchResultRealm::class.java).equalTo("following", 1)
                .findAllSortedAsync("time", Sort.DESCENDING).asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isLoaded) {
                        mFollowingChannelsFragment.onChange(it)
                        it.addChangeListener {
                            mFollowingChannelsFragment.onChange(it)
                        }
                        if (it.isEmpty()) {
//                            mFollowingChannelsFragment.isEmptyData()
                        }
                    }
                }, {
                    it.printStackTrace()
                    realm.close()
                }, {
                    realm.close()
                })
        mFollowingChannelsFragment.mSubscription.add(d)
    }

    fun obtainSearchResultRealm(mLiveCountFragment: LiveCountFragment) {
        if (isRequest) return
        isRequest = true
        val realm = Realm.getDefaultInstance()
        val d = realm.where(SearchResultRealm::class.java)
                .findAllSortedAsync("time", Sort.DESCENDING).asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isLoaded) {
                        if (it.isNotEmpty()) {
                            val searchResultRealm = it[0]
                            mLiveCountFragment.updateViewChannelInfo(searchResultRealm)
                            DebugLog.e("obtainSearchResultRealm======================")
                            mLiveCountFragment.loadLC(searchResultRealm, singleRequest = true)
                        } else {
                            mLiveCountFragment.onObtainSearchResultRealmFailure()
                        }
                    } else {
                        mLiveCountFragment.onObtainSearchResultRealmFailure()
                    }
                    isRequest = false
                }, {
                    it.printStackTrace()
                    isRequest = false
                    realm.close()
                }, {
                    isRequest = false
                    realm.close()
                })
        mLiveCountFragment.mSubscription.add(d)
    }

    fun saveSearchQuery(query: String) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            val searchQueryRealm = it.createObject(SearchQueryRealm::class.java, query)
//            val searchQueryRealm = it.createObject()
//            searchQueryRealm.query = query
            searchQueryRealm.time = System.currentTimeMillis()
        }, {
            DebugLog.e("saveSearchQuery ---------------- success")
            realm.close()
        }, {
            it.printStackTrace()
            realm.close()
        })
    }

    fun updateSearchResultRealm(channelId: String?, channelTitle: String?, following: Int?, time: Long?, thumbnailUrl: String?) {
        DebugLog.e("updateSearchResultRealm-----------")
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            val item = it.where(SearchResultRealm::class.java).equalTo("channelId", channelId).findFirst()
            if (item != null) {
                if (item.isLoaded) {
                    //update the found item
                    if (item.following == following) {
                        //already
                        DebugLog.e("already-------------")
                    } else {
                        item.following = following
                    }
                    item.time = time
                }
            } else {
                val x = it.createObject(SearchResultRealm::class.java, channelId)
//                x.channelId=channelId
                x.channelTitle = channelTitle
                x.time = time
                x.following = following
                x.thumbnailUrl = thumbnailUrl
            }
        }, {
            DebugLog.e("updateSearchResultRealm -------------- success")
            realm.close()
        }, {
            it.printStackTrace()
            realm.close()
        })
    }

    fun saveObject(obj: RealmObject) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(obj)
        realm.commitTransaction()
    }
}