package net.lc.fragments.main

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tieudieu.util.DebugLog
import kotlinx.android.synthetic.main.fragment_init_notis.*
import net.lc.activities.MainActivity
import net.lc.adapters.InitialNotificationsRvAdapter
import net.lc.fragments.MBaseFragment
import net.lc.holders.TYPE_MILESTONE
import net.lc.holders.TYPE_NOTI_APPROACH
import net.lc.holders.TYPE_NOTI_REACH
import net.lc.holders.TYPE_SUBSCRIBER_AWAY
import net.lc.models.*
import net.lc.presenters.MRealmPresenter
import net.lc.utils.*
import net.live.sub.R

/**
 * Created by tusi on 2/2/17.
 */
class InitialNotificationsFragment(val followingChannelsFragment: FollowingChannelsFragment, val rSearchResult: RSearchResult)
    : MBaseFragment(), ICallbackOnClick, ICallbackSubmit, IBackListener {
    companion object {
        fun newInstance(followingChannelsFragment: FollowingChannelsFragment, rSearchResult: RSearchResult): InitialNotificationsFragment {
            val fragment = InitialNotificationsFragment(followingChannelsFragment, rSearchResult)
            followingChannelsFragment.mCallbackChange = fragment
            return fragment
        }
    }

    var mMilestone = 0L
    var mSubscriberAway = 0L
    var mInitialNotisRvAdapter: InitialNotificationsRvAdapter? = null
    var mNotiItems: List<ToggleNotis>? = null
    //    private var mRealmPresenter: MRealmPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MRealmPresenter.notiCallback = this
        title = getString(R.string.notifications)
        back = true
        if (activity is MainActivity) {
            (activity as MainActivity).apply {
                mBackListener = this@InitialNotificationsFragment
                mCallbackSubmit = this@InitialNotificationsFragment
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_init_notis, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initFields()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun initFields() {
        initNotiItems()
        tvTitle.text = rSearchResult.channelTitle
        onChange()
    }

    fun initNotiItems() {
        val notiApproach = ToggleNotis(activity, R.string.noti_approach, rSearchResult, TYPE_NOTI_APPROACH, rSearchResult.notiApproachMilestone == 1)
        val notiReach = ToggleNotis(activity, R.string.noti_reach, rSearchResult, TYPE_NOTI_REACH, rSearchResult.notiReachMilestone == 1)
        val inputMilestone = ToggleNotis(activity, R.string.milestone, rSearchResult, TYPE_MILESTONE, notiApproach.checked || notiReach.checked)
        val inputSubAway = ToggleNotis(activity, R.string.sub_away, rSearchResult, TYPE_SUBSCRIBER_AWAY, notiApproach.checked)
        mNotiItems = listOf(notiApproach, notiReach, inputMilestone, inputSubAway)
        mInitialNotisRvAdapter = InitialNotificationsRvAdapter(activity, rSearchResult, mNotiItems!!, this)
        rv.adapter = mInitialNotisRvAdapter
        rv.layoutManager = LinearLayoutManager(activity)
    }

    override fun onBackPressed() {
        activity?.apply {
            InputUtils.hideKeyboard(this)
            onBackPressed()
        }
    }

    override fun onSubmit() {
        DebugLog.e("onSubmit----------------------")
        if (!validate()) return
    }

    fun onChange() {
        if (isVisible) {
            rSearchResult.apply {
                tvSubscribers?.text = getString(R.string.subscribers, numberFormat.format(subscriberCount!!.toLong()))
            }
        }
    }

    fun validate(): Boolean {
        var error = ""
        mInitialNotisRvAdapter?.apply {
            val notiApproach = if (data[0].checked) 1 else 0
            val notiReach = if (data[1].checked) 1 else 0
            rSearchResult.apply {
                MRealmPresenter.apply {
                    if (notiApproach == 1) {
                        if (data[2].validate() && data[3].validate()) {
                            mMilestone = data[2].value!!
                            mSubscriberAway = data[3].value!!
                            if (mSubscriberAway > mMilestone - subscriberCount!!.toLong()) {
                                val subAway = numberFormat.format(mSubscriberAway)
                                val ml = numberFormat.format(mMilestone)
                                error = getString(R.string.sub2mile, channelTitle, subAway, ml)
                                showMessage(activity, error, 1)
                                return false
                            }
                            updateChannelInitialNoti(channelId!!, notiApproach,
                                    notiReach, mMilestone, mSubscriberAway)
                        }
                    } else if (notiReach == 1) {
                        if (data[2].validate()) {
                            mMilestone = data[2].value!!
                            updateChannelInitialNoti(channelId!!, notiApproach,
                                    notiReach, mMilestone, subscriberAway!!)
                        }
                    } else {
                        updateChannelInitialNoti(channelId!!, notiApproach,
                                notiReach, milestone!!, subscriberAway!!)
                    }
                }
            }
        }
        return false
    }

    fun onSubmitSuccess() {
        rSearchResult.apply {
            if (mMilestone != 0L)
                milestone = mMilestone
            if (mSubscriberAway != 0L)
                subscriberAway = mSubscriberAway
        }
        followingChannelsFragment.apply {
            onSubmitSuccess()
        }
        onBackPressed()
    }

    override fun onClick(position: Int, event: Int) {
        mInitialNotisRvAdapter?.apply {
            data[2].checked = data[0].checked || data[1].checked
            data[3].checked = data[0].checked
            sendHit(CATEGORY_ACTION, ACTION_TOGGLE + "__" + data[position].type)
            when (position) {
                0 -> {
                    Handler().post {
                        notifyItemChanged(2)
                        notifyItemChanged(3)
                    }
                }
                1 -> {
                    Handler().post {
                        notifyItemChanged(2)
                    }
                }
            }
        }
    }
}