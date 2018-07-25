package com.beers.beers.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beers.beers.Classes.Beers;
import com.beers.beers.Interface.RequestDeleteFavorite;
import com.beers.beers.Interface.RequestGetFavorites;
import com.beers.beers.Main.MainBeer;
import com.beers.beers.Main.MainFavorites;
import com.beers.beers.Model.RequestResponse;
import com.beers.beers.Model.ServerResponse;
import com.beers.beers.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataAdapterFavoritos extends RecyclerView.Adapter<DataAdapterFavoritos.ViewHolder> {
    private ArrayList<Beers> beers;
    private Context mContext;
    private Beers modelBeers;

    public DataAdapterFavoritos(Context context, ArrayList<Beers> beers) {
        this.beers = beers;
        mContext = context;
    }

    @Override
    public DataAdapterFavoritos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_favorite, parent, false);
        return new DataAdapterFavoritos.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapterFavoritos.ViewHolder holder, final int position) {
        Context imageContext = holder.image.getContext();
        Picasso.with(imageContext).load(beers.get(position).getImage_url()).into(holder.image);
        holder.name.setText(beers.get(position).getName());
        holder.tagline.setText(beers.get(position).getTagline());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modelBeers =  beers.get(position);
                Intent myIntent = new Intent(view.getContext(), MainBeer.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("beer", modelBeers);
                myIntent.putExtras(bundle);
                view.getContext().startActivity(myIntent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(R.string.delete_favorite);
                builder.setMessage(R.string.mensagem_delete_itinerarie_dialog)
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteFavorite(beers.get(position).getId_favorite());
                                Intent intent = new Intent(view.getContext(),MainFavorites.class);
                                view.getContext().startActivity(intent);
                                ((Activity)view.getContext()).finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image,delete;
        TextView name,tagline;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.txt_name);
            tagline = itemView.findViewById(R.id.txt_tagline);
            delete = itemView.findViewById(R.id.img_delete);
            card = itemView.findViewById(R.id.card_favorite);
        }
    }

    public void deleteFavorite(String id){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://beer.travelingworld.com.br/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestDeleteFavorite deleteFavorite = retrofit.create(RequestDeleteFavorite.class);

        Beers beer = new Beers();
        beer.setId_favorite(id);

        RequestResponse request = new RequestResponse();
        request.setOperation("deleteFavorite");
        request.setBeers(beer);
        final Call<ServerResponse> response = deleteFavorite.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                ServerResponse resp = response.body();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.i("LOG ERROR", t.getMessage());
            }
        });

    }
}
