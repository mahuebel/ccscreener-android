package com.huebelancer.ccscreener.Activities.StockDetails;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huebelancer.ccscreener.Helpers.Helpers;
import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;
import com.huebelancer.ccscreener.ModelLayer.Enums.Frame;
import com.huebelancer.ccscreener.R;

import java.text.SimpleDateFormat;

import static com.huebelancer.ccscreener.Helpers.Helpers.dateDisplay;
import static com.huebelancer.ccscreener.Helpers.Helpers.frameLabelToDisplay;
import static com.huebelancer.ccscreener.Helpers.Helpers.labelToFrame;

/**
 * Created by mahuebel on 8/15/17.
 */

class DetailViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = "DetailViewHolder";

    Context context;
    CardView cv;
    TextView frame, strike, ask, premium,
            profitabilityLabel, profitability,
            probabilityLabel, probability,
            volatility;
    ImageView profitIcon, probabilityIcon;

    public DetailViewHolder(View v) {
        super(v);

        this.context = v.getContext();
        this.cv = (CardView) v.findViewById(R.id.card_view);
        this.frame = (TextView) v.findViewById(R.id.frame);
        this.strike = (TextView) v.findViewById(R.id.strike);
        this.ask = (TextView) v.findViewById(R.id.ask);
        this.premium = (TextView) v.findViewById(R.id.premium);
        this.volatility = (TextView) v.findViewById(R.id.volatility);

        this.profitabilityLabel = (TextView) v.findViewById(R.id.profitability_label);
        this.profitability = (TextView) v.findViewById(R.id.profitability);

        this.probabilityLabel = (TextView) v.findViewById(R.id.probability_label);
        this.probability = (TextView) v.findViewById(R.id.probability);

        this.profitIcon = (ImageView) v.findViewById(R.id.profit_icon);
        this.probabilityIcon = (ImageView) v.findViewById(R.id.probability_icon);

    }

    public void configureWith(Stock stock, String f) {

        Frame frame_enum = labelToFrame(f);

        String date = dateDisplay(
                stock.callDate(frame_enum),
                new SimpleDateFormat("MMM dd")
        ) + " - ";
        frame.setText(date + frameLabelToDisplay(f));

        double s = stock.strike(f);
        strike.setText("$"+s);

        double a = stock.ask(f);
        ask.setText("$"+a);

        double p = stock.premium(f);
        premium.setText("$"+p);


        double min = stock.minProfit(frame_enum);
        double max = stock.maxProfit(frame_enum);
        String prof = max + "% / " + min + "%";
        profitability.setText(prof);

        double probab = stock.probability(frame_enum);
        String prob = probab + "%";
        probability.setText(prob);

        setupColoration(
                isProfitable(Math.min(min, max)),
                isProbable(probab)
        );

        volatility.setText(stock.volatility(frame_enum) + "%");

    }

    private void setupColoration(boolean isProfitable, boolean isProbable) {
        int profitColor = isProfitable ? R.color.colorPositive : R.color.colorNegative;
        int probabilityColor = isProbable ? R.color.colorPositive : R.color.colorNegative;

        profitability.setTextColor(
                ContextCompat.getColor(context, profitColor)
        );

        profitabilityLabel.setTextColor(
                ContextCompat.getColor(context, profitColor)
        );

        probabilityLabel.setTextColor(
                ContextCompat.getColor(context, probabilityColor)
        );

        probability.setTextColor(
                ContextCompat.getColor(context, probabilityColor)
        );



        profitIcon.setImageResource(
                Helpers.profitEvalutationIcon(isProfitable)
        );

        probabilityIcon.setImageResource(
                Helpers.probabilityEvaluationIcon(isProbable)
        );



        DrawableCompat.setTint(
                profitIcon.getDrawable(),
                ContextCompat.getColor(context, profitColor)
        );

        DrawableCompat.setTint(
                probabilityIcon.getDrawable(),
                ContextCompat.getColor(context, probabilityColor)
        );
    }

    private boolean isProbable(double probability) {
        return probability > 49;
    }

    private boolean isProfitable(double profit) {
        return profit > 0;
    }
}
