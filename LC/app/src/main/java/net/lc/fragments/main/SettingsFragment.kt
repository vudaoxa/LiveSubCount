package net.lc.fragments.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_settings.*
import net.lc.R
import net.lc.adapters.settings.GroupSettingsRvAdapter
import net.lc.fragments.MBaseFragment
import net.lc.models.GroupSettings
import net.lc.models.ICallbackOnClick
import net.lc.presenters.SettingsPresenter

/**
 * Created by HP on 11/28/2016.
 */
class SettingsFragment : MBaseFragment(), ICallbackOnClick {
    companion object {
        fun newInstance(): SettingsFragment {
            val fragment = SettingsFragment()
            return fragment
        }
    }

    private var mSettingsPresenter: SettingsPresenter? = null
    private var groupSettingsRvAdapter: GroupSettingsRvAdapter? = null
    val mDisposables = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSettingsPresenter = SettingsPresenter(activity)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mSettingsPresenter?.apply { initSharedPrefrs(this@SettingsFragment) }
    }

    override fun onDestroy() {
        super.onDestroy()
        mDisposables.dispose()
    }

    fun initSettings(groupSettings: MutableList<GroupSettings>) {
        if (groupSettingsRvAdapter != null) return
        groupSettingsRvAdapter = GroupSettingsRvAdapter(activity, groupSettings, this)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.adapter = groupSettingsRvAdapter
    }

    override fun onClick(position: Int, event: Int) {
        mSettingsPresenter!!.toggleChildSetting(event)
    }
}