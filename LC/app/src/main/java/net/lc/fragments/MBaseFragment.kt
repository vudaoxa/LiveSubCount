package net.lc.fragments

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.layout_progress_view.*
import kotlinx.android.synthetic.main.layout_show_message.*
import net.lc.R
import net.lc.interfaces.ICallbackLoadMore
import net.lc.interfaces.ILoadData
import net.lc.views.rv.EndlessRecyclerViewScrollListener

/**
 * Created by mrvu on 12/29/16.
 */
abstract class MBaseFragment : BaseFragmentStack(), ILoadData, ICallbackLoadMore {
    val onClickListenerDismiss: View.OnClickListener = View.OnClickListener { showMessageBottom(false, R.string.OK, 0, false, null) }


    fun showMessageBottom(isShow: Boolean, message: Int, btnText: Int,
                          isShowRetry: Boolean, onClickListener: View.OnClickListener?) {

        if (!isShow) {
            layout_content.visibility = View.GONE
            return
        }
        tv_message.setText(message)
        btn_retry.visibility = if (isShowRetry) View.VISIBLE else View.GONE
        if (btnText != 0) {
            btn_retry.setText(btnText)
        }
        if (onClickListener != null) {
            btn_retry.setOnClickListener(onClickListener)
        }
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

    override fun onLoadDataFailure(listener: View.OnClickListener) {
        showLoading(false)
        showMessageBottom(true, R.string.error_conection,
                R.string.redo, true, listener)
    }

    override fun onNoInternetConnection(listener: View.OnClickListener) {
        showLoading(false)
        showMessageBottom(true, R.string.internet_no_conection, R.string.redo,
                true, listener)
    }

    override fun onNoInternetConnection() {
        onNoInternetConnection(onClickListenerDismiss)
    }
}