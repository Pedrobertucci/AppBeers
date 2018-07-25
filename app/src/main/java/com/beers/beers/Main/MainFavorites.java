package com.beers.beers.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.beers.beers.Adapters.DataAdapterFavoritos;
import com.beers.beers.Classes.Beers;
import com.beers.beers.Interface.RequestAddBeers;
import com.beers.beers.Interface.RequestGetFavorites;
import com.beers.beers.Main.MainBeer;
import com.beers.beers.Model.RequestResponse;
import com.beers.beers.Model.ServerResponse;
import com.beers.beers.R;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFavorites extends AppCompatActivity {
    private ProgressDialog progress;
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<Beers> dataBeers;
    private TextView txt_no_favorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_favorites);
        context = getApplicationContext();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        txt_no_favorite = (TextView) findViewById(R.id.txt_no_favorite);
        getFavorites();
    }



    public void getFavorites(){
        progress = new ProgressDialog(this, R.style.styleDialogProgress);
        progress.setTitle("");
        progress.setMessage(context.getResources().getString(R.string.search_beer));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beer.travelingworld.com.br/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestGetFavorites requestInterface = retrofit.create(RequestGetFavorites.class);

        Beers beer = new Beers();
        beer.setMac(getDeviceName());

        RequestResponse request = new RequestResponse();
        request.setOperation("getFavorites");
        request.setBeers(beer);
        final Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                if (resp.getResult().equals("success")) {
                    dataBeers = new ArrayList<>(Arrays.asList(resp.getBeers()));
                    initRecyclerView();
                    progress.dismiss();

                } else if (resp.getResult().equals("error")) {
                    progress.dismiss();

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.dismiss();
                txt_no_favorite.setVisibility(View.VISIBLE);


            }
        });
    }

    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerview_favorites);
        recyclerView.setLayoutManager(layoutManager);
        DataAdapterFavoritos adapter = new DataAdapterFavoritos(this, dataBeers);
        recyclerView.setAdapter(adapter);
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();  // finish() here.

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
