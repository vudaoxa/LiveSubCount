package net.lc.fragments.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.ViewSwitcher
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.gms.ads.AdListener
import com.tieudieu.util.DebugLog
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fragment_live_count.*
import net.lc.fragments.MBaseFragment
import net.lc.holders.BaseViewHolder
import net.lc.models.*
import net.lc.presenters.LCPresenter
import net.lc.presenters.MRealmPresenter
import net.lc.presenters.SearchPresenter
import net.lc.presenters.SettingsPresenter
import net.lc.utils.*
import net.live.sub.R
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

/**
 * Created by HP on 11/28/2016.
 */
class LiveCountFragment(val mMainFragment: MainFragment) : MBaseFragment(), IFields,
        ICallbackChannelListResponse, ICallbackSearchResult, IConnect, ICallbackSearch, ICallbackFollowing
        , ICallbackSearchResultRealm, IRandomChannel, ICallbackCheckFollowing, ICallbackSharing, ICallbackRefresh {
    private var d: Disposable? = null
    val mSubscription = CompositeSubscription()
    private var rSearchResult: RSearchResult? = null
    val mDisposables = CompositeDisposable()
    private var mLCPresenter: LCPresenter? = null
    private var mSearchPresenter: SearchPresenter? = null
    //    private val valve = PublishProcessor.create<Boolean>()
    private var valveOpened = false
    private var startLoad: Boolean = false
    private var currentSubCount: Long? = 0

    companion object {
        fun newInstance(mMainFragment: MainFragment): LiveCountFragment {
            val fragment = LiveCountFragment(mMainFragment)
            mMainFragment.mCallbackSearchResult = fragment
            mMainFragment.mCallbackFollowing = fragment
            mMainFragment.mCallbackSearchResultRealm = fragment
            mMainFragment.mCallbackSharing = fragment
            return fragment
        }
    }

    private val listenerLoadChecklist = View.OnClickListener {
        reload()
    }

    val mCallbackLoadChecklist = CallbackBottomMessage(true, R.string.internet_no_conection, R.string.redo,
            true, listenerLoadChecklist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLCPresenter = LCPresenter()
        mSearchPresenter = SearchPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_live_count, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //test
//        loadRandomYoutube()
        if (mMainFragment.created) {
            requestRandomChannel()
            mMainFragment.created = false
        } else {
            obtainSearchResultRealm()
        }
    }

    override fun onResume() {
        super.onResume()
        DebugLog.e("onresume-------------------------------------")
        if (!valveOpened) {
            toggle(d, true)
        }
    }

    override fun onPause() {
        super.onPause()
        DebugLog.e("onpause---------------------+++")
//        toggle(d, false) xx
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLog.e("onDestroy--------------------------")
        dispose()
    }

    override fun isFollowing(): Boolean {
        rSearchResult?.apply {
            return following == 1
        }
        return false
    }
    override fun onSearch(query: String, pageToken: String?) {
        sendHit(CATEGORY_ACTION, ACTION_QUICK_SEARCH)
        showLoading(true)
        val mSearchRequest = SearchRequest(query, null, pageToken)
        showMessageBottom(mCallbackDismiss)
        requestSearch(mSearchRequest)
    }

    fun obtainSearchResultRealm() {
        MRealmPresenter.apply {
            this.obtainSearchResultRealm(this@LiveCountFragment)
        }
    }

    fun requestRandomChannel() {
        mLCPresenter!!.requestRandomChannel(this, MDateUtils.currentDate())
    }

    private fun requestSearch(mSearchRequest: SearchRequest) {
        mLCPresenter?.requestQSearchLC(this, Constants.API_KEY, Constants.PART_SNIPPET, Constants.TYPE_CHANNEL
                , mSearchRequest.nextPageToken, mSearchRequest.query)
    }

    override fun onLoadDataFailure() {
        mCallbackLoadChecklist.content = R.string.error_conection
        onLoadDataFailure(mCallbackLoadChecklist)
        dispose()
    }

    override fun onLoadRandomChannelFailure() {
        DebugLog.e("onLoadRandomChannelFailure-------------")
        //load @searchResultRealm from local db
        //xx
        obtainSearchResultRealm()
    }

    fun loadRandomYoutube() {
        val name = topYtbChannels[Math.round(Math.random() * (topYtbChannels.size - 1)).toInt()]
        DebugLog.e("loadRandomYoutube----------- $name")
        mLCPresenter?.requestChannelsInfo(this, Constants.API_KEY, Constants.PART_SNIPPET, null, name, -1)
    }

    fun onObtainSearchResultRealmFailure() {
        DebugLog.e("onObtainSearchResultRealmFailure------------------")
        loadRandomYoutube()
    }

    override fun onNoInternetConnection() {
        mCallbackLoadChecklist.content = R.string.internet_no_conection
        onNoInternetConnection(mCallbackLoadChecklist)
        dispose()
    }

    val DURATION = 5L
    fun loadLC(searchResultRealm: RSearchResult) {
        if (valveOpened) {
            return
        }
        DebugLog.e("loadLC---------------------")
        startLoad = true
        showLoading(true)
        rSearchResult = searchResultRealm
        val consumer: Consumer<Long>?
        val id = searchResultRealm.channelId
        consumer = Consumer {
            if (valveOpened) {
                //statistics
                DebugLog.e(it.toString() + "-----")
                mLCPresenter?.requestChannelsInfo(this@LiveCountFragment, Constants.API_KEY,
                        Constants.PART_STATISTICS, id, null, DURATION * 1000)
            }
        }
        d = Flowable.interval(0, DURATION, TimeUnit.SECONDS)
//                .compose(FlowableTransformers.valve<Long>(valve, true)) xx
                .subscribe(consumer)
        toggle(d, true)
    }

    override fun onSearchResultReceived(searchResult: SearchResult) {
        searchResult.apply {
            rSearchResult?.apply {
                if (channelId == idInfo!!.channelId) {
                    return
                }
            }
            currentSubCount = 0
            toggle(d, false)
            dispose()
            showLoading(true)
            snippet?.apply {
                val searchResultRealm = RSearchResult(idInfo!!.channelId,
                        channelTitle, statistics?.subscriberCount, description, thumbnails?.medium?.url, System.currentTimeMillis())
                searchResultRealm.apply {
                    updateViewChannelInfo(this)
                }
                loadLC(searchResultRealm)
            }
        }
    }

    override fun onSearchResultRealmReceived(searchResultRealm: RSearchResult) {
        this.rSearchResult?.apply {
            if (channelId == searchResultRealm.channelId) {
                return
            }
        }

        currentSubCount = 0
        toggle(d, false)
        dispose()
        showLoading(true)
        searchResultRealm.apply {
            MRealmPresenter.updateSearchResultRealm(channelId, channelTitle, subscriberCount,
                    description, following, System.currentTimeMillis(), thumbnailUrl, false)
        }
        updateViewChannelInfo(searchResultRealm)
        loadLC(searchResultRealm)
    }

    override fun onSharing() {
        rSearchResult?.apply {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, NetConstants.YOUTUBE_CHANNEL_BASE_URL + channelId)
            startActivity(Intent.createChooser(shareIntent, "Share link using"))
        }
    }
    override fun onFollowing() {
        DebugLog.e("onFollowing ------------------")
        rSearchResult?.apply {
            if (following == 1) {
                showMessage(activity, activity.getString(R.string.already_following, channelTitle), time = 0)
            } else {
                MRealmPresenter.apply {
                    followingCallback = this@LiveCountFragment
                    rSearchResult = updateSearchResultRealm(channelId, channelTitle, currentSubCount.toString(),
                            description, 1, System.currentTimeMillis(), thumbnailUrl, true)
                }
            }
        }
    }

    fun onFollowSuccess() {
        rSearchResult?.apply {
            showMessage(activity, activity.getString(R.string.follow_success, channelTitle), 0)
        }
    }

    fun onFeaturedResponse(response: String) {
        val texts = response.split("\r\n")
        requestFeaturedChannelInfo(texts[0])
    }

    fun requestFeaturedChannelInfo(id: String) {
        DebugLog.e("requestFeaturedChannelInfo+++++++++++++++++++")
        mLCPresenter?.requestChannelsInfo(this, Constants.API_KEY, Constants.PART_SNIPPET, id, null, DURATION * 1000)
    }

    fun dispose() {
        DebugLog.e("dispose-----------")
        d?.dispose()
        mDisposables.clear()
        mSubscription.unsubscribe()
        valveOpened = false
    }

    fun updateViewChannelInfo(searchResultRealm: RSearchResult?) {
        searchResultRealm?.apply {
            updateViewChannelInfo(channelTitle, description, thumbnailUrl)
        }
    }

    fun updateViewChannelInfo(title: String?, des: String?, thumbNailUrl: String?) {
        tv_name.text = title
        img_avatar.setImageURI(thumbNailUrl)
        tv_des.text = des
    }

    override fun initFields() {
        showLoading(true)
        initAnims()
        initSwipe()
        img_avatar.setOnClickListener { onClick(BaseViewHolder.ACTION_CLICK_CHANNEL) }
        btn_ytb_view.setOnClickListener { onClick(BaseViewHolder.ACTION_CLICK_CHANNEL) }
        tv_name.setOnClickListener { onClick(BaseViewHolder.ACTION_QUICK_SEARCH) }
        mInterstitialAd = initInterAds(activity, adListener)
    }

    fun initSwipe() {
        swipeContainer?.apply {
            if (pullToRefreshEnabled()) {
                setOnRefreshListener { onRefresh() }
                setColorSchemeResources(*pullToRefreshColorResources)
            } else {
                isEnabled = false
            }
        }
    }

    fun initAnims() {
        sw_count_subs.inAnimation = animIn
        sw_count_subs.outAnimation = animOut
        sw_count_vids.inAnimation = animIn
        sw_count_vids.outAnimation = animOut
        sw_count_views.inAnimation = animIn
        sw_count_views.outAnimation = animOut
        sw_count_subs.setFactory(mFactoryCountSubs)
        sw_count_vids.setFactory(mFactoryCountVids)
        sw_count_views.setFactory(mFactoryCountViews)
    }

    fun onClick(event: Int) {
        mEvent = event
        val x = rand(4)
        if (x < 2) {
            action()
        } else {
            if (!showInterAds(mInterstitialAd)) {
                action()
            }
        }
    }

    fun action() {
        when (mEvent) {
            BaseViewHolder.ACTION_CLICK_CHANNEL -> goYoutube()
            BaseViewHolder.ACTION_QUICK_SEARCH -> showDialogSearchChannel()
        }
    }
    fun goYoutube() {
        sendHit(CATEGORY_ACTION, ACTION_GO_YOUTUBE)
        rSearchResult?.apply {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(NetConstants.YOUTUBE_CHANNEL_BASE_URL + channelId)))
        }
    }

    fun isEmptyQSearchResult() {
        showLoading(false)
        InputUtils.showMessage(activity, R.string.empty_data)
    }

    override fun showLoading(isShow: Boolean) {
        super.showLoading(isShow)
        val visibility = if (isShow) View.GONE else View.VISIBLE
        layout_lc.visibility = visibility
    }

    //update upon to @channelListResponse
    override fun update(channelListResponse: ChannelListResponse, part: String) {
        when (part) {
            Constants.PART_STATISTICS -> {
                val channelStatistics = channelListResponse.channelsInfo!!
                updateLC(channelStatistics[0].statistics!!)
            }
            Constants.PART_SNIPPET -> {
                updateSnippet(channelListResponse)
            }
        }
    }

    fun updateSnippet(channelListResponse: ChannelListResponse) {
        channelListResponse.apply {
            channelsInfo?.apply {
                if (isEmpty()) {
                    DebugLog.e("channelsInfo ---- empty")
                    onLoadDataFailure()
                    return
                }
                val channelInfo = channelsInfo[0]
                channelInfo.apply {
                    snippet?.apply {
                        val searchResultRealm = RSearchResult(id,
                                title, statistics?.subscriberCount, description, thumbnails?.medium?.url, System.currentTimeMillis())
                        searchResultRealm.apply {
                            MRealmPresenter.updateSearchResultRealm(channelId, channelTitle,
                                    subscriberCount, description, -1, System.currentTimeMillis(), thumbnailUrl, false)
                        }
                        updateViewChannelInfo(searchResultRealm)
                        loadLC(searchResultRealm)
                    }
                }
            }
        }
    }

    //    var count=0L
    fun updateLC(statistic: ItemStatistics) {
        if (startLoad) {
            showLoading(false)
            startLoad = false
        }
        statistic.apply {

            rSearchResult?.apply {
                subscriberCount = statistic.subscriberCount
                MRealmPresenter.updateSearchResultRealm(channelId, channelTitle,
                        statistic.subscriberCount, description, -1, System.currentTimeMillis(), thumbnailUrl, false)
            }
            val lSubscriberCount = subscriberCount?.toLong()

            val transitionSetting = SettingsPresenter.getSettingsMapItem(3)
            val moreStatisticSetting = SettingsPresenter.getSettingsMapItem(2)
            val audioSetting = SettingsPresenter.getSettingsMapItem(1)
            layout_more_statistic.visibility = if (moreStatisticSetting.checked) View.VISIBLE else View.GONE
            var audioPath: String? = null
            if (transitionSetting.checked) {
                if (currentSubCount != lSubscriberCount) {
                    if (audioSetting.checked) {
                        audioPath = if (currentSubCount != 0L) {
                            if (lSubscriberCount!! > currentSubCount!!) AudioConstants.PATH_INCRE else AudioConstants.PATH_DECRE
                        } else null
                    }
                    currentSubCount = lSubscriberCount
                    sw_count_subs.setText(numberFormat.format(lSubscriberCount))
                }
                if (moreStatisticSetting.checked) {
                    sw_count_vids.setText(numberFormat.format(videoCount?.toLong()))
                    sw_count_views.setText(numberFormat.format(viewCount?.toLong()))
                }
            } else {
                if (currentSubCount != lSubscriberCount) {
                    if (audioSetting.checked) {
                        audioPath = if (currentSubCount != 0L) {
                            if (lSubscriberCount!! > currentSubCount!!) AudioConstants.PATH_INCRE else AudioConstants.PATH_DECRE
                        } else null
                    }
                    currentSubCount = lSubscriberCount
                    sw_count_subs.setCurrentText(numberFormat.format(lSubscriberCount))
                }
                if (moreStatisticSetting.checked) {
                    sw_count_vids.setCurrentText(numberFormat.format(videoCount?.toLong()))
                    sw_count_views.setCurrentText(numberFormat.format(viewCount?.toLong()))
                }
            }
            audioPath?.apply {
                AudioPlayer.getInstance(activity).playAudio(this, false)
            }
        }
        val settingChannelThumb = SettingsPresenter.getSettingsMapItem(4)
        img_avatar.visibility = if (settingChannelThumb.checked) View.VISIBLE else View.GONE
        btn_ytb_view.visibility = if (settingChannelThumb.checked) View.GONE else View.VISIBLE
        val settingDescription = SettingsPresenter.getSettingsMapItem(5)
        tv_des.visibility = if (settingDescription.checked) View.VISIBLE else View.GONE
    }

    fun showDialogSearchChannel() {
        MaterialDialog.Builder(activity)
                .inputType(InputType.TYPE_CLASS_TEXT or
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
                .positiveText(R.string.OK)
                .input(R.string.search_topic, 0, false, { dialog, input -> onSearch(input.toString(), null) })
                .show()
    }

    fun toggle(d: Disposable?, on: Boolean) {
        d?.apply {
            if (isDisposed) {
                return
            }
            valveOpened = on
//            valve.onNext(on) xx
        }
    }

    fun reload() {
        if (valveOpened) {
            DebugLog.e("reload---------return")
            return
        }
        showLoading(true)
        showMessageBottom(mCallbackDismiss)
//        obtainSearchResultRealm()
        loadRandomYoutube()
//        requestRandomChannel()
    }

    override fun pullToRefreshEnabled() = true
    override val pullToRefreshColorResources: IntArray
        get() = intArrayOf(R.color.colorPrimary, R.color.blue, R.color.green)

    override fun onRefresh() {
        Handler().postDelayed({
            reload()
            setRefreshing(false)
        }, 2000)
    }

    override fun setRefreshing(refreshing: Boolean) {
        swipeContainer?.apply {
            isRefreshing = refreshing
        }
    }

    private val mFactoryCountSubs = ViewSwitcher.ViewFactory {
        val t = TextView(activity)
        t.apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
//            setTextColor(Color.WHITE)
            textSize = 45F
        }
        t
    }
    private val mFactoryCountViews = ViewSwitcher.ViewFactory {
        val t = TextView(activity)
        t.apply {
            gravity = Gravity.CENTER_VERTICAL
//            setTextColor(Color.WHITE)
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            textSize = 20F
        }
        t
    }
    private val mFactoryCountVids = ViewSwitcher.ViewFactory {
        val t = TextView(activity)
        t.apply {
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
//            setTextColor(Color.WHITE)
            textSize = 20F
        }
        t
    }
    val adListener = object : AdListener() {
        override fun onAdClosed() {
            DebugLog.e("onAdClosed--------------")
            sendHit(CATEGORY_ACTION, ACTION_onAdClosed)
            action()
            requestNewInterstitial(mInterstitialAd)
        }

        override fun onAdFailedToLoad(p0: Int) {
            sendHit(CATEGORY_ACTION, mapAdsErrors.get(p0) as String)
        }

        override fun onAdOpened() {
            sendHit(CATEGORY_ACTION, ACTION_onAdOpened)
        }

        override fun onAdLoaded() {
            sendHit(CATEGORY_ACTION, ACTION_onAdLoaded)
        }

        override fun onAdLeftApplication() {
            sendHit(CATEGORY_ACTION, ACTION_onAdLeftApplication)
        }
    }
}