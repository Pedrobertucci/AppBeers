package com.beers.beers.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beers.beers.Classes.Beers;
import com.beers.beers.Interface.OnLoadMoreListener;
import com.beers.beers.Main.MainBeer;
import com.beers.beers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<Beers> beers;
    private Context mContext;
    private Beers modelBeers;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public DataAdapter(Context context, ArrayList<Beers> beers,RecyclerView recyclerView) {
        this.beers = beers;
        mContext = context;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.card, parent, false);

            viewHolder = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar1, parent, false);

            viewHolder = new ProgressViewHolder(v);
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            Context imageContext = ((ViewHolder) holder).image.getContext();

            Picasso.with(imageContext).load(beers.get(position).getImage_url()).into(((ViewHolder) holder).image);
            ((ViewHolder) holder).name.setText(beers.get(position).getName());
            ((ViewHolder) holder).tagline.setText(beers.get(position).getTagline());
            ((ViewHolder) holder).ph.setText("Ph:"+beers.get(position).getPh());
            ((ViewHolder) holder).card.setOnClickListener(new View.OnClickListener() {
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

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }



    }

    @Override
    public int getItemViewType(int position) {
        return beers.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name,tagline,ph;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.txt_name);
            tagline = itemView.findViewById(R.id.txt_tagline);
            ph = itemView.findViewById(R.id.txt_ph);
            card = itemView.findViewById(R.id.card_item_list);

        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setLoaded() {
        loading = false;
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

}
