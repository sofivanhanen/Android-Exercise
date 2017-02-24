package com.example.sofi.exercise;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sofi on 23.2.2017.
 */

public interface YleApiService {

    @GET("v1/programs/items.json?")
    Call<BaseData> getBaseData(@Query("app_id") String app_id, @Query("app_key") String app_key);

}
