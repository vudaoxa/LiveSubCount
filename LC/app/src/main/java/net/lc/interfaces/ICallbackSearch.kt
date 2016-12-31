package net.lc.interfaces

/**
 * Created by mrvu on 12/29/16.
 */
interface ICallbackSearch {
    fun onSearch(query: String, pageToken: String?)
}