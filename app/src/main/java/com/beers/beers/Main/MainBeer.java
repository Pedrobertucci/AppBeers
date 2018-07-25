package com.beers.beers.Main;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beers.beers.Classes.Beers;
import com.beers.beers.Interface.RequestAddBeers;
import com.beers.beers.Model.RequestResponse;
import com.beers.beers.Model.ServerResponse;
import com.beers.beers.R;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainBeer extends AppCompatActivity {
    private Context context;
    private Beers beer;
    private TextView name_beer, descricao, ph;
    private ImageView img_beer;
    private Toolbar toolbar;
    private FloatingActionButton button;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_beer);

        context = getApplicationContext();
        beer = new Beers();

        if(getIntent().hasExtra("beer")){
            Bundle extras = getIntent().getExtras();
            beer= (Beers) getIntent().getExtras().getSerializable("beer");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle(beer.getName());
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.CollapsedAppBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name_beer = (TextView) findViewById(R.id.txt_name_beer);
        ph = (TextView) findViewById(R.id.txt_ph);
        descricao = (TextView) findViewById(R.id.txt_descricao);
        img_beer = (ImageView) findViewById(R.id.image_beer);
        button = (FloatingActionButton) findViewById(R.id.floating_comment_attraction);
        Context imageContext = img_beer.getContext();
        Glide.with(imageContext)
                .load(beer.getImage_url())
                .into(img_beer);

        name_beer.setText(beer.getTagline());
        descricao.setText(beer.getDescription());
        ph.setText("Ph: "+beer.getPh()+" Abv: "+beer.getAbv());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBeer();
            }
        });
    }

    public void addBeer(){
        progress = new ProgressDialog(this, R.style.styleDialogProgress);
        progress.setTitle("");
        progress.setMessage(context.getResources().getString(R.string.addFavorite));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beer.travelingworld.com.br/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestAddBeers requestInterface = retrofit.create(RequestAddBeers.class);

        Beers beer = new Beers();
        beer.setId(this.beer.getId());
        beer.setName(this.beer.getName());
        beer.setTagline(this.beer.getTagline());
        beer.setImage_url(this.beer.getImage_url());
        beer.setDescription(this.beer.getDescription());
        beer.setFirst_brewed(this.beer.getFirst_brewed());
        beer.setAbv(this.beer.getAbv());
        beer.setPh(this.beer.getPh());
        beer.setMac(getDeviceName());

        RequestResponse request = new RequestResponse();
        request.setOperation("addFavorites");
        request.setBeers(beer);
        final Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();

                if (resp.getResult().equals("success")) {
                    progress.dismiss();
                    Toast.makeText(MainBeer.this, "Favorito Cadastrado", Toast.LENGTH_SHORT).show();


                } else if (resp.getResult().equals("error")) {
                    progress.dismiss();
                    Toast.makeText(MainBeer.this, "Favorito Já Cadastrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(context, " Erro: "+ t + "", Toast.LENGTH_SHORT).show();
            }
        });

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
