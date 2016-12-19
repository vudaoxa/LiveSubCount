//package net.lc.presenters
//
//import com.tieudieu.util.DebugLog
//import io.reactivex.Observable
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.observers.DisposableObserver
//import io.reactivex.schedulers.Schedulers
//import net.lc.utils.Constants
//import net.lc.utils.Models
//import net.lc.utils.network.RetrofitService
//import java.util.concurrent.Executors
//import java.util.concurrent.TimeUnit
//
//
///**
// * Created by HP on 11/28/2016.
// */
//object MainPresenter {
//    var isRequest = false
//    fun periodicRequestChannelInfo(channelName: String){
//        val scheduler = Schedulers.from(Executors.newSingleThreadExecutor())
////        val obs = Observable.interval(5, TimeUnit.SECONDS, scheduler)
////                .flatMap { tick ->  requestChannelInfo()}
////                .subsc
//        Observable
//                .interval(0, 30, TimeUnit.SECONDS)
//                .flatMap { x -> requestChannelInfo() }
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ list ->
//                    // ...
//                },
//                        { error -> Timber.e(error, "can't load users") })
////        Observable.interval(30, TimeUnit.SECONDS, scheduler)
////                .flatMap { tick -> service.getAthletes(1, 0L) }
////                // Performed on service.getAthletes() observable
////                .subscribeOn(scheduler)
////                .observeOn(AndroidSchedulers.mainThread())
////                .doOnError({ err -> Log.d("APP", "Error retrieving athletes: " + err.toString()) })
////                .retry()
////                .flatMap(???({ Observable.from() }))
////        .filter { athlete ->
////            // Process Athlete here in the filter
////            // Return true to always send messages but could filter them out too
////            true
////        }
//    }
//    fun requestChannelInfo() {
//        if (isRequest)
//            return
//        isRequest = true
//        val xx = RetrofitService.instance.service.getChannelInfo(Constants.API_KEY, Constants.PART_STATISTICS, "SkyDoesMinecraft")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith { object : DisposableObserver<Models.ChannelListResponse>(){
//                    override fun onComplete() {
//                        // do nothing
//
//                    }
//
//                    override fun onError(e: Throwable) {
//                        e.printStackTrace()
//                        isRequest = false
//                    }
//
//                    override fun onNext(response: Models.ChannelListResponse) {
//                        DebugLog.e(response.items!!.get(0).statistics!!.videoCount)
//                        isRequest = false
//                    }
//                } }
////                .subscribe { response -> { DebugLog.e(response.items!!.get(0).statistics!!.videoCount)
////                        isRequest = false},  e -> {
////                        e.printStackTrace()
////                        isRequest = false
////                    }}
//    }
//}
//
