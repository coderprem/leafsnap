package com.leafsnap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface weatherapi {
    @GET("weather")
    Call<target_weather> getweather(@Query("q") String cityname,
                                    @Query("appid") String apikey);
}
