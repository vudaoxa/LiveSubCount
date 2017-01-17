package net.lc.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.layout_progress_view.*
import kotlinx.android.synthetic.main.layout_show_message.*
import net.lc.R
import net.lc.models.ICallbackLoadMore
import net.lc.utils.CallbackBottomMessage

import net.lc.views.rv.EndlessRecyclerViewScrollListener

/**
 * Created by mrvu on 12/29/16.
 */
abstract class MBaseFragment : BaseFragmentStack() {
    //    val onClickListenerDismiss: View.OnClickListener = View.OnClickListener { showMessageBottom(mCallbackDismiss) }
    val mCallbackDismiss = CallbackBottomMessage(isShow = false,
            btnText = R.string.OK, content = R.string.OK, isShowRetry = false, onClickListener = null)

    fun showMessageBottom(callbackBottomMessage: CallbackBottomMessage) {
        if (!callbackBottomMessage.isShow) {
            layout_content.visibility = View.GONE
            return
        }
        tv_message.setText(callbackBottomMessage.content)
        btn_retry.visibility = if (callbackBottomMessage.isShowRetry) View.VISIBLE else View.GONE
        if (callbackBottomMessage.btnText != 0) {
            btn_retry.setText(callbackBottomMessage.btnText)
        }
        btn_retry.setOnClickListener(callbackBottomMessage.onClickListener)
        layout_content.visibility = View.VISIBLE
    }

    open fun showLoading(isShow: Boolean) {
        if (isShow) avi.show()
        else avi.hide()
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