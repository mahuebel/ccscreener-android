package com.huebelancer.ccscreener.Activities.StockDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;
import com.huebelancer.ccscreener.R;

import java.util.List;

/**
 * Created by mahuebel on 8/15/17.
 */

class DetailViewAdapter extends RecyclerView.Adapter<DetailViewHolder>{

    List<String> frames;
    Stock stock;

    public DetailViewAdapter(Stock stock, List<String> frames) {
        this.stock = stock;
        this.frames = frames;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View detailView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item, parent, false);
        DetailViewHolder viewHolder = new DetailViewHolder(detailView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {

        String frame = frames.get(position);
        holder.configureWith(stock, frame);

    }

    @Override
    public int getItemCount() {
        return stock != null ? frames.size() : 0;
    }
}
