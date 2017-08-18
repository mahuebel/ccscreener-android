package com.huebelancer.ccscreener.Activities.StockList;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;
import com.huebelancer.ccscreener.R;

import static com.huebelancer.ccscreener.Helpers.Helpers.iconResIdFor;

/**
 * Created by mahuebel on 8/14/17.
 */

class StockViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "StockViewHolder";

    Context context;
    CardView cv;
    TextView symbol;
    TextView price;
    TextView profit;
    TextView probability;
    ImageView icon;


    public StockViewHolder(View itemView) {
        super(itemView);

        this.context = itemView.getContext();
        this.cv = (CardView) itemView.findViewById(R.id.card_view);
        this.symbol = (TextView) itemView.findViewById(R.id.item_symbol);
        this.price = (TextView) itemView.findViewById(R.id.item_price);
        this.profit = (TextView) itemView.findViewById(R.id.item_profit);
        this.probability = (TextView) itemView.findViewById(R.id.item_probability);
        this.icon = (ImageView) itemView.findViewById(R.id.item_icon);
    }

    public void configureWith(Stock stock, Frame frame) {
        Resources res = context.getResources();

        symbol.setText(stock.symbol);
        price.setText(String.valueOf(stock.price));

        double min = stock.minProfit(frame);
        double max = stock.maxProfit(frame);
        String prof = res.getString(R.string.item_profit) + " " + max + "% - " + min + "%";
        profit.setText(prof);

        double probab = stock.probability(frame);
        String prob = res.getString(R.string.item_probability) + " " + probab + "%";
        probability.setText(prob);

        icon.setImageResource(iconResIdFor(stock, frame));

    }
}
