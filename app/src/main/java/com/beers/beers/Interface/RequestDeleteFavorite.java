package com.beers.beers.Interface;

import com.beers.beers.Model.RequestResponse;
import com.beers.beers.Model.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestDeleteFavorite {

    @POST("Beer/deleteFavorite")
    Call<ServerResponse> operation(@Body RequestResponse request);
}
