package com.leafsnap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("species/search")
    Call<PlantsData> searchSpecies(@Query("q") String query, @Query("token") String token);
}