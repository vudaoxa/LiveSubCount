package net.lc.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.android.gms.ads.InterstitialAd
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.layout_progress_view.*
import kotlinx.android.synthetic.main.layout_show_message.*
import net.lc.models.ICallbackLoadMore
import net.lc.utils.CallbackBottomMessage
import net.lc.views.rv.EndlessRecyclerViewScrollListener
import net.live.sub.R

/**
 * Created by mrvu on 12/29/16.
 */
abstract class MBaseFragment : BaseFragmentStack() {
    val mCallbackDismiss = CallbackBottomMessage(isShow = false,
            btnText = R.string.OK, content = R.string.OK, isShowRetry = false, onClickListener = null)
    var mInterstitialAd: InterstitialAd? = null
    var mPosition = 0
    var mEvent = 0
    fun showMessageBottom(callbackBottomMessage: CallbackBottomMessage) {
        lcontent?.apply {
            callbackBottomMessage.apply {
                if (!isShow) {
                    visibility = View.GONE
                    return
                }
                tv_message.setText(content)
                btn_retry.visibility = if (isShowRetry) View.VISIBLE else View.GONE
                if (btnText != 0) {
                    btn_retry.setText(btnText)
                }
                btn_retry.setOnClickListener(onClickListener)
                visibility = View.VISIBLE
            }
        }
    }

    open fun showLoading(isShow: Boolean) {
        avi?.apply {
            if (isShow) show()
            else hide()
        }
    }

    fun setupOnLoadMore(rv: RecyclerView, mCallbackLoadMore: ICallbackLoadMore) {
        rv.addOnScrollListener(
                object : EndlessRecyclerViewScrollListener(rv.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        mCallbackLoadMore.onLoadMore()
                    }
                })
    }

    fun onLoadDataFailure(callbackBottomMessage: CallbackBottomMessage) {
        showLoading(false)
        showMessageBottom(callbackBottomMessage)
    }

    fun onNoInternetConnection(callbackBottomMessage: CallbackBottomMessage) {
        showLoading(false)
        showMessageBottom(callbackBottomMessage)
    }
}