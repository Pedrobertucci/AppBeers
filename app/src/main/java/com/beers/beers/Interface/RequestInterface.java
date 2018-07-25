package com.beers.beers.Interface;

import com.beers.beers.Classes.Beers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestInterface {

    @GET("beers?page=2&per_page=80")
    Call<List<Beers>> getJSON();
}
