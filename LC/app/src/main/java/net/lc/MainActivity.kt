package net.lc

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.jakewharton.rxbinding.widget.RxTextView
import com.tieudieu.fragmentstackmanager.BaseFragmentStack
import kotlinx.android.synthetic.main.layout_input_text.*
import net.lc.fragments.MainFragment
import net.lc.utils.Constants
import net.lc.utils.IndexTag
import net.lc.utils.InputUtil
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : ActionBarActivity() {
    override fun onMainScreenRequested() {
        fragmentStackManager.clearStack()
        fragmentStackManager.swapFragment(MainFragment())
    }

    override fun onFragmentEntered(fragment: Fragment?) {
        if ((fragment as BaseFragmentStack).showBackButton()) {
            showBtnBack()
        }

    }

    override fun onNewScreenRequested(indexTag: Int, typeContent: String?, `object`: Any?) {
        when(indexTag){
            IndexTag.FRAGMENT_MAIN-> {
                fragmentStackManager.clearStack()
                fragmentStackManager.swapFragment(MainFragment())
            }

        }
    }

    fun registerReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Constants.ACTION_CLICK_TOPIC_SUGGESTION)
        intentFilter.addAction(Constants.ACTION_CLICK_SEARCH_RESULT)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action.equals(Constants.ACTION_CLICK_TOPIC_SUGGESTION, ignoreCase = true)) {
                val bundle = intent.extras
                if (bundle != null) {
                    edt_search.text = null
                }
            } else if (action.equals(Constants.ACTION_CLICK_SEARCH_RESULT, ignoreCase = true)) {
                layout_input_text.visibility = View.GONE
                InputUtil.hideKeyboard(this@MainActivity)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initEdtSearch()
        registerReceiver()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    fun initEdtSearch(){
        edt_search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                // code here
                submitSearch()
                return@OnEditorActionListener true
            }
            false
        })
        RxTextView.afterTextChangeEvents(edt_search)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { tvChangeEvent ->
                    val s = tvChangeEvent.view().text.toString()
                    val text = s.trim { it <= ' ' }
                    if (text.length > 0) {
                        submitSearchSuggestion(text)
                    } else {
                        btn_close!!.visibility = View.GONE
                        submitSearchEmpty()
                    }
                }
        btn_close!!.setOnClickListener {
            edt_search!!.text = null
        }
    }
    fun submitSearch(){
        val query =edt_search.text.toString().trim()
        if(query.isEmpty())
            return
        signalSearch(query)
        InputUtil.hideKeyboard(this)
    }

    fun submitSearchSuggestion(text:String){

    }

    fun submitSearchEmpty(){

    }
    fun signalSearch(query: String){
        val bundle = Bundle()
        bundle.putString(Constants.KEY_SEARCH_QUERY, query)
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Constants.ACTION_SEARCH).putExtras(bundle))
    }
}
