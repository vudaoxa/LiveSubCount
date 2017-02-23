package net.lc.presenters

import android.content.Context
import com.tieudieu.util.DebugLog
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject
import io.realm.Sort
import net.lc.fragments.main.FollowingChannelsFragment
import net.lc.fragments.main.InitialNotificationsFragment
import net.lc.fragments.main.LiveCountFragment
import net.lc.fragments.main.NotificationsFragment
import net.lc.fragments.search.FirstSuggestionFragment
import net.lc.models.ICallBackBadge
import net.lc.models.RSearchResult
import net.lc.models.SearchQueryRealm
import net.lc.models.SearchResultRealm
import rx.Observable
import rx.android.schedulers.AndroidSchedulers

/**
 * Created by mrvu on 1/15/17.
 */
object MRealmPresenter {
    var isRequest: Boolean = false
    fun loadSearchHistory(mFirstSuggestionFragment: FirstSuggestionFragment) {
        val realm = Realm.getDefaultInstance()
        val d = realm.where(SearchQueryRealm::class.java)
                .findAllSortedAsync("time", Sort.DESCENDING).asObservable()
                .filter { it.isLoaded }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    DebugLog.e("loadSearchHistory------------isLoaded")
                    mFirstSuggestionFragment.apply {
                        onChange(it)
                        it.addChangeListener {
                            onChange(it)
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
        DebugLog.e("loadFollowingChannels----------")
        val realm = Realm.getDefaultInstance()
        val d = Observable.just(realm.where(SearchResultRealm::class.java).equalTo("following", 1)
                .findAll())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isLoaded) {
                        DebugLog.e("loadFollowingChannels----------isLoaded")
                        mFollowingChannelsFragment.apply {
                            if (isAdded && isVisible) {
                                onChange(it)
                                loadLC()
                                it.addChangeListener {
                                    onChange(it)
                                }
                                if (it.isEmpty()) {
                                    isEmptyData()
                                }
                            }
                        }
                    }
                }, {
                    it.printStackTrace()
                    realm.close()
                }, {
                    //                    realm.close()
                })
        mFollowingChannelsFragment.mSubscription.add(d)
    }

    fun loadNotis(notificationsFragment: NotificationsFragment) {
        DebugLog.e("loadNotis----------")
        val realm = Realm.getDefaultInstance()
        val d = Observable.just(realm.where(SearchResultRealm::class.java).equalTo("noti", 1)
                .findAll())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.isLoaded) {
                        DebugLog.e("loadNotis----------isLoaded")
                        notificationsFragment.apply {
                            if (isAdded && isVisible) {
                                onChange(it)
                                it.addChangeListener {
                                    onChange(it)
                                }
                                if (it.isEmpty()) {
                                    isEmptyData()
                                }
                            }
                        }
                    }
                }, {
                    it.printStackTrace()
                    realm.close()
                }, {
                    //                    realm.close()
                })
        notificationsFragment.mSubscription.add(d)
    }

    fun obtainSearchResultRealm(mLiveCountFragment: LiveCountFragment) {
        if (isRequest) return
        isRequest = true
        DebugLog.e("obtainSearchResultRealm======================")
        val realm = Realm.getDefaultInstance()
        val d = Observable.just(realm.where(SearchResultRealm::class.java)
                .findAllSorted("time", Sort.DESCENDING))
//                .filter { it.isLoaded }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mLiveCountFragment.apply {
                        if (isAdded && isVisible) {
                            if (it.isLoaded) {
                                DebugLog.e("obtainSearchResultRealm======================isLoaded")
                                if (it.isNotEmpty()) {
                                    val searchResultRealm = it[0]
                                    searchResultRealm?.apply {
                                        val rSearchResult = this.clone()
                                        rSearchResult.apply {
                                            //test
//                                            onObtainSearchResultRealmFailure()

                                            updateViewChannelInfo(this)
                                            loadLC(this)
                                        }
                                    }
                                } else {
                                    onObtainSearchResultRealmFailure()
                                }
                            } else {
                                onObtainSearchResultRealmFailure()
                            }
                        }
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

    fun updateChannelSubscriberCount(channelId: String?, subscriberCount: String?,
                                     time: Long?): SearchResultRealm? {
        DebugLog.e("updateChannelSubscriberCount-----------")
        var res: SearchResultRealm? = null
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            val item = it.where(SearchResultRealm::class.java).equalTo("channelId", channelId).findFirst()
            item?.apply {
                if (isLoaded) {
                    this.subscriberCount = subscriberCount
                    this.time = time
                    res = item
                }
            }
        }, {
            DebugLog.e("updateChannelSubscriberCount -------------- success 00")
            realm.close()
        }, {
            it.printStackTrace()
            realm.close()
        })
        return res
    }

    fun updateChannelNotisRemoval(rSearchResult: RSearchResult): SearchResultRealm? {
        var res: SearchResultRealm? = null
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            rSearchResult.apply {
                val item = it.where(SearchResultRealm::class.java).equalTo("channelId", channelId).findFirst()
                item?.apply {
                    if (isLoaded) {
                        DebugLog.e("updateChannelNotisRemoval-----------000")
                        noti = rSearchResult.noti
                        res = this
                    }
                }
            }
        }, {
            DebugLog.e("updateChannelNotisRemoval -------------- success 04")
            removalCallback?.apply {
                onNotisSuccess()
            }
            badgeCallback?.apply {
                updateBadge()
            }
            realm.close()
        }, {
            it.printStackTrace()
            realm.close()
        })
        return res
    }

    fun updateChannelNotis(rSearchResult: RSearchResult): SearchResultRealm? {
        var res: SearchResultRealm? = null
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            rSearchResult.apply {
                val item = it.where(SearchResultRealm::class.java).equalTo("channelId", channelId).findFirst()
                item?.apply {
                    if (isLoaded) {
                        DebugLog.e("updateChannelNotis-----------000")
                        noti = rSearchResult.noti
                        notiDes = rSearchResult.notiDes
                        notiTime = rSearchResult.time
                        notiApproachMilestone = rSearchResult.notiApproachMilestone
                        notiReachMilestone = rSearchResult.notiReachMilestone
                        approached = rSearchResult.approached
                        reached = rSearchResult.reached
                        res = this
                    }
                }
            }
        }, {
            DebugLog.e("updateChannelNotis -------------- success 04")
            removalCallback?.apply {
                onNotisSuccess()
            }
            badgeCallback?.apply {
                updateBadge()
            }
            realm.close()
        }, {
            it.printStackTrace()
            realm.close()
        })
        return res
    }

    fun updateSearchResultRealm(channelId: String?, channelTitle: String?, subscriberCount: String?,
                                des: String?, following: Int?, time: Long?, thumbnailUrl: String?, onFollowing: Boolean): RSearchResult? {
        DebugLog.e("updateSearchResultRealm-----------")
        var res: RSearchResult? = null
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            val item = it.where(SearchResultRealm::class.java).equalTo("channelId", channelId).findFirst()
            item?.apply {
                if (isLoaded) {
                    if (following != -1) {
                        this.following = following
                    }
                    this.subscriberCount = subscriberCount
                    this.description = des
                    this.time = time
                    res = this.clone()
                }
                return@executeTransactionAsync
            }
            val x = it.createObject(SearchResultRealm::class.java, channelId)
            x.channelTitle = channelTitle
            x.subscriberCount = subscriberCount
            x.description = des
            x.time = time
            x.following = following
            x.thumbnailUrl = thumbnailUrl
            res = x.clone()
        }, {
            DebugLog.e("updateSearchResultRealm -------------- success 01")
            if (onFollowing && following == 1) {
                followingCallback?.apply {
                    onFollowSuccess()
                }
            }
            if (following == 0) {
                removalCallback?.apply {
                    isEmptyData()
                }
            }
            realm.close()
        }, {
            it.printStackTrace()
            realm.close()
        })
        return res
    }

    fun updateChannelAutoLC(channelId: String, autoLC: Int): SearchResultRealm? {
        DebugLog.e("updateChannelAutoLC-----------")
        var res: SearchResultRealm? = null
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            val item = it.where(SearchResultRealm::class.java).equalTo("channelId", channelId).findFirst()
            item?.apply {
                if (isLoaded) {
                    this.autoLC = autoLC
                    res = item
                }
            }
        }, {
            DebugLog.e("updateChannelAutoLC -------------- success 02")
            realm.close()
        }, {
            it.printStackTrace()
            realm.close()
        })
        return res
    }

    fun updateChannelInitialNoti(channelId: String, notiApproachMilestone: Int,
                                 notiReachMilestone: Int, milestone: Long, subAway: Long): SearchResultRealm? {
        DebugLog.e("updateChannelInitialNoti-----------")
        var res: SearchResultRealm? = null
        val realm = Realm.getDefaultInstance()
        realm.executeTransactionAsync({
            val item = it.where(SearchResultRealm::class.java).equalTo("channelId", channelId).findFirst()
            item?.apply {
                if (isLoaded) {
                    this.notiApproachMilestone = notiApproachMilestone
                    this.notiReachMilestone = notiReachMilestone
                    this.milestone = milestone
                    this.subscriberAway = subAway
                    approached = 0
                    reached = 0
                    res = this
                }
            }
        }, {
            DebugLog.e("updateChannelInitialNoti -------------- success 03")
            notiCallback?.apply {
                onSubmitSuccess()
            }
            realm.close()
        }, {
            it.printStackTrace()
            realm.close()
        })
        return res
    }

    var followingCallback: LiveCountFragment? = null
    var removalCallback: FollowingChannelsFragment? = null
    var notiCallback: InitialNotificationsFragment? = null
    var badgeCallback: ICallBackBadge? = null
    fun saveObject(obj: RealmObject) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(obj)
        realm.commitTransaction()
    }

    fun realmClose() {
        Realm.getDefaultInstance().close()
    }
}

fun initRealm(context: Context) {
    Realm.init(context)
    val realmConfig = RealmConfiguration.Builder()
            .name("lc.realm")
            .deleteRealmIfMigrationNeeded()
            .build()
//        Realm.deleteRealm(realmConfig)
    Realm.setDefaultConfiguration(realmConfig)

//        val realmConfig = RealmConfiguration.Builder(
//                this).deleteRealmIfMigrationNeeded().build()
//        Realm.setDefaultConfiguration(realmConfig)
}