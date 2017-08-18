package com.huebelancer.ccscreener.Helpers;

/**
 * Created by mahuebel on 8/14/17.
 */

public class Constants {

    //region Collections

    public static final String COLLECTION_STOCKS  = "stocks";
    public static final String COLLECTION_OPTIONS = "options";

    //endregion


    //region Subscriptions

    public static final String SUBSCRIBE_OPTIONABLE = "stocks.optionables";
    public static final String SUBSCRIBE_ALL = "stocks.all";
    public static final String SUBSCRIBE_BY_ID = "stock.byId";

    //endregion


    //region Stock Fields

    public static final String FIELD_ASKS = "asks";
    public static final String FIELD_BIDS = "bids";
    public static final String FIELD_PRICE = "price";
    public static final String FIELD_SYMBOL = "symbol";
    public static final String FIELD_STRIKES = "strikes";
    public static final String FIELD_PROFITS = "profits";
    public static final String FIELD_EXCHANGE = "exchange";
    public static final String FIELD_PREMIUMS = "premiums";
    public static final String FIELD_IS_OPTION = "isOption";
    public static final String FIELD_VOLATILITIES = "volatilities";
    public static final String FIELD_PROBABILITIES = "probabilities";

    public static final String FIELD_PRICE_UPDATED = "priceUpdatedAt";
    public static final String FIELD_CHAIN_UPDATED = "chainUpdatedAt";
    public static final String FIELD_EARNINGS_UPDATED = "earningsUpdatedAt";

    public static final String FIELD_CURRENT_CALL_DATE = "currentCallDate";
    public static final String FIELD_NEXT_CALL_DATE    = "nextCallDate";
    public static final String FIELD_EARNINGS_RPT_DATE = "earningsRptDate";

    public static final String CREATED_AT = "createdAt";
    public static final String UPDATED_AT = "updatedAt";

    //endregion


    //region Frames

    public static final String CURRENT_ITM = "currentItm";
    public static final String CURRENT_OTM = "currentOtm";
    public static final String NEXT_ITM    = "nextItm";
    public static final String NEXT_OTM    = "nextOtm";

    //endregion


    public static final String stockIdKey = "stockId";
}
