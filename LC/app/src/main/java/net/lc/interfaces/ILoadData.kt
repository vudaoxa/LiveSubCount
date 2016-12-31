package net.lc.interfaces

import android.view.View

/**
 * Created by mrvu on 12/29/16.
 */
interface ILoadData {
    fun getMinSize(): Int
    fun <T> isEmptyData(list: MutableList<T>?): Boolean
    fun isEmptyData()
    fun hideLoadMoreIndicator()
    fun onLoadDataFailure(listener: View.OnClickListener)
    fun onLoadDataFailure()
    fun onNoInternetConnection(listener: View.OnClickListener)
    fun onNoInternetConnection()
//    fun reload()
//    fun showLoading()
}