package net.lc.network

import io.reactivex.Observable
import net.lc.models.ChannelListResponse
import net.lc.models.SearchListResponse
import net.lc.utils.NetConstants
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

/**
 * Created by HP on 11/25/2016.
 */

interface RxApiServices {

    @GET(NetConstants.API_YOUTUBE_CHANNEL)
    fun requestChannelsInfo(@Query("key") developerKey: String, @Query("part") part: String,
                            @Query("forUsername") forUsername: String?, @Query("id") ids: String?): Observable<ChannelListResponse>

    @GET(NetConstants.API_YOUTUBE_SEARCH)
    fun requestSearch(@Query("key") developerKey: String, @Query("part") part: String, @Query("pageToken") pageToken: String?,
                      @Query("q") q: String, @Query("type") type: String): Observable<SearchListResponse>


}

interface RxxApiServices {
    @Streaming
    @GET(NetConstants.API_FEATURED_FILE)
    fun requestFeaturedFileLC(@Path("date") date: String): Observable<ResponseBody>
}