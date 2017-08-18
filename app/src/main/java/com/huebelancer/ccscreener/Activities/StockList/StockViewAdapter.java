package com.huebelancer.ccscreener.Activities.StockList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huebelancer.ccscreener.Helpers.CustomItemClickListener;
import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;
import com.huebelancer.ccscreener.R;

import java.util.List;

/**
 * Created by mahuebel on 8/14/17.
 */

class StockViewAdapter extends RecyclerView.Adapter<StockViewHolder> {

    List<Stock> stocks;
    Frame frame;
    CustomItemClickListener listener;

    public StockViewAdapter(List<Stock> stocks, Frame frame, CustomItemClickListener listener) {
        this.stocks   = stocks;
        this.frame    = frame;
        this.listener = listener;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View stockView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);
        final StockViewHolder stockViewHolder = new StockViewHolder(stockView);
        stockView.setOnClickListener(view -> {
            listener.onItemClick(view, stockViewHolder.getAdapterPosition());
        });

        return stockViewHolder;
    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {

        Stock stock = stocks.get(position);
        holder.configureWith(stock, frame);

    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }
}
