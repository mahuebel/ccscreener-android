package com.huebelancer.ccscreener.ModelLayer.Translation;

import com.huebelancer.ccscreener.ModelLayer.Database.Models.Stock;

import java.util.List;

import im.delight.android.ddp.db.Document;

/**
 * Created by mahuebel on 8/14/17.
 */

public class TranslationLayer {

    StockTranslator translator;

    public TranslationLayer(StockTranslator translator) {
        this.translator = translator;
    }

    public Stock translate(Document from) {
        return translator.translate(from);
    }

    public List<Stock> translate(Document[] docs) {
        return translator.translate(docs);
    }

}
