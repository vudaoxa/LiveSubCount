package net.lc.presenters

import com.google.api.services.youtube.model.SearchListResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import net.lc.fragments.search.FirstSuggestionFragment
import net.lc.utils.Constants
import net.lc.utils.network.RxRetrofitService
import net.lc.utils.network.models.RetrofitException

/**
 * Created by mrvu on 12/28/16.
 */
class SearchPresenter {
    var isRequest = false

    companion object {
        var isFirs = true
    }

    fun requestSearchSuggestion(mFirstSuggestFragment: FirstSuggestionFragment,
                                query: String, pageToken: String?) {
        if (isRequest) return
        isRequest = true
        val disposableObserver = object : DisposableObserver<SearchListResponse>() {
            override fun onComplete() {
                isRequest = false
            }

            override fun onError(e: Throwable?) {
                if (mFirstSuggestFragment.isAdded && mFirstSuggestFragment.isVisible) {
                    val error = e as RetrofitException
                    when (error.kind) {
                        RetrofitException.Kind.NETWORK -> {
                            mFirstSuggestFragment.onNoInternetConnection()
                        }
                        else -> {
                            mFirstSuggestFragment.onLoadDataFailure()
                        }
                    }
                    mFirstSuggestFragment.hideLoadMoreIndicator()

                }
                isRequest = false
            }

            override fun onNext(t: SearchListResponse?) {
                if (mFirstSuggestFragment.isAdded && mFirstSuggestFragment.isVisible) {
                    if (t != null) {
                        mFirstSuggestFragment.bindData(t)
                    } else {
                        mFirstSuggestFragment.onLoadDataFailure()
                    }
                }
            }
        }
        RxRetrofitService.instance.rxApiServices.requestSearch(Constants.API_KEY, Constants.PART_SNIPPET, pageToken, query, Constants.TYPE_CHANNEL)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver)
        mFirstSuggestFragment.disposables.add(disposableObserver)
    }
}