//package net.lc.utils.network;
//
//import com.google.gson.Gson;
//import com.tieudieu.util.DebugLog;
//
//import net.lc.utils.Constants;
//import net.lc.utils.Models;
//
//import java.io.IOException;
//
//import okhttp3.Interceptor;
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class InterceptorRefreshToken implements Interceptor {
//
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            DebugLog.i("xyz~on~intercept~chain~");
//            Request original = chain.request();
//            // try the request
//            Request request = original.newBuilder()
////                    .header(Constants.INSTANCE.getKEY_AUTHORIZATION(), (Mconfig.getInstance().getAccessToken() != null ? "Bearer " + Mconfig.getInstance().getAccessToken().getAccessToken() : "Bearer xxx"))
//                    .method(original.method(), original.body())
//                    .build();
//            Response response = chain.proceed(request);
//            DebugLog.i("xyz~on~intercept~code~" + response.code());
//            if (response.code() != 401) return response;
//            else {
////                DebugLog.i("xyz~content url~"+request.url());
//                String xxx = response.body().string();
//                DebugLog.i("xyz~intercept~xxxx" + xxx);
//                Models.OnResponse onResponse = new Gson().fromJson(xxx, Models.OnResponse.class);
//                if (response.code() == 401 && onResponse != null && onResponse.getErrors() != null && onResponse.getErrors().getCode() != null) {
//                    DebugLog.i("xyz~onResponse.status-code" + onResponse.getStatusCode());
//                    DebugLog.i("xyz~onResponse.status-code" + onResponse.getErrors().getCode());
//                    if (onResponse.getErrors().getCode().equals(Constants.ERROR_TOKEN_EXPIRE)) {
//                        if (Mconfig.getInstance().getAccessToken() != null && !isRefreshToken) {
//                            isRefreshToken = true;
//                            Call<AccessToken> requestRefreshToken = service.requestRefeshToken(Constants.GRANT_TYPE_REFRESH_TOKEN,
//                                    (Mconfig.getInstance().getAccessToken() != null ? Mconfig.getInstance().getAccessToken().getRefreshToken() : ""),
//                                    Constants.CLIENT_ID, Constants.CLIENT_SECRET);
//                            retrofit2.Response<AccessToken> execute = requestRefreshToken.execute();
//
//                            if (execute.isSuccessful()) {
//                                if (execute.code() == 200 && execute.body() != null) {
//                                    Mconfig.getInstance().saveAccessToken(execute.body().getData());
//                                    Request newRequest = request.newBuilder().removeHeader(Constants.KEY_AUTHORIZATION).addHeader(Constants.KEY_AUTHORIZATION, (Mconfig.getInstance().getAccessToken() != null ? "Bearer " + Mconfig.getInstance().getAccessToken().getAccessToken() : "Bearer xxx")).build();
//                                    isRefreshToken = false;
//                                    return chain.proceed(newRequest);
//                                }
//                            }
//                            isRefreshToken = false;
//                        }
//                    }
//                }
//                return response.newBuilder()
//                        .body(ResponseBody.create(response.body().contentType(), xxx))
//                        .build();
//            }
//        }
//    }