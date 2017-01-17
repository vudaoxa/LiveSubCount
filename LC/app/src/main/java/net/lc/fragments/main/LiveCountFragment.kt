package net.lc.fragments.main

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ViewSwitcher
import com.afollestad.materialdialogs.MaterialDialog
import com.tieudieu.util.DebugLog
import hu.akarnokd.rxjava2.operators.FlowableTransformers
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.processors.PublishProcessor
import kotlinx.android.synthetic.main.fragment_live_count.*
import net.lc.R
import net.lc.fragments.MBaseFragment
import net.lc.models.*
import net.lc.presenters.LCPresenter
import net.lc.presenters.MRealmPresenter
import net.lc.presenters.SearchPresenter
import net.lc.utils.*
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit


/**
 * Created by HP on 11/28/2016.
 */
class LiveCountFragment(val mMainFragment: MainFragment) : MBaseFragment(), IFields,
        ICallbackChannelListResponse, ICallbackSearchResult, IConnect, ICallbackSearch, ICallbackFollowing
        , ICallbackSearchResultRealm, IRandomChannel {
    private var d: Disposable? = null
    val mSubscription = CompositeSubscription()
    private var searchResultRealm: SearchResultRealm? = null
    val mDisposables = CompositeDisposable()
    private var mLCPresenter: LCPresenter? = null
    private var mSearchPresenter: SearchPresenter? = null
    private var mRealmPresenter: MRealmPresenter? = null
    private val valve = PublishProcessor.create<Boolean>()
    private var valveOpened: Boolean? = false
    private var startLoad: Boolean = false

    companion object {
        fun newInstance(mMainFragment: MainFragment): LiveCountFragment {
            val fragment = LiveCountFragment(mMainFragment)
            mMainFragment.mCallbackSearchResult = fragment
            mMainFragment.mCallbackFollowing = fragment
            mMainFragment.mCallbackSearchResultRealm = fragment
            return fragment
        }
    }

    private val onClickListenerLoadChecklist = View.OnClickListener {
        showLoading(true)
        showMessageBottom(mCallbackDismiss)
        searchResultRealm?.apply {
            loadLC(this, singleRequest = true)
        }
    }

    val mCallbackLoadChecklist = CallbackBottomMessage(true, R.string.internet_no_conection, R.string.redo,
            true, onClickListenerLoadChecklist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mLCPresenter = LCPresenter()
        mSearchPresenter = SearchPresenter()
        mRealmPresenter = MRealmPresenter()
        mLCPresenter?.mRealmPresenter = mRealmPresenter
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
        if (mMainFragment.created!!) {
            requestRandomChannel()
            mMainFragment.created = false
        } else {
            obtainSearchResultRealm()
        }
    }

    override fun onResume() {
        super.onResume()
        DebugLog.e("onresume-------------------------------------")
        if (!valveOpened!!) {
            toggle(d, true)
        }
    }

    override fun onPause() {
        super.onPause()
        DebugLog.e("onpause---------------------+++")
        toggle(d, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        DebugLog.e("onDestroy--------------------------")
        dispose()
    }

    override fun onSearch(query: String, pageToken: String?) {
        showLoading(true)
        val mSearchRequest = SearchRequest(query, null, pageToken)
        showMessageBottom(mCallbackDismiss)
        requestSearch(mSearchRequest)
    }

    fun obtainSearchResultRealm() {
        mRealmPresenter?.obtainSearchResultRealm(this)
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

    val duration = 5L
    fun loadLC(searchResultRealm: SearchResultRealm, singleRequest: Boolean) {
        if (valveOpened!!) {
            return
        }
        DebugLog.e("loadLC---------------------")
        startLoad = true
        showLoading(true)
        this.searchResultRealm = searchResultRealm
        val consumer: Consumer<Long>?
        val id = searchResultRealm.channelId
        consumer = Consumer {
            if (valveOpened!!) {
                //statistics
                DebugLog.e(it.toString() + "-----")
                mLCPresenter?.requestChannelsInfo(this@LiveCountFragment, Constants.API_KEY,
                        Constants.PART_STATISTICS, id, singleRequest, duration * 1000)
            }
        }
        d = Flowable.interval(0, duration, TimeUnit.SECONDS)
                .compose(FlowableTransformers.valve<Long>(valve, true))
                .subscribe(consumer)
        toggle(d, true)
    }

    override fun onSearchResultReceived(searchResult: SearchResult) {
        searchResultRealm?.apply {
            if (channelId == searchResult.idInfo!!.channelId) {
                return
            }
        }

        toggle(d, false)
        dispose()
        showLoading(true)
        //xx
        val searchResultRealm = SearchResultRealm(searchResult.idInfo!!.channelId,
                searchResult.snippet?.channelTitle, searchResult.snippet?.thumbnails?.medium?.url, System.currentTimeMillis())
        updateViewChannelInfo(searchResultRealm)
        searchResultRealm.apply {
            mRealmPresenter?.updateSearchResultRealm(this.channelId, this.channelTitle, this.following, System.currentTimeMillis(), this.thumbnailUrl)
        }
        loadLC(searchResultRealm, singleRequest = false)
    }

    override fun onSearchResultRealmReceived(searchResultRealm: SearchResultRealm) {
        this.searchResultRealm?.apply {
            if (channelId == searchResultRealm.channelId) {
                return
            }
        }

        toggle(d, false)
        dispose()
        showLoading(true)
        searchResultRealm.apply {
            mRealmPresenter?.updateSearchResultRealm(this.channelId, this.channelTitle, this.following, System.currentTimeMillis(), this.thumbnailUrl)
        }
        updateViewChannelInfo(searchResultRealm)
        loadLC(searchResultRealm, singleRequest = false)
    }

    override fun onFollowing() {
        DebugLog.e("onFollowing ------------------")
        searchResultRealm?.apply {
            mRealmPresenter?.updateSearchResultRealm(this.channelId, this.channelTitle, 1, System.currentTimeMillis(), this.thumbnailUrl)
        }
    }

    fun onFeaturedResponse(response: String) {
        val texts = response.split("\r\n")
        requestFeaturedChannelInfo(texts[0])
        if (texts.size > 1)
            tv_some.text = texts[1]
    }

    fun requestFeaturedChannelInfo(id: String) {
        DebugLog.e("requestFeaturedChannelInfo+++++++++++++++++++")
        mLCPresenter?.requestChannelsInfo(this, Constants.API_KEY, Constants.PART_SNIPPET, id, false, duration * 1000)
    }

    fun dispose() {
        DebugLog.e("dispose-----------")
        d?.dispose()
        mDisposables.clear()
        mSubscription.unsubscribe()
    }

    fun updateViewChannelInfo(searchResultRealm: SearchResultRealm?) {
        searchResultRealm?.apply {
            updateViewChannelInfo(channelTitle!!, thumbnailUrl!!)
        }
    }

    fun updateViewChannelInfo(title: String, thumbNailUrl: String) {
        tv_name.text = title
        img_avatar.setImageURI(thumbNailUrl)
    }

    override fun initFields() {
        showLoading(true)
        initSwCountLC()
        img_avatar.setOnClickListener { goYoutube() }
        tv_name.setOnClickListener { showDialogSearchChannel() }
    }

    fun initSwCountLC() {
        val animIn = AnimationUtils.loadAnimation(activity,
                android.R.anim.fade_in)
        val animOut = AnimationUtils.loadAnimation(activity,
                android.R.anim.fade_out)
        animIn.duration = Constants.ANIM_DURATION
        animOut.duration = Constants.ANIM_DURATION
        sw_count.setInAnimation(animIn)
        sw_count.setOutAnimation(animOut)
        sw_count.setFactory(mFactory)
    }

    fun goYoutube() {
        searchResultRealm?.apply {
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
                updateLC(channelStatistics[0].statistics?.subscriberCount!!)
            }
            Constants.PART_SNIPPET -> {
                updateSnippet(channelListResponse)
            }
        }
    }

    fun updateSnippet(channelListResponse: ChannelListResponse) {
        val channelInfo = channelListResponse.channelsInfo!![0]
        val searchResultRealm = SearchResultRealm(channelInfo.id,
                channelInfo.snippet?.title, channelInfo.snippet?.thumbnails?.medium?.url, System.currentTimeMillis())
        searchResultRealm.apply {
            mRealmPresenter?.updateSearchResultRealm(this.channelId, this.channelTitle, 0, System.currentTimeMillis(), this.thumbnailUrl)
        }
        updateViewChannelInfo(searchResultRealm)
        loadLC(searchResultRealm, false)
    }

    fun updateLC(count: String) {
        if (startLoad) {
            showLoading(false)
            startLoad = false
        }
        sw_count.setText(count)
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
            valve.onNext(on)
        }
    }

    private val mFactory = ViewSwitcher.ViewFactory {
        // Create a new TextView
        val t = TextView(activity)
        t.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        t.setTextColor(Color.WHITE)
        t.textSize = 40F
        t
    }
}