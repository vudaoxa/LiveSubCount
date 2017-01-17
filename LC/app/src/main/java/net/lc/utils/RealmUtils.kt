//package net.lc.utils
//
//import android.content.Context
//import com.tieudieu.util.DebugLog
//import io.realm.Realm
//import io.realm.RealmObject
//import net.lc.models.SearchQueryRealm
//import net.lc.models.SearchResult
//import net.lc.models.SearchResultRealm
//
//
///**
// * Created by mrvu on 1/6/17.
// */
//
//object RealmUtils {
//    fun saveSearchQuery(query: String) {
//        val realm = Realm.getDefaultInstance()
//        val searchQueryRealm = SearchQueryRealm(query, System.currentTimeMillis())
//        realm.beginTransaction()
//        realm.copyToRealmOrUpdate(searchQueryRealm)
//        realm.commitTransaction()
//    }
//
//    fun saveObject(obj: RealmObject){
//        val realm = Realm.getDefaultInstance()
//        realm.beginTransaction()
//        realm.copyToRealmOrUpdate(obj)
//        realm.commitTransaction()
//    }
//
//    fun saveObject(obj: SearchResult){
//        val realm = Realm.getDefaultInstance()
//        realm.beginTransaction()
//        realm.copyToRealmOrUpdate(obj)
//        realm.commitTransaction()
//    }
//    fun updateSearchResultRealm(searchResultRealm: SearchResultRealm?, following: Int, time: Long){
//        searchResultRealm?.apply {
//            val realm = Realm.getDefaultInstance()
//            realm.beginTransaction()
//            this.following = following
//            if(time != -1L){
//                this.time = time
//            }
//            realm.copyToRealmOrUpdate(searchResultRealm)
//            realm.commitTransaction()
////            realm.refresh()
//        }
//    }
//
//    fun updateSearchResultRealm(searchResultRealm: SearchResultRealm,  following: Int, time: Long){
//
//        searchResultRealm?.apply {
//            val realm = Realm.getDefaultInstance()
//            realm.executeTransactionAsync(Realm.Transaction { bgRealm->
//                this.following = following
//                if(time != -1L){
//                    this.time = time
//                }
//            }, Realm.Transaction.OnSuccess {
//                DebugLog.e("success")
//            })
//
//        }
//    }
//}
