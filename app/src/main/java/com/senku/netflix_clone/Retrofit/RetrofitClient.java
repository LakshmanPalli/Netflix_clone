package com.senku.netflix_clone.Retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final static String BASE_URL="https://androidappsforyoutube.s3.ap-south-1.amazonaws.com/primevideo/api/";
    private final APIinterface getRetrofitClient(){
        Retrofit.Builder builder=new Retrofit.Builder()
                                     .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //we use RxJava to make retrofit calls
                                     .addConverterFactory(GsonConverterFactory.create()) //converting to Gson(more readable form)
                                     .baseUrl(BASE_URL);
        return builder.build().create(APIinterface.class);
    }
}

/*imp notes
RxAndroid is an extension of RxJava and it contains the Android threads to be used in the Android Environment. To use RxJava in retrofit environment we need to do just two major changes: Add the RxJava in Retrofit Builder. Use Observable type in the interface instead of Call.
 */
